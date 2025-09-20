package com.gabriel.pos_system.dto;

import java.time.LocalDateTime;

public class SaleHistoryRowDto {
    private Long id;
    private LocalDateTime fechaRegistro;
    private String numeroVenta;
    private String documentType;
    private String clientRfc;
    private String clientNombre;
    private Double total;
    private String detailsJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

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

    public String getClientNombre() {
        return clientNombre;
    }

    public void setClientNombre(String clientNombre) {
        this.clientNombre = clientNombre;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDetailsJson() {
        return detailsJson;
    }

    public void setDetailsJson(String detailsJson) {
        this.detailsJson = detailsJson;
    }

}
