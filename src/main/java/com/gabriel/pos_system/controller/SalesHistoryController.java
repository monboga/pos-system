package com.gabriel.pos_system.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.pos_system.dto.SaleDetailDto;
import com.gabriel.pos_system.dto.SaleHistoryRowDto;
import com.gabriel.pos_system.dto.SaleViewDto;
import com.gabriel.pos_system.model.Sale;
import com.gabriel.pos_system.repository.SaleRepository;

@Controller
public class SalesHistoryController {
    private final SaleRepository saleRepository;
    private final ObjectMapper objectMapper;

    public SalesHistoryController(SaleRepository saleRepository, ObjectMapper objectMapper) {
        this.saleRepository = saleRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/sales-history")
    public String showSalesHistoryPage(
            @RequestParam(required = false) String filterType,
            @RequestParam(required = false) String saleNumber,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {

        List<Sale> sales;

        if ("saleNumber".equals(filterType) && saleNumber != null && !saleNumber.trim().isEmpty()) {
            // Filtrar por número de venta
            sales = saleRepository.findByNumeroVentaContainingIgnoreCase(saleNumber);
        } else if ("date".equals(filterType) && startDate != null && !startDate.isEmpty() && endDate != null
                && !endDate.isEmpty()) {
            // Filtrar por rango de fechas
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            sales = saleRepository.findByFechaRegistroBetween(start.atStartOfDay(), end.atTime(LocalTime.MAX));
        } else {
            // Por defecto, obtener todas las ventas
            sales = saleRepository.findAllByOrderByIdDesc();
        }

        List<SaleHistoryRowDto> salesDtos = new ArrayList<>();
        for (Sale sale : sales) {
            SaleHistoryRowDto dto = new SaleHistoryRowDto();
            dto.setId(sale.getId());
            dto.setFechaRegistro(sale.getFechaRegistro());
            dto.setNumeroVenta(sale.getNumeroVenta());
            dto.setDocumentType(sale.getDocumentType());
            dto.setClientRfc(sale.getClient().getRfc());
            dto.setClientNombre(sale.getClient().getNombre());
            dto.setTotal(sale.getTotal());
            try {
                dto.setDetailsJson(objectMapper.writeValueAsString(sale.getDetails()));
            } catch (JsonProcessingException e) {
                dto.setDetailsJson("[]");
            }
            salesDtos.add(dto);
        }

        model.addAttribute("sales", salesDtos);
        // Devolvemos los parámetros a la vista para mantener el estado de los filtros
        model.addAttribute("selectedFilterType", filterType);
        model.addAttribute("saleNumberValue", saleNumber);
        model.addAttribute("startDateValue", startDate);
        model.addAttribute("endDateValue", endDate);

        return "sales-history";
    }

    @GetMapping("/sales-history/details/{id}")
    @ResponseBody // Indica que la respuesta es JSON, no una vista HTML
    public ResponseEntity<SaleViewDto> getSaleDetails(@PathVariable Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // Mapeamos la entidad a nuestro DTO de vista
        SaleViewDto dto = new SaleViewDto();
        dto.setSaleNumber(sale.getNumeroVenta());
        dto.setDocumentType("Ticket"); // Valor estático por ahora
        dto.setClientName(sale.getClient().getNombre());
        dto.setClientRfc(sale.getClient().getRfc());
        dto.setSubtotal(sale.getSubtotal());
        dto.setIva(sale.getIva());
        dto.setTotal(sale.getTotal());

        // Mapeamos los detalles
        dto.setDetails(sale.getDetails().stream().map(detail -> {
            SaleDetailDto detailDto = new SaleDetailDto();
            detailDto.setProductName(detail.getProduct().getDescripcion());
            detailDto.setQuantity(detail.getCantidad());
            detailDto.setPrice(detail.getPrecioUnitario());
            detailDto.setTotal(detail.getTotalProducto());
            return detailDto;
        }).collect(Collectors.toList()));

        return ResponseEntity.ok(dto);
    }
}
