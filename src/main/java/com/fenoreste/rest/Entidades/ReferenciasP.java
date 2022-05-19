package com.fenoreste.rest.Entidades;

import com.fenoreste.rest.Entidades.ReferenciasPPK;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "referenciasp")
public class ReferenciasP implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected ReferenciasPPK referenciasPPK;

    @Column(name = "idorigenp")
    private int idorigenp;

    @Column(name = "idproducto")
    private int idproducto;

    @Column(name = "idauxiliar")
    private int idauxiliar;

    @Column(name = "referencia")
    private String referencia;

    public ReferenciasP() {
    }

    public ReferenciasP(ReferenciasPPK referenciasPPK, int idorigenp, int idproducto, int idauxiliar, String referencia) {
        this.referenciasPPK = referenciasPPK;
        this.idorigenp = idorigenp;
        this.idproducto = idproducto;
        this.idauxiliar = idauxiliar;
        this.referencia = referencia;
    }

    public ReferenciasP(ReferenciasPPK referenciasPPK) {
        this.referenciasPPK = referenciasPPK;
    }

    public ReferenciasPPK getReferenciasPPK() {
        return this.referenciasPPK;
    }

    public void setReferenciasPPK(ReferenciasPPK referenciasPPK) {
        this.referenciasPPK = referenciasPPK;
    }

    public int getIdorigenp() {
        return this.idorigenp;
    }

    public void setIdorigenp(int idorigenp) {
        this.idorigenp = idorigenp;
    }

    public int getIdproducto() {
        return this.idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getIdauxiliar() {
        return this.idauxiliar;
    }

    public void setIdauxiliar(int idauxiliar) {
        this.idauxiliar = idauxiliar;
    }

    public String getReferencia() {
        return this.referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String toString() {
        return "ReferenciasP{referenciasPPK=" + this.referenciasPPK + ", idorigenp=" + this.idorigenp + ", idproducto=" + this.idproducto + ", idauxiliar=" + this.idauxiliar + ", referencia=" + this.referencia + '}';
    }

}
