package com.gabriel.pos_system.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "business")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El RFC no puede estar vacio")
    @Size(max = 100, message = "El RFC no puede tener mas de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String rfc;

    @NotEmpty(message = "La razon social no puede estar vacia")
    @Size(max = 100, message = "La razon social no puede tener mas de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String razonSocial;

    @NotEmpty(message = "El correo no puede estar vacio")
    @Email(message = "El formato del correo no es valido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty
    private String address;

    @NotEmpty(message = "El telefono no puede estar vacio")
    @Pattern(regexp = "^\\d{10}$", message = "El telefono debe contener exactamente 10 digitos")
    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @NotNull(message = "El codigo postal no puede estar vacio")
    @Column(nullable = false)
    private Integer postalCode;

    @NotEmpty
    private String currencyType;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String logo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "business_regimenes", joinColumns = @JoinColumn(name = "business_id"), inverseJoinColumns = @JoinColumn(name = "regimen_fiscal_id"))
    private Set<RegimenFiscal> regimenesFiscales = new HashSet<>();

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

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<RegimenFiscal> getRegimenesFiscales() {
        return regimenesFiscales;
    }

    public void setRegimenesFiscales(Set<RegimenFiscal> regimenesFiscales) {
        this.regimenesFiscales = regimenesFiscales;
    }

}
