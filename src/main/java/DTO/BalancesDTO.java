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
public class BalancesDTO {

    private String accountId;
    private Double ledger;
    private Double avalaible;

    public BalancesDTO() {
    }

    public BalancesDTO(String accountId, Double ledger, Double avalaible) {
        this.accountId = accountId;
        this.ledger = ledger;
        this.avalaible = avalaible;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Double getLedger() {
        return ledger;
    }

    public void setLedger(Double ledger) {
        this.ledger = ledger;
    }

    public Double getAvalaible() {
        return avalaible;
    }

    public void setAvalaible(Double avalaible) {
        this.avalaible = avalaible;
    }

    @Override
    public String toString() {
        return "BalancesDTO{" + "accountId=" + accountId + ", ledger=" + ledger + ", avalaible=" + avalaible + '}';
    }

}
