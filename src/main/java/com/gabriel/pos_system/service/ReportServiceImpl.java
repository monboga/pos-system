package com.gabriel.pos_system.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gabriel.pos_system.dto.SaleTicketDetailDto;
import com.gabriel.pos_system.model.Business;
import com.gabriel.pos_system.model.Sale;
import com.gabriel.pos_system.repository.BusinessRepository;
import com.gabriel.pos_system.repository.SaleRepository;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServiceImpl implements ReportService {
    private final SaleRepository saleRepository;
    private final BusinessRepository businessRepository;

    public ReportServiceImpl(SaleRepository saleRepository, BusinessRepository businessRepository) {
        this.saleRepository = saleRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    public void generateSaleTicket(Long saleId, HttpServletResponse response) {
        try {
            Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new RuntimeException("Venta no encontrada"));
            // Usamos findTopByOrderByIdAsc para ser más eficientes al obtener la única
            // entidad Business
            Business business = businessRepository.findAll().stream().findFirst().orElse(new Business());

            Locale localeMx = new Locale("es", "MX");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeMx);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            List<SaleTicketDetailDto> detailsDto = sale.getDetails().stream()
                    .map(detail -> new SaleTicketDetailDto(
                            detail.getProduct().getDescripcion(),
                            detail.getCantidad(),
                            currencyFormatter.format(detail.getPrecioUnitario()),
                            currencyFormatter.format(detail.getTotalProducto())))
                    .collect(Collectors.toList());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detailsDto);

            Map<String, Object> parameters = new HashMap<>();

            // --- INICIO DE LA CORRECCIÓN DEL LOGO ---
            InputStream logoStream = null;
            if (business.getLogo() != null && !business.getLogo().isEmpty()) {
                // 1. Separamos el prefijo "data:image/png;base64,"
                String[] parts = business.getLogo().split(",");
                if (parts.length == 2) {
                    // 2. Decodificamos la segunda parte (el Base64 puro) a bytes
                    byte[] logoBytes = Base64.getDecoder().decode(parts[1]);
                    // 3. Creamos el InputStream a partir de los bytes
                    logoStream = new ByteArrayInputStream(logoBytes);
                }
            }
            parameters.put("businessLogo", logoStream);
            // --- FIN DE LA CORRECCIÓN DEL LOGO ---

            parameters.put("businessName", business.getRazonSocial()); // Usamos razonSocial que es más apropiado
            parameters.put("saleNumber", sale.getNumeroVenta());
            parameters.put("saleDate", sale.getFechaRegistro().format(dateFormatter));
            parameters.put("clientName", sale.getClient().getNombre());
            parameters.put("clientRfc", sale.getClient().getRfc());
            parameters.put("saleSubtotal", currencyFormatter.format(sale.getSubtotal()));
            parameters.put("saleIva", currencyFormatter.format(sale.getIva()));
            parameters.put("saleTotal", currencyFormatter.format(sale.getTotal()));

            InputStream reportStream = getClass().getResourceAsStream("/reports/ticket_template.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "inline; filename=ticket_venta_" + sale.getNumeroVenta() + ".pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
