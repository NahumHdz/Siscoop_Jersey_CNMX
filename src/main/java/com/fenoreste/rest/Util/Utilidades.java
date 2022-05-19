/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.Util;

import DTO.ogsDTO;
import DTO.opaDTO;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author nahum
 */
public class Utilidades {

    public ogsDTO ogs(String cadena_ogs) {
        ogsDTO ogs = new ogsDTO();
        try {
            ogs.setIdorigen(Integer.parseInt(cadena_ogs.substring(0, 6)));
            ogs.setIdgrupo(Integer.parseInt(cadena_ogs.substring(6, 8)));
            ogs.setIdsocio(Integer.parseInt(cadena_ogs.substring(8, 14)));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return ogs;
    }

    public opaDTO opa(String cadena_opa) {
        opaDTO opa = new opaDTO();
        try {
            opa.setIdorigenp(Integer.parseInt(cadena_opa.substring(0, 6)));
            opa.setIdproducto(Integer.parseInt(cadena_opa.substring(6, 11)));
            opa.setIdauxiliar(Integer.parseInt(cadena_opa.substring(11, 19)));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return opa;
    }

    public boolean actividad(EntityManager em) {
        boolean bandera = false;

        try {
            String actividad = "SELECT sai_bankingly_servicio_activo_inactivo()";
            Query query = em.createNativeQuery(actividad);
            bandera = (boolean) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("ERROR AL RECUPERAR EL TIEMPO DE ACTIVIDAD: " + e.getMessage());
        }
        System.out.println("BANDERA HORARIO: " + bandera);
        return bandera;
    }

}
