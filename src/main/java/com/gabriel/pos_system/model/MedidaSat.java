package com.gabriel.pos_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "c_medida_sat")
public class MedidaSat {
    @Id
    private String clave;
    private String nombre;
    private String tipo;

    public MedidaSat() {
    }

    public MedidaSat(String clave, String nombre, String tipo) {
        this.clave = clave;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
