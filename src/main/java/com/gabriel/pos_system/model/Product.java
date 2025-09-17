package com.gabriel.pos_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String imagen;
    @Column(unique = true)
    private String codigoDeBarra;
    private String marca;
    @NotEmpty
    private String descripcion;
    @NotNull
    private Integer stock;
    @NotNull
    private Double precioUnitario;
    @NotNull
    private Integer estado;
    private Double descuento;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "medida_local_id")
    private MedidaLocal medidaLocal;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "medida_sat_id")
    private MedidaSat medidaSat;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "clave_prod_serv_sat_id")
    private ClaveProdServSat claveProdServSat;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "objeto_imp_sat_id")
    private ObjetoImpSat objetoImpSat;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "impuesto_sat_id")
    private ImpuestoSat impuestoSat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public MedidaLocal getMedidaLocal() {
        return medidaLocal;
    }

    public void setMedidaLocal(MedidaLocal medidaLocal) {
        this.medidaLocal = medidaLocal;
    }

    public MedidaSat getMedidaSat() {
        return medidaSat;
    }

    public void setMedidaSat(MedidaSat medidaSat) {
        this.medidaSat = medidaSat;
    }

    public ClaveProdServSat getClaveProdServSat() {
        return claveProdServSat;
    }

    public void setClaveProdServSat(ClaveProdServSat claveProdServSat) {
        this.claveProdServSat = claveProdServSat;
    }

    public ObjetoImpSat getObjetoImpSat() {
        return objetoImpSat;
    }

    public void setObjetoImpSat(ObjetoImpSat objetoImpSat) {
        this.objetoImpSat = objetoImpSat;
    }

    public ImpuestoSat getImpuestoSat() {
        return impuestoSat;
    }

    public void setImpuestoSat(ImpuestoSat impuestoSat) {
        this.impuestoSat = impuestoSat;
    }

}
