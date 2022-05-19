package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Cacheable(false)
@Entity
@Table(name = "colonias")
public class Colonias implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcolonia")
    private Integer idcolonia;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "idmunicipio")
    private Integer idmunicipio;

    @Column(name = "codigopostal")
    private String codigopostal;

    public Colonias() {
    }

    public Colonias(Integer idcolonia) {
        this.idcolonia = idcolonia;
    }

    public Colonias(Integer idcolonia, String nombre) {
        this.idcolonia = idcolonia;
        this.nombre = nombre;
    }

    public Integer getIdcolonia() {
        return this.idcolonia;
    }

    public void setIdcolonia(Integer idcolonia) {
        this.idcolonia = idcolonia;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdmunicipio() {
        return this.idmunicipio;
    }

    public void setIdmunicipio(Integer idmunicipio) {
        this.idmunicipio = idmunicipio;
    }

    public String getCodigopostal() {
        return this.codigopostal;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    public int hashCode() {
        int hash = 0;
        hash += (this.idcolonia != null) ? this.idcolonia.hashCode() : 0;
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof com.fenoreste.rest.Entidades.Colonias)) {
            return false;
        }
        com.fenoreste.rest.Entidades.Colonias other = (com.fenoreste.rest.Entidades.Colonias) object;
        return ((this.idcolonia != null || other.idcolonia == null) && (this.idcolonia == null || this.idcolonia.equals(other.idcolonia)));
    }

    public String toString() {
        return "Colonias{idcolonia=" + this.idcolonia + ", nombre=" + this.nombre + ", idmunicipio=" + this.idmunicipio + ", codigopostal=" + this.codigopostal + '}';
    }

}
