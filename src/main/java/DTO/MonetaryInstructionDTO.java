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
public class MonetaryInstructionDTO {

    private String debitAccount;
    private String creditAccount;
    private Double monto;
    private String typeNameId;
    private String executionDate;
    private String originatorTransactionType;
    private int monetaryId;

    public MonetaryInstructionDTO() {
    }

    public MonetaryInstructionDTO(String debitAccount, String creditAccount, Double monto, String typeNameId, String executionDate, String originatorTransactionType, int monetaryId) {
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.monto = monto;
        this.typeNameId = typeNameId;
        this.executionDate = executionDate;
        this.originatorTransactionType = originatorTransactionType;
        this.monetaryId = monetaryId;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getTypeNameId() {
        return typeNameId;
    }

    public void setTypeNameId(String typeNameId) {
        this.typeNameId = typeNameId;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getOriginatorTransactionType() {
        return originatorTransactionType;
    }

    public void setOriginatorTransactionType(String originatorTransactionType) {
        this.originatorTransactionType = originatorTransactionType;
    }

    public int getMonetaryId() {
        return monetaryId;
    }

    public void setMonetaryId(int monetaryId) {
        this.monetaryId = monetaryId;
    }

    @Override
    public String toString() {
        return "MonetaryInstructionDTO{" + "debitAccount=" + debitAccount + ", creditAccount=" + creditAccount + ", monto=" + monto + ", typeNameId=" + typeNameId + ", executionDate=" + executionDate + ", originatorTransactionType=" + originatorTransactionType + ", monetaryId=" + monetaryId + '}';
    }

}
