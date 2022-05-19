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
public class AccountHoldersDTO {

    private String name;
    private String relationCode;

    public AccountHoldersDTO() {
    }

    public AccountHoldersDTO(String name, String relationCode) {
        this.name = name;
        this.relationCode = relationCode;
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

    @Override
    public String toString() {
        return "AccountHoldersDTO{" + "name=" + name + ", relationCode=" + relationCode + '}';
    }

}
