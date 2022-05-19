/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import com.fenoreste.rest.Dao.AccountsDAO;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.List;
import javax.json.Json;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author wilmer
 */
@Path("api/beneficiary/validate")
public class ValidateBeneficiary {

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response validateBeneficiary(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        JsonObject Error = new JsonObject();
        AccountsDAO acDao = new AccountsDAO();

        /*if (!acDao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        String accountId = "";
        System.out.println("Cadena:" + cadena);
        String accountType = "";
        String nameb = "";
        String address = "";
        try {
            JSONObject jsonRecibido = new JSONObject(cadena);
            System.out.println("JsonRecibido:" + jsonRecibido);
            JSONObject json2 = jsonRecibido.getJSONObject("beneficiaryAccount");
            //JSONObject json3 = jsonRecibido.getJSONObject("beneficiary");
            //accountId = json2.getString("accountNumber");
            accountId = json2.getString("accountNumber");
            int p = Integer.parseInt(accountId.substring(6, 11));
            accountType = json2.getString("accountType");
            //nameb = json3.getString("name");
            //address = json3.getString("address");
            System.out.println("json2:" + json2);
            System.out.println("AccountNumber:" + accountId + "\n accounType:" + accountType + "\n name:" + nameb + "\n address:" + address);
            List<String> lista = acDao.validateBeneficiary(accountId, accountType.toUpperCase());
            javax.json.JsonObject create = null;
            create = Json.createObjectBuilder().add("beneficiaryAccount", Json.createObjectBuilder().add("accountNumber", lista.get(2)).add("accountType", lista.get(1)).add("accountSchemaType", "internal").build()).add("beneficiary", Json.createObjectBuilder().add("name", lista.get(0)).add("countryCode", "MX").build()).build();
            return Response.status(Response.Status.OK).entity(create).build();
        } catch (Exception e) {
            System.out.println("Error al obtener objetos Json:" + e.getMessage());
        }
        return null;
    }

}
