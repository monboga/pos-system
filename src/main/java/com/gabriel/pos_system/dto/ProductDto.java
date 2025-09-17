package com.gabriel.pos_system.dto;

public class ProductDto {

    private Long id;
    private String codigoDeBarra, marca, descripcion;
    private Long categoryId;
    private Integer stock, estado;
    private Double precioUnitario, descuento;
    private String medidaLocalId, medidaSatId, claveProdServSatId, objetoImpSatId, impuestoSatId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoDeBarra() {
        return codigoDeBarra;
    }

    public void setCodigoDeBarra(String codigoDeBarra) {
        this.codigoDeBarra = codigoDeBarra;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public String getMedidaLocalId() {
        return medidaLocalId;
    }

    public void setMedidaLocalId(String medidaLocalId) {
        this.medidaLocalId = medidaLocalId;
    }

    public String getMedidaSatId() {
        return medidaSatId;
    }

    public void setMedidaSatId(String medidaSatId) {
        this.medidaSatId = medidaSatId;
    }

    public String getClaveProdServSatId() {
        return claveProdServSatId;
    }

    public void setClaveProdServSatId(String claveProdServSatId) {
        this.claveProdServSatId = claveProdServSatId;
    }

    public String getObjetoImpSatId() {
        return objetoImpSatId;
    }

    public void setObjetoImpSatId(String objetoImpSatId) {
        this.objetoImpSatId = objetoImpSatId;
    }

    public String getImpuestoSatId() {
        return impuestoSatId;
    }

    public void setImpuestoSatId(String impuestoSatId) {
        this.impuestoSatId = impuestoSatId;
    }

}
