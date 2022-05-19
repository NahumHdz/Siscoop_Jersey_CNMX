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
public class OrderWsSPEI {

    private String CIF;
    private String NombreSolicitante;
    private String RfcCurpSolicitate;
    private String ClabeSolicitante;
    private String CorreoElectronicoSolicitante;

    private String NombreBeneficiario;
    private int TipoCuentaBeneficiario;
    private String CuentaTarjetaBeneficiario;
    private String RfcCurpBeneficario;
    private int InstitucionContraparte;
    private String CorreoElectronicoBeneficiario;

    private Double monto;
    private Double IVA;
    private Double comision;
    private String conceptoPago;
    private Integer numeroReferencia;

    public OrderWsSPEI() {
    }

    public String getCIF() {
        return CIF;
    }

    public void setCIF(String CIF) {
        this.CIF = CIF;
    }

    public String getNombreSolicitante() {
        return NombreSolicitante;
    }

    public void setNombreSolicitante(String NombreSolicitante) {
        this.NombreSolicitante = NombreSolicitante;
    }

    public String getRfcCurpSolicitate() {
        return RfcCurpSolicitate;
    }

    public void setRfcCurpSolicitate(String RfcCurpSolicitate) {
        this.RfcCurpSolicitate = RfcCurpSolicitate;
    }

    public String getClabeSolicitante() {
        return ClabeSolicitante;
    }

    public void setClabeSolicitante(String ClabeSolicitante) {
        this.ClabeSolicitante = ClabeSolicitante;
    }

    public String getCorreoElectronicoSolicitante() {
        return CorreoElectronicoSolicitante;
    }

    public void setCorreoElectronicoSolicitante(String CorreoElectronicoSolicitante) {
        this.CorreoElectronicoSolicitante = CorreoElectronicoSolicitante;
    }

    public String getNombreBeneficiario() {
        return NombreBeneficiario;
    }

    public void setNombreBeneficiario(String NombreBeneficiario) {
        this.NombreBeneficiario = NombreBeneficiario;
    }

    public int getTipoCuentaBeneficiario() {
        return TipoCuentaBeneficiario;
    }

    public void setTipoCuentaBeneficiario(int TipoCuentaBeneficiario) {
        this.TipoCuentaBeneficiario = TipoCuentaBeneficiario;
    }

    public String getCuentaTarjetaBeneficiario() {
        return CuentaTarjetaBeneficiario;
    }

    public void setCuentaTarjetaBeneficiario(String CuentaTarjetaBeneficiario) {
        this.CuentaTarjetaBeneficiario = CuentaTarjetaBeneficiario;
    }

    public String getRfcCurpBeneficario() {
        return RfcCurpBeneficario;
    }

    public void setRfcCurpBeneficario(String RfcCurpBeneficario) {
        this.RfcCurpBeneficario = RfcCurpBeneficario;
    }

    public int getInstitucionContraparte() {
        return InstitucionContraparte;
    }

    public void setInstitucionContraparte(int InstitucionContraparte) {
        this.InstitucionContraparte = InstitucionContraparte;
    }

    public String getCorreoElectronicoBeneficiario() {
        return CorreoElectronicoBeneficiario;
    }

    public void setCorreoElectronicoBeneficiario(String CorreoElectronicoBeneficiario) {
        this.CorreoElectronicoBeneficiario = CorreoElectronicoBeneficiario;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double getIVA() {
        return IVA;
    }

    public void setIVA(Double IVA) {
        this.IVA = IVA;
    }

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }

    public String getConceptoPago() {
        return conceptoPago;
    }

    public void setConceptoPago(String conceptoPago) {
        this.conceptoPago = conceptoPago;
    }

    public Integer getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(Integer numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

}
