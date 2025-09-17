package com.gabriel.pos_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "c_objeto_imp_sat")
public class ObjetoImpSat {

    @Id
    @Column(name = "c_objeto_imp")
    private String cObjetoImp;
    private String descripcion;

    public ObjetoImpSat() {
    }

    public ObjetoImpSat(String cObjetoImp, String descripcion) {
        this.cObjetoImp = cObjetoImp;
        this.descripcion = descripcion;
    }

    public String getcObjetoImp() {
        return cObjetoImp;
    }

    public void setcObjetoImp(String cObjetoImp) {
        this.cObjetoImp = cObjetoImp;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
