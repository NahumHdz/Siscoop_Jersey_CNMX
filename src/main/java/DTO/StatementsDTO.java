/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author Elliot
 */
public class StatementsDTO {

    private String statementId;
    private String initialDate;
    private String finalDate;
    private String displayAccountNumber;
    private String idpdf;

    public StatementsDTO() {
    }

    public StatementsDTO(String statementId, String initialDate, String finalDate, String displayAccountNumber, String idpdf) {
        this.statementId = statementId;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.displayAccountNumber = displayAccountNumber;
        this.idpdf = idpdf;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public String getDisplayAccountNumber() {
        return displayAccountNumber;
    }

    public void setDisplayAccountNumber(String displayAccountNumber) {
        this.displayAccountNumber = displayAccountNumber;
    }

    public String getIdpdf() {
        return idpdf;
    }

    public void setIdpdf(String idpdf) {
        this.idpdf = idpdf;
    }

    @Override
    public String toString() {
        return "StatementsDTO{" + "statementId=" + statementId + ", initialDate=" + initialDate + ", finalDate=" + finalDate + ", displayAccountNumber=" + displayAccountNumber + ", idpdf=" + idpdf + '}';
    }

}
