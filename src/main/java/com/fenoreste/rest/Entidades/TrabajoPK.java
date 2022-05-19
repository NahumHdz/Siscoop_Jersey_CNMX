/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.Entidades;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author wilmer
 */
@Cacheable(false)
@Embeddable
public class TrabajoPK implements Serializable {

    @Column(name = "idorigen")
    private String idtorigen;
    @Column(name = "idgrupo")
    private String idgrupo;
    @Column(name = "idsocio")
    private String idsocio;

    public TrabajoPK() {
    }

    public TrabajoPK(String idtorigen, String idgrupo, String idsocio) {
        this.idtorigen = idtorigen;
        this.idgrupo = idgrupo;
        this.idsocio = idsocio;
    }

    public String getIdtorigen() {
        return idtorigen;
    }

    public void setIdtorigen(String idtorigen) {
        this.idtorigen = idtorigen;
    }

    public String getIdgrupo() {
        return idgrupo;
    }

    public void setIdgrupo(String idgrupo) {
        this.idgrupo = idgrupo;
    }

    public String getIdsocio() {
        return idsocio;
    }

    public void setIdsocio(String idsocio) {
        this.idsocio = idsocio;
    }

}
