package com.gabriel.pos_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gabriel.pos_system.service.ReportService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales/ticket/{saleId}")
    public void generateSaleTicket(@PathVariable Long saleId, HttpServletResponse response) {
        // La única responsabilidad del controlador es delegar la tarea al servicio.
        reportService.generateSaleTicket(saleId, response);
    }

}
