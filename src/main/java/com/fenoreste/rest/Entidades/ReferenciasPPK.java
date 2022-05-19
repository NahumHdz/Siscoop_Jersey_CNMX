package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ReferenciasPPK implements Serializable {

    @Column(name = "idorigenpr")
    private int idorigenpr;

    @Column(name = "idproductor")
    private int idproductor;

    @Column(name = "idauxiliarr")
    private int idauxiliarr;

    @Column(name = "tiporeferencia")
    private int tiporeferencia;

    public ReferenciasPPK() {
    }

    public ReferenciasPPK(int idorigenpr, int idproductor, int idauxiliarr, int tiporeferencia) {
        this.idorigenpr = idorigenpr;
        this.idproductor = idproductor;
        this.idauxiliarr = idauxiliarr;
        this.tiporeferencia = tiporeferencia;
    }

    public int getIdorigenpr() {
        return this.idorigenpr;
    }

    public void setIdorigenpr(int idorigenpr) {
        this.idorigenpr = idorigenpr;
    }

    public int getIdproductor() {
        return this.idproductor;
    }

    public void setIdproductor(int idproductor) {
        this.idproductor = idproductor;
    }

    public int getIdauxiliarr() {
        return this.idauxiliarr;
    }

    public void setIdauxiliarr(int idauxiliarr) {
        this.idauxiliarr = idauxiliarr;
    }

    public int getTiporeferencia() {
        return this.tiporeferencia;
    }

    public void setTiporeferencia(int tiporeferencia) {
        this.tiporeferencia = tiporeferencia;
    }

    public int hashCode() {
        int hash = 0;
        hash += this.idorigenpr;
        hash += this.idproductor;
        hash += this.idauxiliarr;
        hash += this.tiporeferencia;
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof com.fenoreste.rest.Entidades.ReferenciasPPK)) {
            return false;
        }
        com.fenoreste.rest.Entidades.ReferenciasPPK other = (com.fenoreste.rest.Entidades.ReferenciasPPK) object;
        if (this.idorigenpr != other.idorigenpr) {
            return false;
        }
        if (this.idproductor != other.idproductor) {
            return false;
        }
        if (this.tiporeferencia != other.tiporeferencia) {
            return false;
        }
        return (this.idauxiliarr == other.idauxiliarr);
    }

    public String toString() {
        return "com.fenoreste.modelo.entidad.ReferenciaspPK[idorigenpr=" + this.idorigenpr + ", idproductor=" + this.idproductor + ", idauxiliarr=" + this.idauxiliarr + " ]";
    }

}
