/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author nahum
 */
public class AccountHoldersValidateDTO {

    private String name;
    private String relationCode;
    private String customerId;

    public AccountHoldersValidateDTO() {
    }

    public AccountHoldersValidateDTO(String name, String relationCode, String customerId) {
        this.name = name;
        this.relationCode = relationCode;
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "AccountHoldersValidateDTO{" + "name=" + name + ", relationCode=" + relationCode + ", customerId=" + customerId + '}';
    }

}
