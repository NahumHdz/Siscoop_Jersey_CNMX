package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class v_auxiliaresPK implements Serializable {

    @Column(name = "idorigenp", nullable = false)
    private Integer idorigenp;

    @Column(name = "idproducto")
    private Integer idproducto;

    @Column(name = "idauxiliar")
    private Integer idauxiliar;

    public v_auxiliaresPK() {
    }

    public v_auxiliaresPK(Integer idorigenp, Integer idproducto, Integer idauxiliar) {
        this.idorigenp = idorigenp;
        this.idproducto = idproducto;
        this.idauxiliar = idauxiliar;
    }

    public Integer getIdorigenp() {
        return this.idorigenp;
    }

    public void setIdorigenp(Integer idorigenp) {
        this.idorigenp = idorigenp;
    }

    public Integer getIdproducto() {
        return this.idproducto;
    }

    public void setIdproducto(Integer idproducto) {
        this.idproducto = idproducto;
    }

    public Integer getIdauxiliar() {
        return this.idauxiliar;
    }

    public void setIdauxiliar(Integer idauxiliar) {
        this.idauxiliar = idauxiliar;
    }

    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.idorigenp);
        hash = 67 * hash + Objects.hashCode(this.idproducto);
        hash = 67 * hash + Objects.hashCode(this.idauxiliar);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        com.fenoreste.rest.Entidades.v_auxiliaresPK other = (com.fenoreste.rest.Entidades.v_auxiliaresPK) obj;
        if (!Objects.equals(this.idorigenp, other.idorigenp)) {
            return false;
        }
        if (!Objects.equals(this.idproducto, other.idproducto)) {
            return false;
        }
        if (!Objects.equals(this.idauxiliar, other.idauxiliar)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "com.fenoreste.modelo.entidad.AuxiliaresPK[ idorigenp=" + this.idorigenp + ", idproducto=" + this.idproducto + ", idauxiliar=" + this.idauxiliar + " ]";
    }

}
