/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author nahum
 */
public class ogsDTO {

    private int idorigen;
    private int idgrupo;
    private int idsocio;

    public ogsDTO() {
    }

    public ogsDTO(int idorigen, int idgrupo, int idsocio) {
        this.idorigen = idorigen;
        this.idgrupo = idgrupo;
        this.idsocio = idsocio;
    }

    public int getIdorigen() {
        return idorigen;
    }

    public void setIdorigen(int idorigen) {
        this.idorigen = idorigen;
    }

    public int getIdgrupo() {
        return idgrupo;
    }

    public void setIdgrupo(int idgrupo) {
        this.idgrupo = idgrupo;
    }

    public int getIdsocio() {
        return idsocio;
    }

    public void setIdsocio(int idsocio) {
        this.idsocio = idsocio;
    }

    @Override
    public String toString() {
        return "ogsDTO{" + "idorigen=" + idorigen + ", idgrupo=" + idgrupo + ", idsocio=" + idsocio + '}';
    }

}
