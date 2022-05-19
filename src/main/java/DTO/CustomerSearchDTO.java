/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;

/**
 *
 * @author wilmer
 */
public class CustomerSearchDTO implements Serializable {

    private String customerId;
    private String name;
    private String taxId;
    private String birthDate;
    private String customerType;

    public CustomerSearchDTO() {
    }

    public CustomerSearchDTO(String customerId, String name, String taxId, String birthDate, String customerType) {
        this.customerId = customerId;
        this.name = name;
        this.taxId = taxId;
        this.birthDate = birthDate;
        this.customerType = customerType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    @Override
    public String toString() {
        return "customerDTO{" + "customerId=" + customerId + ", name=" + name + ", taxId=" + taxId + ", birthDate=" + birthDate + ", customerType=" + customerType + '}';
    }

}
