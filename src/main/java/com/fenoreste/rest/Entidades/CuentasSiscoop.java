package com.fenoreste.rest.Entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tipos_cuenta_siscoop")
public class CuentasSiscoop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproducto")
    private Integer idproducto;

    @Column(name = "producttypeid")
    private Integer producttypeid;

    @Column(name = "producttypename")
    private String producttypename;

    @Column(name = "descripcion")
    private String descripcion;

    public CuentasSiscoop() {
    }

    public CuentasSiscoop(Integer idproducto, Integer producttypeid, String producttypename, String descripcion) {
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
        return "CuentasSiscoop{idproducto=" + this.idproducto + ", producttypeid=" + this.producttypeid + ", producttypename=" + this.producttypename + ", descripcion=" + this.descripcion + '}';
    }

}
