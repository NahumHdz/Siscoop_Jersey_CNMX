/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Elliot
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
@Entity
@Table(name = "e_alertas")
public class e_Alerts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_validaciones_tsiscoop")
    @SequenceGenerator(name = "sec_validaciones_tsiscoop", sequenceName = "sec_validaciones_tsiscoop")
    private Integer id;
    @Column(name = "alertcode")
    private String alertCode;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "accountid")
    private String accountId;
    @Column(name = "customerid")
    private String customerid;
    @Column(name = "property")
    private String property;
    @Column(name = "monto")
    private Double monto;
    @Column(name = "operator")
    private String operator;
    @Column(name = "ruletype")
    private String ruleType;
    @Column(name = "fechaejecucion")
    private Date fechaejecucion;
    @Column(name = "alert_enviado")
    private boolean alert_enviado;

    public e_Alerts() {

    }

    public e_Alerts(Integer id, String alertCode, boolean enabled, String accountId, String customerid, String property, Double monto, String operator, String ruleType, Date fechaejecucion, boolean alert_enviado) {
        this.id = id;
        this.alertCode = alertCode;
        this.enabled = enabled;
        this.accountId = accountId;
        this.customerid = customerid;
        this.property = property;
        this.monto = monto;
        this.operator = operator;
        this.ruleType = ruleType;
        this.fechaejecucion = fechaejecucion;
        this.alert_enviado = alert_enviado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlertCode() {
        return alertCode;
    }

    public void setAlertCode(String alertCode) {
        this.alertCode = alertCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public Date getFechaejecucion() {
        return fechaejecucion;
    }

    public void setFechaejecucion(Date fechaejecucion) {
        this.fechaejecucion = fechaejecucion;
    }

    public boolean isAlert_enviado() {
        return alert_enviado;
    }

    public void setAlert_enviado(boolean alert_enviado) {
        this.alert_enviado = alert_enviado;
    }

    @Override
    public String toString() {
        return "e_Alerts{" + "id=" + id + ", alertCode=" + alertCode + ", enabled=" + enabled + ", accountId=" + accountId + ", customerid=" + customerid + ", property=" + property + ", monto=" + monto + ", operator=" + operator + ", ruleType=" + ruleType + ", fechaejecucion=" + fechaejecucion + ", alert_enviado=" + alert_enviado + '}';
    }

}
