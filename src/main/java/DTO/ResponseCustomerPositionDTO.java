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
public class ResponseCustomerPositionDTO {

    private String customerId;
    private Double avalaible;
    private Double ledger;

    public ResponseCustomerPositionDTO() {
    }

    public ResponseCustomerPositionDTO(String customerId, Double avalaible, Double ledger) {
        this.customerId = customerId;
        this.avalaible = avalaible;
        this.ledger = ledger;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getAvalaible() {
        return avalaible;
    }

    public void setAvalaible(Double avalaible) {
        this.avalaible = avalaible;
    }

    public Double getLedger() {
        return ledger;
    }

    public void setLedger(Double ledger) {
        this.ledger = ledger;
    }

    @Override
    public String toString() {
        return "ResponseCustomerPositionDTO{" + "customerId=" + customerId + ", avalaible=" + avalaible + ", ledger=" + ledger + '}';
    }

}
