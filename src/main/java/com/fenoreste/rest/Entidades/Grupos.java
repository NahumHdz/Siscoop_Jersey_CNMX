package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "grupos")
public class Grupos implements Serializable {

    @Id
    @Column(name = "idgrupo")
    private int idgrupo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipogrupo")
    private String tipogrupo;

    public Grupos() {
    }

    public Grupos(int idgrupo, String nombre, String tipogrupo) {
        this.idgrupo = idgrupo;
        this.nombre = nombre;
        this.tipogrupo = tipogrupo;
    }

    public int getIdgrupo() {
        return this.idgrupo;
    }

    public void setIdgrupo(int idgrupo) {
        this.idgrupo = idgrupo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipogrupo() {
        return this.tipogrupo;
    }

    public void setTipogrupo(String tipogrupo) {
        this.tipogrupo = tipogrupo;
    }

    public String toString() {
        return "Grupos{idgrupo=" + this.idgrupo + ", nombre=" + this.nombre + ", tipogrupo=" + this.tipogrupo + '}';
    }

}
