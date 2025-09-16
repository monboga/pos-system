package com.gabriel.pos_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "regimen_fiscal")
public class RegimenFiscal {

    @Id
    @Column(name = "c_regimen_fiscal", length = 3)
    private String cRegimenFiscal;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private boolean fisica;

    @Column(nullable = false)
    private boolean moral;

    public String getcRegimenFiscal() {
        return cRegimenFiscal;
    }

    public void setcRegimenFiscal(String cRegimenFiscal) {
        this.cRegimenFiscal = cRegimenFiscal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isFisica() {
        return fisica;
    }

    public void setFisica(boolean fisica) {
        this.fisica = fisica;
    }

    public boolean isMoral() {
        return moral;
    }

    public void setMoral(boolean moral) {
        this.moral = moral;
    }

}
