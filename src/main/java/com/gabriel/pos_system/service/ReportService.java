package com.gabriel.pos_system.service;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportService {
    void generateSaleTicket(Long saleId, HttpServletResponse response);
}
