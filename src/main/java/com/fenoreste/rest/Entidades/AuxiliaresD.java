package com.fenoreste.rest.Entidades;

import com.fenoreste.rest.Entidades.AuxiliaresDPK;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Cacheable(false)
@Entity
@Table(name = "auxiliares_d")
public class AuxiliaresD implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected AuxiliaresDPK auxiliaresDPK;

    @Column(name = "cargoabono")
    private Short cargoabono;

    @Column(name = "monto")
    private BigDecimal monto;

    @Column(name = "montoio")
    private BigDecimal montoio;

    @Column(name = "montoim")
    private BigDecimal montoim;

    @Column(name = "montoiva")
    private BigDecimal montoiva;

    @Column(name = "idorigenc")
    private Integer idorigenc;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "idtipo")
    private Short idtipo;

    @Column(name = "idpoliza")
    private Integer idpoliza;

    @Column(name = "tipomov")
    private Short tipomov;

    @Column(name = "saldoec")
    private BigDecimal saldoec;

    @Column(name = "transaccion")
    private Integer transaccion;

    @Column(name = "montoivaim")
    private BigDecimal montoivaim;

    @Column(name = "efectivo")
    private BigDecimal efectivo;

    @Column(name = "diasvencidos")
    private int diasvencidos;

    @Column(name = "montovencido")
    private BigDecimal montovencido;

    @Column(name = "ticket")
    private Integer ticket;

    @Column(name = "montoidnc")
    private BigDecimal montoidnc;

    @Column(name = "montoieco")
    private BigDecimal montoieco;

    @Column(name = "montoidncm")
    private BigDecimal montoidncm;

    @Column(name = "montoiecom")
    private BigDecimal montoiecom;

    public AuxiliaresD() {
    }

    public AuxiliaresD(AuxiliaresDPK auxiliaresDPK) {
        this.auxiliaresDPK = auxiliaresDPK;
    }

    public AuxiliaresD(int idorigenp, int idproducto, int idauxiliar, Date fecha) {
        this.auxiliaresDPK = new AuxiliaresDPK(idorigenp, idproducto, idauxiliar, fecha);
    }

    public AuxiliaresDPK getAuxiliaresDPK() {
        return this.auxiliaresDPK;
    }

    public void setAuxiliaresDPK(AuxiliaresDPK auxiliaresDPK) {
        this.auxiliaresDPK = auxiliaresDPK;
    }

    public Short getCargoabono() {
        return this.cargoabono;
    }

    public void setCargoabono(Short cargoabono) {
        this.cargoabono = cargoabono;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getMontoio() {
        return this.montoio;
    }

    public void setMontoio(BigDecimal montoio) {
        this.montoio = montoio;
    }

    public BigDecimal getMontoim() {
        return this.montoim;
    }

    public void setMontoim(BigDecimal montoim) {
        this.montoim = montoim;
    }

    public BigDecimal getMontoiva() {
        return this.montoiva;
    }

    public void setMontoiva(BigDecimal montoiva) {
        this.montoiva = montoiva;
    }

    public Integer getIdorigenc() {
        return this.idorigenc;
    }

    public void setIdorigenc(Integer idorigenc) {
        this.idorigenc = idorigenc;
    }

    public String getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Short getIdtipo() {
        return this.idtipo;
    }

    public void setIdtipo(Short idtipo) {
        this.idtipo = idtipo;
    }

    public Integer getIdpoliza() {
        return this.idpoliza;
    }

    public void setIdpoliza(Integer idpoliza) {
        this.idpoliza = idpoliza;
    }

    public Short getTipomov() {
        return this.tipomov;
    }

    public void setTipomov(Short tipomov) {
        this.tipomov = tipomov;
    }

    public BigDecimal getSaldoec() {
        return this.saldoec;
    }

    public void setSaldoec(BigDecimal saldoec) {
        this.saldoec = saldoec;
    }

    public Integer getTransaccion() {
        return this.transaccion;
    }

    public void setTransaccion(Integer transaccion) {
        this.transaccion = transaccion;
    }

    public BigDecimal getMontoivaim() {
        return this.montoivaim;
    }

    public void setMontoivaim(BigDecimal montoivaim) {
        this.montoivaim = montoivaim;
    }

    public BigDecimal getEfectivo() {
        return this.efectivo;
    }

    public void setEfectivo(BigDecimal efectivo) {
        this.efectivo = efectivo;
    }

    public int getDiasvencidos() {
        return this.diasvencidos;
    }

    public void setDiasvencidos(int diasvencidos) {
        this.diasvencidos = diasvencidos;
    }

    public BigDecimal getMontovencido() {
        return this.montovencido;
    }

    public void setMontovencido(BigDecimal montovencido) {
        this.montovencido = montovencido;
    }

    public Integer getTicket() {
        return this.ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public BigDecimal getMontoidnc() {
        return this.montoidnc;
    }

    public void setMontoidnc(BigDecimal montoidnc) {
        this.montoidnc = montoidnc;
    }

    public BigDecimal getMontoieco() {
        return this.montoieco;
    }

    public void setMontoieco(BigDecimal montoieco) {
        this.montoieco = montoieco;
    }

    public BigDecimal getMontoidncm() {
        return this.montoidncm;
    }

    public void setMontoidncm(BigDecimal montoidncm) {
        this.montoidncm = montoidncm;
    }

    public BigDecimal getMontoiecom() {
        return this.montoiecom;
    }

    public void setMontoiecom(BigDecimal montoiecom) {
        this.montoiecom = montoiecom;
    }

    public int hashCode() {
        int hash = 0;
        hash += (this.auxiliaresDPK != null) ? this.auxiliaresDPK.hashCode() : 0;
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof com.fenoreste.rest.Entidades.AuxiliaresD)) {
            return false;
        }
        com.fenoreste.rest.Entidades.AuxiliaresD other = (com.fenoreste.rest.Entidades.AuxiliaresD) object;
        return ((this.auxiliaresDPK != null || other.auxiliaresDPK == null) && (this.auxiliaresDPK == null || this.auxiliaresDPK.equals(other.auxiliaresDPK)));
    }

    public String toString() {
        return "com.fenoreste.modelo.entidad.AuxiliaresD[ auxiliaresDPK=" + this.auxiliaresDPK + " ]";
    }

}
