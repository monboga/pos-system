package com.gabriel.pos_system.dto;

public class SaleTicketDetailDto {

    private String productDescription;
    private Integer quantity;
    private String unitPrice;
    private String totalPrice;

    public SaleTicketDetailDto(String productDescription, Integer quantity, String unitPrice, String totalPrice) {
        this.productDescription = productDescription;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

}
