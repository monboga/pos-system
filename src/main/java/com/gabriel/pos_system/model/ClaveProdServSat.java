package com.gabriel.pos_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "c_clave_prod_serv_sat")
public class ClaveProdServSat {

    @Id
    @Column(name = "c_clave_prod_serv")
    private String cClaveProdServ;
    private String descripcion;

    public ClaveProdServSat() {
    }

    public ClaveProdServSat(String cClaveProdServ, String descripcion) {
        this.cClaveProdServ = cClaveProdServ;
        this.descripcion = descripcion;
    }

    public String getcClaveProdServ() {
        return cClaveProdServ;
    }

    public void setcClaveProdServ(String cClaveProdServ) {
        this.cClaveProdServ = cClaveProdServ;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
