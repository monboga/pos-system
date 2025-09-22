package com.gabriel.pos_system.dto;

import java.util.List;

public class SaleReportViewDto {

    private String documentType;
    private String clientRfc;
    private String clientName;
    private String subtotal; // Será String para incluir el formato de moneda
    private String iva; // Será String para incluir el formato de moneda
    private String total; // Será String para incluir el formato de moneda
    private List<SaleReportDetailViewDto> details;

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getClientRfc() {
        return clientRfc;
    }

    public void setClientRfc(String clientRfc) {
        this.clientRfc = clientRfc;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<SaleReportDetailViewDto> getDetails() {
        return details;
    }

    public void setDetails(List<SaleReportDetailViewDto> details) {
        this.details = details;
    }

}
