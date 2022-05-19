/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author wilmer
 */
@Entity
@Table(name = "bankingly_movimientos_ca")
public class Procesa_pago_movimientos implements Serializable {

    @EmbeddedId
    protected AuxiliaresPK auxiliaresPK;
    @Column(name = "fecha")
    private Timestamp fecha;
    @Column(name = "idusuario")
    private Integer idusuario;
    @Column(name = "sesion")
    private String sesion;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "idorigen")
    private Integer idorigen;
    @Column(name = "idgrupo")
    private Integer idgrupo;
    @Column(name = "idsocio")
    private Integer idsocio;
    @Column(name = "cargoabono")
    private Integer cargoabono;
    @Column(name = "monto")
    private Double monto;
    @Column(name = "iva")
    private Double iva;
    @Column(name = "tipo_amort")
    private Integer tipo_amort;
    @Column(name = "sai_aux")
    private String sai_aux;

    public Procesa_pago_movimientos() {
    }

    public Procesa_pago_movimientos(AuxiliaresPK auxiliaresPK, Timestamp fecha, Integer idusuario, String sesion, String referencia, Integer idorigen, Integer idgrupo, Integer idsocio, Integer cargoabono, Double monto, Double iva, Integer tipo_amort, String sai_aux) {
        this.auxiliaresPK = auxiliaresPK;
        this.fecha = fecha;
        this.idusuario = idusuario;
        this.sesion = sesion;
        this.referencia = referencia;
        this.idorigen = idorigen;
        this.idgrupo = idgrupo;
        this.idsocio = idsocio;
        this.cargoabono = cargoabono;
        this.monto = monto;
        this.iva = iva;
        this.tipo_amort = tipo_amort;
        this.sai_aux = sai_aux;
    }

    public AuxiliaresPK getAuxiliaresPK() {
        return auxiliaresPK;
    }

    public void setAuxiliaresPK(AuxiliaresPK auxiliaresPK) {
        this.auxiliaresPK = auxiliaresPK;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getIdorigen() {
        return idorigen;
    }

    public void setIdorigen(Integer idorigen) {
        this.idorigen = idorigen;
    }

    public Integer getIdgrupo() {
        return idgrupo;
    }

    public void setIdgrupo(Integer idgrupo) {
        this.idgrupo = idgrupo;
    }

    public Integer getIdsocio() {
        return idsocio;
    }

    public void setIdsocio(Integer idsocio) {
        this.idsocio = idsocio;
    }

    public Integer getCargoabono() {
        return cargoabono;
    }

    public void setCargoabono(Integer cargoabono) {
        this.cargoabono = cargoabono;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Integer getTipo_amort() {
        return tipo_amort;
    }

    public void setTipo_amort(Integer tipo_amort) {
        this.tipo_amort = tipo_amort;
    }

    public String getSai_aux() {
        return sai_aux;
    }

    public void setSai_aux(String sai_aux) {
        this.sai_aux = sai_aux;
    }

    @Override
    public String toString() {
        return "Procesa_pago_movimientos{" + "auxiliaresPK=" + auxiliaresPK + ", fecha=" + fecha + ", idusuario=" + idusuario + ", sesion=" + sesion + ", referencia=" + referencia + ", idorigen=" + idorigen + ", idgrupo=" + idgrupo + ", idsocio=" + idsocio + ", cargoabono=" + cargoabono + ", monto=" + monto + ", iva=" + iva + ", tipo_amort=" + tipo_amort + ", sai_aux=" + sai_aux + '}';
    }

    private static final long serialVersionUID = 1L;

}
