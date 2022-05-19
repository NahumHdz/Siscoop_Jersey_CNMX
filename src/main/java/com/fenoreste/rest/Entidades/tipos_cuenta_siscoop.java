package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tipos_cuenta_siscoop")
public class tipos_cuenta_siscoop implements Serializable {

    @Id
    @Column(name = "idproducto")
    private Integer idproducto;

    private Integer producttypeid;

    private String producttypename;

    private String descripcion;

    public tipos_cuenta_siscoop() {
    }

    public tipos_cuenta_siscoop(Integer idproducto, Integer producttypeid, String producttypename, String descripcion) {
        this.idproducto = idproducto;
        this.producttypeid = producttypeid;
        this.producttypename = producttypename;
        this.descripcion = descripcion;
    }

    public Integer getIdproducto() {
        return this.idproducto;
    }

    public void setIdproducto(Integer idproducto) {
        this.idproducto = idproducto;
    }

    public Integer getProducttypeid() {
        return this.producttypeid;
    }

    public void setProducttypeid(Integer producttypeid) {
        this.producttypeid = producttypeid;
    }

    public String getProducttypename() {
        return this.producttypename;
    }

    public void setProducttypename(String producttypename) {
        this.producttypename = producttypename;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String toString() {
        return "tipos_cuenta_siscoop{idproducto=" + this.idproducto + ", producttypeid=" + this.producttypeid + ", producttypename=" + this.producttypename + ", descripcion=" + this.descripcion + '}';
    }

}
