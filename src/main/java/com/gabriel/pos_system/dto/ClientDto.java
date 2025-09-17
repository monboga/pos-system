package com.gabriel.pos_system.dto;

public class ClientDto {
    private Long id;
    private String rfc;
    private String nombre;
    private String correo;
    private Integer activo;
    private String regimenFiscalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getRegimenFiscalId() {
        return regimenFiscalId;
    }

    public void setRegimenFiscalId(String regimenFiscalId) {
        this.regimenFiscalId = regimenFiscalId;
    }

}
