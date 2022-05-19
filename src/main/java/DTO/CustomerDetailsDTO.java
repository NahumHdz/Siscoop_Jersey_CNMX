/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author wilmer
 */
public class CustomerDetailsDTO {

    private String nationalId;
    private String birthDate;
    private String customerId;
    private String name;
    private String customerType;
    private String taxId;

    public CustomerDetailsDTO() {
    }

    public CustomerDetailsDTO(String nationalId, String birthDate, String customerId, String name, String customerType, String taxId) {
        this.nationalId = nationalId;
        this.birthDate = birthDate;
        this.customerId = customerId;
        this.name = name;
        this.customerType = customerType;
        this.taxId = taxId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    @Override
    public String toString() {
        return "CustomerDetailsDTO{" + "nationalId=" + nationalId + ", birthDate=" + birthDate + ", customerId=" + customerId + ", name=" + name + ", customerType=" + customerType + ", taxId=" + taxId + '}';
    }

}
