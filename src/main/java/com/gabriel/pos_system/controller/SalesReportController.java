package com.gabriel.pos_system.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gabriel.pos_system.dto.SaleReportViewDto;
import com.gabriel.pos_system.service.SaleService;

@Controller
public class SalesReportController {
    private final SaleService saleService;

    public SalesReportController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping("/sales-report")
    public String showSalesReportPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        // 1. Llamamos al servicio para obtener la página de ventas
        Page<SaleReportViewDto> salesPage = saleService.findPaginatedSales(startDate, endDate, page, size);

        // 2. Pasamos la página de ventas a la vista
        model.addAttribute("salesPage", salesPage);

        // 3. Devolvemos los parámetros de filtro para mantener el estado en la UI
        model.addAttribute("selectedSize", size);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "sales-report";
    }
}
