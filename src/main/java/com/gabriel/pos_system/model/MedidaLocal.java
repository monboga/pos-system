package com.gabriel.pos_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "c_medida_local")
public class MedidaLocal {
    @Id
    private String clave;
    private String descripcionMedida;

    public MedidaLocal() {
    }

    public MedidaLocal(String clave, String descripcionMedida) {
        this.clave = clave;
        this.descripcionMedida = descripcionMedida;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcionMedida() {
        return descripcionMedida;
    }

    public void setDescripcionMedida(String descripcionMedida) {
        this.descripcionMedida = descripcionMedida;
    }

}
