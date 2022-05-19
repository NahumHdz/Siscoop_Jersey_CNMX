package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "e_transferenciassiscoop")
public class transferencias_completadas_siscoop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String customerId;

    private String tipotransferencia;

    private String cuentaorigen;

    private String cuentadestino;

    private Double monto;

    private String comentario1;

    private String comentario2;

    private Date fechaejecucion;

    private String tipoejecucion;

    private boolean estatus;

    private Double runningBalance;

    private String validationid;

    private Integer transaccionorigen;

    private Integer transacciondestino;

    public transferencias_completadas_siscoop() {
    }

    public transferencias_completadas_siscoop(String customerId, String tipotransferencia, String cuentaorigen, String cuentadestino, Double monto, String comentario1, String comentario2, Date fechaejecucion, String tipoejecucion, boolean estatus, Double runningBalance, String validationid, Integer transaccionorigen, Integer transacciondestino) {
        this.customerId = customerId;
        this.tipotransferencia = tipotransferencia;
        this.cuentaorigen = cuentaorigen;
        this.cuentadestino = cuentadestino;
        this.monto = monto;
        this.comentario1 = comentario1;
        this.comentario2 = comentario2;
        this.fechaejecucion = fechaejecucion;
        this.tipoejecucion = tipoejecucion;
        this.estatus = estatus;
        this.runningBalance = runningBalance;
        this.validationid = validationid;
        this.transaccionorigen = transaccionorigen;
        this.transacciondestino = transacciondestino;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTipotransferencia() {
        return tipotransferencia;
    }

    public void setTipotransferencia(String tipotransferencia) {
        this.tipotransferencia = tipotransferencia;
    }

    public String getCuentaorigen() {
        return cuentaorigen;
    }

    public void setCuentaorigen(String cuentaorigen) {
        this.cuentaorigen = cuentaorigen;
    }

    public String getCuentadestino() {
        return cuentadestino;
    }

    public void setCuentadestino(String cuentadestino) {
        this.cuentadestino = cuentadestino;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getComentario1() {
        return comentario1;
    }

    public void setComentario1(String comentario1) {
        this.comentario1 = comentario1;
    }

    public String getComentario2() {
        return comentario2;
    }

    public void setComentario2(String comentario2) {
        this.comentario2 = comentario2;
    }

    public Date getFechaejecucion() {
        return fechaejecucion;
    }

    public void setFechaejecucion(Date fechaejecucion) {
        this.fechaejecucion = fechaejecucion;
    }

    public String getTipoejecucion() {
        return tipoejecucion;
    }

    public void setTipoejecucion(String tipoejecucion) {
        this.tipoejecucion = tipoejecucion;
    }

    public boolean isEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public Double getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(Double runningBalance) {
        this.runningBalance = runningBalance;
    }

    public String getValidationid() {
        return validationid;
    }

    public void setValidationid(String validationid) {
        this.validationid = validationid;
    }

    public Integer getTransaccionorigen() {
        return transaccionorigen;
    }

    public void setTransaccionorigen(Integer transaccionorigen) {
        this.transaccionorigen = transaccionorigen;
    }

    public Integer getTransacciondestino() {
        return transacciondestino;
    }

    public void setTransacciondestino(Integer transacciondestino) {
        this.transacciondestino = transacciondestino;
    }

    @Override
    public String toString() {
        return "transferencias_completadas_siscoop{" + "customerId=" + customerId + ", tipotransferencia=" + tipotransferencia + ", cuentaorigen=" + cuentaorigen + ", cuentadestino=" + cuentadestino + ", monto=" + monto + ", comentario1=" + comentario1 + ", comentario2=" + comentario2 + ", fechaejecucion=" + fechaejecucion + ", tipoejecucion=" + tipoejecucion + ", estatus=" + estatus + ", runningBalance=" + runningBalance + ", validationid=" + validationid + ", transaccionorigen=" + transaccionorigen + ", transacciondestino=" + transacciondestino + '}';
    }

}
