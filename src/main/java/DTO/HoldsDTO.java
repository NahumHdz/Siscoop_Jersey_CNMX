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
public class HoldsDTO {

    private String holdId;
    private Double amount;
    private String entryDate;
    private String descritpion;

    public HoldsDTO() {
    }

    public HoldsDTO(String holdId, Double amount, String entryDate, String descritpion) {
        this.holdId = holdId;
        this.amount = amount;
        this.entryDate = entryDate;
        this.descritpion = descritpion;
    }

    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getDescritpion() {
        return descritpion;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }

    @Override
    public String toString() {
        return "HoldsDTO{" + "holdId=" + holdId + ", amount=" + amount + ", entryDate=" + entryDate + ", descritpion=" + descritpion + '}';
    }

}
