package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "v_transferenciassiscoop")
@Cacheable(false)
public class validaciones_transferencias_siscoop implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Id
    private String validationId;

    public validaciones_transferencias_siscoop() {
    }

    public validaciones_transferencias_siscoop(String customerId, String tipotransferencia, String cuentaorigen, String cuentadestino, Double monto, String comentario1, String comentario2, Date fechaejecucion, String tipoejecucion, boolean estatus, String validationId) {
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
        this.validationId = validationId;
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

    public String getValidationId() {
        return validationId;
    }

    public void setValidationId(String validationId) {
        this.validationId = validationId;
    }

    @Override
    public String toString() {
        return "validaciones_transferencias_siscoop{" + "customerId=" + customerId + ", tipotransferencia=" + tipotransferencia + ", cuentaorigen=" + cuentaorigen + ", cuentadestino=" + cuentadestino + ", monto=" + monto + ", comentario1=" + comentario1 + ", comentario2=" + comentario2 + ", fechaejecucion=" + fechaejecucion + ", tipoejecucion=" + tipoejecucion + ", estatus=" + estatus + ", validationId=" + validationId + '}';
    }

}
