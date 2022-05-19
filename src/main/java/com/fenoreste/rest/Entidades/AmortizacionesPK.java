package com.fenoreste.rest.Entidades;

/**
 *
 * @author Elliot
 */
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AmortizacionesPK implements Serializable {

    @Column(name = "idorigenp")
    private int idorigenp;

    @Column(name = "idproducto")
    private int idproducto;

    @Column(name = "idauxiliar")
    private int idauxiliar;

    @Column(name = "idamortizacion")
    private int idamortizacion;

    public AmortizacionesPK() {
    }

    public AmortizacionesPK(int idorigenp, int idproducto, int idauxiliar) {
        this.idorigenp = idorigenp;
        this.idproducto = idproducto;
        this.idauxiliar = idauxiliar;
    }

    public AmortizacionesPK(int idorigenp, int idproducto, int idauxiliar, int idamortizacion) {
        this.idorigenp = idorigenp;
        this.idproducto = idproducto;
        this.idauxiliar = idauxiliar;
        this.idamortizacion = idamortizacion;
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

    public int getIdamortizacion() {
        return this.idamortizacion;
    }

    public void setIdamortizacion(int idamortizacion) {
        this.idamortizacion = idamortizacion;
    }

    public int hashCode() {
        int hash = 0;
        hash += this.idorigenp;
        hash += this.idproducto;
        hash += this.idauxiliar;
        hash += this.idamortizacion;
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof com.fenoreste.rest.Entidades.AmortizacionesPK)) {
            return false;
        }
        com.fenoreste.rest.Entidades.AmortizacionesPK other = (com.fenoreste.rest.Entidades.AmortizacionesPK) object;
        if (this.idorigenp != other.idorigenp) {
            return false;
        }
        if (this.idproducto != other.idproducto) {
            return false;
        }
        if (this.idauxiliar != other.idauxiliar) {
            return false;
        }
        return (this.idamortizacion == other.idamortizacion);
    }

    public String toString() {
        return "com.fenoreste.modelo.entidad.AmortizacionesPK[ idorigenp=" + this.idorigenp + ", idproducto=" + this.idproducto + ", idauxiliar=" + this.idauxiliar + ", idamortizacion=" + this.idamortizacion + " ]";
    }

}
