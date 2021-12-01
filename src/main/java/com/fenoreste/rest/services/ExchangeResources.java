/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author Elliot
 */
@Path("/api/exchange")
public class ExchangeResources {

    @POST
    @Path("/convert")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response convertAmount(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject json = new JSONObject(cadena);
        System.out.println("JSONConvertAmount:" + json);
        try {
            JSONObject jsonC = json.getJSONObject("amount");
            Double amount = jsonC.getDouble("amount");

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.put("amount", amount);
            jsonResponse.put("currencyCode", "MXN");
            JsonObject response = new JsonObject();
            response.put("convertedAmount", jsonResponse);
            System.out.println("salio");
            return Response.status(Response.Status.OK).entity(response).build();

        } catch (Exception e) {
            System.out.println("Error al retornar objetos:" + e.getMessage());
        }
        return null;
    }

    @GET
    @Path("/rates")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response rate(@HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            Date d = new Date();
            Timestamp time = new Timestamp(d.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String Feh = sdf.format(time);
            System.out.println("FECHA HOYYY: " + Feh);

            /*ZonedDateTime zonedDateTime = ZonedDateTime.parse("2021-06-09T00:00:00.000-07:00");
            String feR = String.valueOf(zonedDateTime);*/

            javax.json.JsonObject jsonOb = Json.createObjectBuilder().add("exchangeRates", Json.createArrayBuilder()
                                                                                                            .add(Json.createObjectBuilder()
                                                                                                            .add("bookRate", 1)
                                                                                                            .add("timestamp", Feh /*feR*/)
                                                                                                            .add("rates", Json.createArrayBuilder()
                                                                                                                  .add(Json.createObjectBuilder()
                                                                                                                  .add("rateType", "payments")
                                                                                                                  .add("buy", 1)
                                                                                                                  .add("sell", 1)
                                                                                                                  .add("median", 1)
                                                                                                                  .build())
                                                                                                            )
                                                                                                      .add("currencyCode", "MXN")
                                                                                                      .build())
                                                                                                .build())
                                                                                        .build();

            return Response.status(Response.Status.OK).entity(jsonOb).build();

        } catch (Exception e) {
            System.out.println("Error al retornar objetos:" + e.getMessage());
        }
        return null;
    }
}
