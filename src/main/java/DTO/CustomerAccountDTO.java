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
public class CustomerAccountDTO {

    private String accountId;
    private String accountNumber;
    private String displayAccountNumber;
    private String accountTye;
    private String currencyCode;
    private String productCode;
    private String status;
    private Object[] restrictions;
    private Object[] customerRelations;

    public CustomerAccountDTO() {
    }

    public CustomerAccountDTO(String accountId, String accountNumber, String displayAccountNumber, String accountTye, String currencyCode, String productCode, String status, Object[] restrictions, Object[] customerRelations) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.displayAccountNumber = displayAccountNumber;
        this.accountTye = accountTye;
        this.currencyCode = currencyCode;
        this.productCode = productCode;
        this.status = status;
        this.restrictions = restrictions;
        this.customerRelations = customerRelations;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDisplayAccountNumber() {
        return displayAccountNumber;
    }

    public void setDisplayAccountNumber(String displayAccountNumber) {
        this.displayAccountNumber = displayAccountNumber;
    }

    public String getAccountTye() {
        return accountTye;
    }

    public void setAccountTye(String accountTye) {
        this.accountTye = accountTye;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object[] getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Object[] restrictions) {
        this.restrictions = restrictions;
    }

    public Object[] getCustomerRelations() {
        return customerRelations;
    }

    public void setCustomerRelations(Object[] customerRelations) {
        this.customerRelations = customerRelations;
    }

    @Override
    public String toString() {
        return "CustomerAccountDTO{" + "accountId=" + accountId + ", accountNumber=" + accountNumber + ", displayAccountNumber=" + displayAccountNumber + ", accountTye=" + accountTye + ", currencyCode=" + currencyCode + ", productCode=" + productCode + ", status=" + status + ", restrictions=" + restrictions + ", customerRelations=" + customerRelations + '}';
    }

}
