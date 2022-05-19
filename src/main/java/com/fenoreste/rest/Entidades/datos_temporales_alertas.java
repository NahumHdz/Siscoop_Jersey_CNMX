/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "datos_temporales_alertas")
public class datos_temporales_alertas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Integer id;
    @Column(name = "alertcode")
    private String alertCode;
    @Column(name = "accountid")
    private String accountId;
    @Column(name = "customerid")
    private String customerId;
    @Column(name = "monto")
    private Double monto;
    @Column(name = "producttype")
    private String accountType;

    public datos_temporales_alertas() {

    }

    public datos_temporales_alertas(Integer id, String alertCode, String accountId, String customerId, Double monto, String accountType) {
        this.id = id;
        this.alertCode = alertCode;
        this.accountId = accountId;
        this.customerId = customerId;
        this.monto = monto;
        this.accountType = accountType;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "datos_temporales_alertas{" + "id=" + id + ", alertCode=" + alertCode + ", accountId=" + accountId + ", customerId=" + customerId + ", monto=" + monto + ", accountType=" + accountType + '}';
    }

}
