package com.gabriel.pos_system.dto;

import java.util.List;

public class SaleViewDto {

    private String saleNumber;
    private String documentType;
    private String clientName;
    private String clientRfc;
    private double subtotal;
    private double iva;
    private double total;
    private List<SaleDetailDto> details;

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientRfc() {
        return clientRfc;
    }

    public void setClientRfc(String clientRfc) {
        this.clientRfc = clientRfc;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<SaleDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetailDto> details) {
        this.details = details;
    }

}
