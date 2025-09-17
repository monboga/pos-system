package com.gabriel.pos_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "c_impuesto_sat")
public class ImpuestoSat {

    @Id
    @Column(name = "c_impuesto")
    private String cImpuesto;
    private String descripcion;
    private boolean retencion;
    private boolean traslado;
    private String localOFederal;

    public ImpuestoSat() {
    }

    public ImpuestoSat(String cImpuesto, String descripcion, boolean retencion, boolean traslado,
            String localOFederal) {
        this.cImpuesto = cImpuesto;
        this.descripcion = descripcion;
        this.retencion = retencion;
        this.traslado = traslado;
        this.localOFederal = localOFederal;
    }

    public String getcImpuesto() {
        return cImpuesto;
    }

    public void setcImpuesto(String cImpuesto) {
        this.cImpuesto = cImpuesto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isRetencion() {
        return retencion;
    }

    public void setRetencion(boolean retencion) {
        this.retencion = retencion;
    }

    public boolean isTraslado() {
        return traslado;
    }

    public void setTraslado(boolean traslado) {
        this.traslado = traslado;
    }

    public String getLocalOFederal() {
        return localOFederal;
    }

    public void setLocalOFederal(String localOFederal) {
        this.localOFederal = localOFederal;
    }

}
