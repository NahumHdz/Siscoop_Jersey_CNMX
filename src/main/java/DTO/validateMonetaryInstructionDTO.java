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
public class validateMonetaryInstructionDTO {

    private String validationId;
    private String[] fees;
    private String executionDate;

    public validateMonetaryInstructionDTO() {
    }

    public validateMonetaryInstructionDTO(String validationId, String[] fees, String executionDate) {
        this.validationId = validationId;
        this.fees = fees;
        this.executionDate = executionDate;
    }

    public String getValidationId() {
        return validationId;
    }

    public void setValidationId(String validationId) {
        this.validationId = validationId;
    }

    public String[] getFees() {
        return fees;
    }

    public void setFees(String[] fees) {
        this.fees = fees;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    @Override
    public String toString() {
        return "validateMonetaryInstructionDTO{" + "validationId=" + validationId + ", fees=" + fees + ", executionDate=" + executionDate + '}';
    }

}
