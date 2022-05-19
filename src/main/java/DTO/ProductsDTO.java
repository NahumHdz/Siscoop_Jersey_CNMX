/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author Elliot
 */
public class ProductsDTO {

    private String idproducto;
    private Integer producttypeid;
    private String producttypename;
    private String descripcion;

    public ProductsDTO() {
    }

    public ProductsDTO(String idproducto, Integer producttypeid, String producttypename, String descripcion) {
        this.idproducto = idproducto;
        this.producttypeid = producttypeid;
        this.producttypename = producttypename;
        this.descripcion = descripcion;
    }

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public Integer getProducttypeid() {
        return producttypeid;
    }

    public void setProducttypeid(Integer producttypeid) {
        this.producttypeid = producttypeid;
    }

    public String getProducttypename() {
        return producttypename;
    }

    public void setProducttypename(String producttypename) {
        this.producttypename = producttypename;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "ProductsDTO{" + "idproducto=" + idproducto + ", producttypeid=" + producttypeid + ", producttypename=" + producttypename + ", descripcion=" + descripcion + '}';
    }

}
