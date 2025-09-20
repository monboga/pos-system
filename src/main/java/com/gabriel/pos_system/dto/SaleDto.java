package com.gabriel.pos_system.dto;

import java.util.List;

public class SaleDto {

    private String clientRfc;
    private List<CartItemDto> items;

    public String getClientRfc() {
        return clientRfc;
    }

    public void setClientRfc(String clientRfc) {
        this.clientRfc = clientRfc;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

}
