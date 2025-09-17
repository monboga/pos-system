package com.gabriel.pos_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "El RFC no puede estar vacío.")
    @Column(nullable = false, unique = true, length = 13)
    private String rfc;

    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Column(nullable = false)
    private String nombre;

    @NotEmpty(message = "El correo no puede estar vacío.")
    @Email(message = "El formato del correo no es válido.")
    @Column(nullable = false)
    private String correo;

    @NotNull
    @Column(nullable = false)
    private Integer activo; // 1 para Activo, 0 para Inactivo

    @NotNull(message = "Debe seleccionar un régimen fiscal.")
    @ManyToOne
    @JoinColumn(name = "regimen_fiscal_id", nullable = false)
    private RegimenFiscal regimenFiscal;

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

    public RegimenFiscal getRegimenFiscal() {
        return regimenFiscal;
    }

    public void setRegimenFiscal(RegimenFiscal regimenFiscal) {
        this.regimenFiscal = regimenFiscal;
    }

}
