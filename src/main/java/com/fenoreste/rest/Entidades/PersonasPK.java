package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Cacheable(false)
@Embeddable
public class PersonasPK implements Serializable {

    @Column(name = "idorigen")
    private int idorigen;

    @Column(name = "idgrupo")
    private int idgrupo;

    @Column(name = "idsocio")
    private int idsocio;

    public PersonasPK() {
    }

    public PersonasPK(int idorigen, int idgrupo, int idsocio) {
        this.idorigen = idorigen;
        this.idgrupo = idgrupo;
        this.idsocio = idsocio;
    }

    public int getIdorigen() {
        return this.idorigen;
    }

    public void setIdorigen(int idorigen) {
        this.idorigen = idorigen;
    }

    public int getIdgrupo() {
        return this.idgrupo;
    }

    public void setIdgrupo(int idgrupo) {
        this.idgrupo = idgrupo;
    }

    public int getIdsocio() {
        return this.idsocio;
    }

    public void setIdsocio(int idsocio) {
        this.idsocio = idsocio;
    }

    public int hashCode() {
        int hash = 0;
        hash += this.idorigen;
        hash += this.idgrupo;
        hash += this.idsocio;
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof com.fenoreste.rest.Entidades.PersonasPK)) {
            return false;
        }
        com.fenoreste.rest.Entidades.PersonasPK other = (com.fenoreste.rest.Entidades.PersonasPK) object;
        if (this.idorigen != other.idorigen) {
            return false;
        }
        if (this.idgrupo != other.idgrupo) {
            return false;
        }
        return (this.idsocio == other.idsocio);
    }

    public String toString() {
        return "com.fenoreste.modelo.entidad.PersonasPK[ idorigen=" + this.idorigen + ", idgrupo=" + this.idgrupo + ", idsocio=" + this.idsocio + " ]";
    }

}
