package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import com.fenoreste.rest.Dao.BalanceDAO;
import DTO.BalancesDTO;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("api/balances")
public class BalancesResources {

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response balances(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        System.out.println("Request Balanceeeee:" + cadena);
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonObject datosError = new JsonObject();
        JSONObject datosEntrada = new JSONObject(cadena);
        JsonObject Error = new JsonObject();
        BalanceDAO dao = new BalanceDAO();
        javax.json.JsonObject json1 = null;
        JsonArrayBuilder jsona = Json.createArrayBuilder();

        /*if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        try {

            JSONArray accounts = datosEntrada.getJSONArray("accounts");
            String[] arrc = new String[accounts.length()];
            for (int i = 0; i < accounts.length(); i++) {
                String accountId = accounts.getJSONObject(i).getString("accountId");
                arrc[i] = accountId;
            }
            ArrayList<BalancesDTO> arr = dao.balances(arrc);
            for (int x = 0; x < arr.size(); x++) {
                BalancesDTO balance = arr.get(x);
                System.out.println("dtoB:" + balance);
                javax.json.JsonObject clientes1 = Json.createObjectBuilder().add("balanceType", "ledger")
                        .add("amount", (JsonValue) Json.createObjectBuilder()
                                .add("amount", balance.getLedger())//formato1.format(dtoB[0]))
                                .add("currencyCode", "MXN")
                                .build())
                        .build();
                javax.json.JsonObject clientes2 = Json.createObjectBuilder().add("balanceType", "available")
                        .add("amount", (JsonValue) Json.createObjectBuilder()
                                .add("amount", balance.getAvalaible()) //formato1.format(arr[1]))
                                .add("currencyCode", "MXN").build())
                        .build();

                jsona.add(Json.createObjectBuilder().add("accountId", balance.getAccountId()).add("balances", Json.createArrayBuilder().add((JsonValue) clientes1).add((JsonValue) clientes2)));

            }
            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
            separadoresPersonalizados.setDecimalSeparator('.');
            DecimalFormat formato1 = new DecimalFormat("0.00", separadoresPersonalizados);
            //javax.json.JsonObject clientes1 = Json.createObjectBuilder().add("balanceType", "ledger").add("amount", (JsonValue)Json.createObjectBuilder().add("amount",formato1.format(arr[0])).add("currencyCode", "MXN").build()).build();
            //javax.json.JsonObject clientes2 = Json.createObjectBuilder().add("balanceType", "available").add("amount", (JsonValue)Json.createObjectBuilder().add("amount", formato1.format(arr[1])).add("currencyCode", "MXN").build()).build();
            json1 = Json.createObjectBuilder().add("balances", jsona).build();

            return Response.status(Response.Status.OK).entity(json1).build();
        } catch (Exception e) {
            datosError.put("Error:", "Request Json Failed");
            System.out.println("Error:" + e.getMessage());
            //return Response.status(Response.Status.BAD_GATEWAY).entity(datosError).build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(datosError).build();
        }
    }
}
