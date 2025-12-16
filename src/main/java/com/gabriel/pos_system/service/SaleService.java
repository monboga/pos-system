package com.gabriel.pos_system.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.gabriel.pos_system.domain.model.User;
import com.gabriel.pos_system.dto.SaleDto;
import com.gabriel.pos_system.dto.SaleReportViewDto;
import com.gabriel.pos_system.model.Sale;

public interface SaleService {

    Sale createSale(SaleDto saleDto, User loggedInUser);

    Page<SaleReportViewDto> findPaginatedSales(LocalDate startDate, LocalDate endDate, int page, int size);
}
