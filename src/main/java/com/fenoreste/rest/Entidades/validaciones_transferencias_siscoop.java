package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "v_transferenciassiscoop")
@Cacheable(false)
public class validaciones_transferencias_siscoop implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_validaciones_tsiscoop")
  @SequenceGenerator(name = "sec_validaciones_tsiscoop", sequenceName = "sec_validaciones_tsiscoop")
  private Integer id;
  
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
  
  private String validationId;
  
  public validaciones_transferencias_siscoop() {}
  
  public validaciones_transferencias_siscoop(Integer id, String customerId, String tipotransferencia, String cuentaorigen, String cuentadestino, Double monto, String comentario1, String comentario2, Date fechaejecucion, String tipoejecucion, boolean estatus, String validationId) {
    this.id = id;
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
  
  public Integer getId() {
    return this.id;
  }
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public String getCustomerId() {
    return this.customerId;
  }
  
  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
  
  public String getTipotransferencia() {
    return this.tipotransferencia;
  }
  
  public void setTipotransferencia(String tipotransferencia) {
    this.tipotransferencia = tipotransferencia;
  }
  
  public String getCuentaorigen() {
    return this.cuentaorigen;
  }
  
  public void setCuentaorigen(String cuentaorigen) {
    this.cuentaorigen = cuentaorigen;
  }
  
  public String getCuentadestino() {
    return this.cuentadestino;
  }
  
  public void setCuentadestino(String cuentadestino) {
    this.cuentadestino = cuentadestino;
  }
  
  public Double getMonto() {
    return this.monto;
  }
  
  public void setMonto(Double monto) {
    this.monto = monto;
  }
  
  public String getComentario1() {
    return this.comentario1;
  }
  
  public void setComentario1(String comentario1) {
    this.comentario1 = comentario1;
  }
  
  public String getComentario2() {
    return this.comentario2;
  }
  
  public void setComentario2(String comentario2) {
    this.comentario2 = comentario2;
  }
  
  public Date getFechaejecucion() {
    return this.fechaejecucion;
  }
  
  public void setFechaejecucion(Date fechaejecucion) {
    this.fechaejecucion = fechaejecucion;
  }
  
  public String getTipoejecucion() {
    return this.tipoejecucion;
  }
  
  public void setTipoejecucion(String tipoejecucion) {
    this.tipoejecucion = tipoejecucion;
  }
  
  public boolean isEstatus() {
    return this.estatus;
  }
  
  public void setEstatus(boolean estatus) {
    this.estatus = estatus;
  }
  
  public String getValidationId() {
    return this.validationId;
  }
  
  public void setValidationId(String validationId) {
    this.validationId = validationId;
  }
  
  public String toString() {
    return "validaciones_transferencias_siscoop{id=" + this.id + ", customerId=" + this.customerId + ", tipotransferencia=" + this.tipotransferencia + ", cuentaorigen=" + this.cuentaorigen + ", cuentadestino=" + this.cuentadestino + ", monto=" + this.monto + ", comentario1=" + this.comentario1 + ", comentario2=" + this.comentario2 + ", fechaejecucion=" + this.fechaejecucion + ", tipoejecucion=" + this.tipoejecucion + ", estatus=" + this.estatus + ", validationId=" + this.validationId + '}';
  }
}