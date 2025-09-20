package com.gabriel.pos_system.service;

import com.gabriel.pos_system.dto.SaleDto;
import com.gabriel.pos_system.model.Sale;
import com.gabriel.pos_system.model.User;

public interface SaleService {

    Sale createSale(SaleDto saleDto, User loggedInUser);
}
