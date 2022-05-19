package com.fenoreste.rest.Entidades;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Cacheable(true)
@Entity
@Table(name = "paises_siscoop")
public class Paises_Siscoop {

    @Id
    @Column(name = "idpais")
    private Integer idpais;

    @Column(name = "codigo")
    private String code;

    @Column(name = "nombre")
    private String name;

    public Paises_Siscoop() {
    }

    public Paises_Siscoop(Integer idpais, String code, String name) {
        this.idpais = idpais;
        this.code = code;
        this.name = name;
    }

    public Integer getIdpais() {
        return this.idpais;
    }

    public void setIdpais(Integer idpais) {
        this.idpais = idpais;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Paises_Siscoop{idpais=" + this.idpais + ", code=" + this.code + ", name=" + this.name + '}';
    }

}
