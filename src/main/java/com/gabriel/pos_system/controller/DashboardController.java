package com.gabriel.pos_system.controller;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gabriel.pos_system.repository.CategoryRepository;
import com.gabriel.pos_system.repository.ProductRepository;
import com.gabriel.pos_system.repository.SaleDetailRepository;
import com.gabriel.pos_system.repository.SaleRepository;

@Controller
public class DashboardController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;

    // Inyectamos los repositorios que necesitamos para contar los registros.
    public DashboardController(ProductRepository productRepository, CategoryRepository categoryRepository,
            SaleRepository saleRepository, SaleDetailRepository saleDetailRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.saleRepository = saleRepository;
        this.saleDetailRepository = saleDetailRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboardPage(Model model) {
        // --- Lógica para las Cards ---
        long totalProducts = productRepository.count();
        long totalCategories = categoryRepository.count();
        long totalSales = saleRepository.count();

        Double totalRevenueDouble = saleRepository.findTotalRevenue();
        // Formateador para moneda (ej. $84,320.00)
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        String totalRevenue = currencyFormatter.format(totalRevenueDouble != null ? totalRevenueDouble : 0.0);

        // Formateador para números (ej. 1,250)
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(new Locale("es", "MX"));

        model.addAttribute("totalProducts", numberFormatter.format(totalProducts));
        model.addAttribute("totalCategories", numberFormatter.format(totalCategories));
        model.addAttribute("totalSales", numberFormatter.format(totalSales));
        model.addAttribute("totalRevenue", totalRevenue);

        return "dashboard";
    }

    @GetMapping("/dashboard/sales-last-7-days")
    @ResponseBody
    public Map<String, Object> getSalesLast7Days() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(6);
        List<Object[]> salesData = saleRepository.findSalesLast7Days(startDate);

        // Usamos un LinkedHashMap para mantener el orden de los días
        Map<String, Double> salesByDay = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES"));

        // Inicializamos los últimos 7 días con 0 ventas
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            salesByDay.put(date.format(dayFormatter), 0.0);
        }

        // Poblamos el mapa con los datos reales de la BD
        for (Object[] result : salesData) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            Double sum = (Double) result[1];
            salesByDay.put(date.format(dayFormatter), sum);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("labels", salesByDay.keySet());
        response.put("data", salesByDay.values());
        return response;
    }

    @GetMapping("/dashboard/top-selling-products")
    @ResponseBody
    public Map<String, Object> getTopSellingProducts() {
        // Obtenemos el TOP 5 de productos más vendidos
        List<Object[]> productData = saleDetailRepository.findTopSellingProducts(PageRequest.of(0, 5));

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("labels", productData.stream().map(p -> (String) p[0]).collect(Collectors.toList()));
        response.put("data", productData.stream().map(p -> (Long) p[1]).collect(Collectors.toList()));
        return response;
    }
}
