package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Cacheable(false)
@Embeddable
public class TablasPK implements Serializable {

    @Column(name = "idtabla")
    private String idtabla;
    @Column(name = "idelemento")
    private String idelemento;

    public TablasPK() {
    }

    public TablasPK(String idtabla, String idelemento) {
        this.idtabla = idtabla;
        this.idelemento = idelemento;
    }

    public String getIdtabla() {
        return this.idtabla;
    }

    public void setIdtabla(String idtabla) {
        this.idtabla = idtabla;
    }

    public String getIdelemento() {
        return this.idelemento;
    }

    public void setIdelemento(String idelemento) {
        this.idelemento = idelemento;
    }

    public int hashCode() {
        int hash = 0;
        hash += (this.idtabla != null) ? this.idtabla.hashCode() : 0;
        hash += (this.idelemento != null) ? this.idelemento.hashCode() : 0;
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof com.fenoreste.rest.Entidades.TablasPK)) {
            return false;
        }
        com.fenoreste.rest.Entidades.TablasPK other = (com.fenoreste.rest.Entidades.TablasPK) object;
        if ((this.idtabla == null && other.idtabla != null) || (this.idtabla != null && !this.idtabla.equals(other.idtabla))) {
            return false;
        }
        return ((this.idelemento != null || other.idelemento == null) && (this.idelemento == null || this.idelemento.equals(other.idelemento)));
    }

    public String toString() {
        return "com.fenoreste.rest.modelos.TablasPK[ idtabla=" + this.idtabla + ", idelemento=" + this.idelemento + " ]";
    }

}
