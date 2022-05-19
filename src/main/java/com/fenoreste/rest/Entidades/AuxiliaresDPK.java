package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class AuxiliaresDPK implements Serializable {

    @Column(name = "idorigenp")
    private int idorigenp;

    @Column(name = "idproducto")
    private int idproducto;

    @Column(name = "idauxiliar")
    private int idauxiliar;

    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public AuxiliaresDPK() {
    }

    public AuxiliaresDPK(int idorigenp, int idproducto, int idauxiliar, Date fecha) {
        this.idorigenp = idorigenp;
        this.idproducto = idproducto;
        this.idauxiliar = idauxiliar;
        this.fecha = fecha;
    }

    public AuxiliaresDPK(int idorigenp, int idproducto, int idauxiliar) {
        this.idorigenp = idorigenp;
        this.idproducto = idproducto;
        this.idauxiliar = idauxiliar;
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

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int hashCode() {
        int hash = 0;
        hash += this.idorigenp;
        hash += this.idproducto;
        hash += this.idauxiliar;
        hash += (this.fecha != null) ? this.fecha.hashCode() : 0;
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof com.fenoreste.rest.Entidades.AuxiliaresDPK)) {
            return false;
        }
        com.fenoreste.rest.Entidades.AuxiliaresDPK other = (com.fenoreste.rest.Entidades.AuxiliaresDPK) object;
        if (this.idorigenp != other.idorigenp) {
            return false;
        }
        if (this.idproducto != other.idproducto) {
            return false;
        }
        if (this.idauxiliar != other.idauxiliar) {
            return false;
        }
        return ((this.fecha != null || other.fecha == null) && (this.fecha == null || this.fecha.equals(other.fecha)));
    }

    public String toString() {
        return "com.fenoreste.modelo.entidad.AuxiliaresDPK[ idorigenp=" + this.idorigenp + ", idproducto=" + this.idproducto + ", idauxiliar=" + this.idauxiliar + ", fecha=" + this.fecha + " ]";
    }

}
