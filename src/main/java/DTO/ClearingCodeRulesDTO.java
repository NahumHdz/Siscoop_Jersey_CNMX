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
public class ClearingCodeRulesDTO {

    private String countryCode;
    private String charsetType;

    public ClearingCodeRulesDTO() {
    }

    public ClearingCodeRulesDTO(String countryCode, String charsetType) {
        this.countryCode = countryCode;
        this.charsetType = charsetType;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCharsetType() {
        return charsetType;
    }

    public void setCharsetType(String charsetType) {
        this.charsetType = charsetType;
    }

    @Override
    public String toString() {
        return "ClearingCodeRulesDTO{" + "countryCode=" + countryCode + ", charsetType=" + charsetType + '}';
    }

}
