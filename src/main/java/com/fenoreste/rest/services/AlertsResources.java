package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import com.fenoreste.rest.Dao.AlertsDAO;
import com.github.cliftonlabs.json_simple.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Elliot
 */
@Path("api/alert")
public class AlertsResources {

    @POST
    @Path("/subscription/set/validate")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public Response alertValidate(String cadena, @HeaderParam("authorization") String authString) {
        System.out.println("Request Alerts Validate:" + cadena);
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if (cadena.contains("null")) {
            cadena = cadena.replace("null", "nulo");
        }
        JSONObject jsonRecibido = new JSONObject(cadena);
        System.out.println("JsonSubscriptionValidate:" + jsonRecibido);
        JsonObject Error = new JsonObject();
        AlertsDAO dao = new AlertsDAO();

        /*if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        String AlercustomerId_ = "", Alertcode_ = "", AlertAccountId_ = "";
        JSONArray rules_ = null;
        double rulesMonto = 0.0;
        boolean AlertEnabled_ = false;
        String validationId = "";

        try {
            Alertcode_ = jsonRecibido.getString("alertCode");
            if (Alertcode_.toUpperCase().contains("BALANCE_ABOVE") || Alertcode_.toUpperCase().contains("BALANCE_BELOW")) {
                AlercustomerId_ = jsonRecibido.getString("customerId");
                Alertcode_ = jsonRecibido.getString("alertCode");
                AlertEnabled_ = jsonRecibido.getBoolean("enabled");
                AlertAccountId_ = jsonRecibido.getString("accountId");
                rules_ = jsonRecibido.getJSONArray("rules");
                JSONObject amount = rules_.getJSONObject(0);
                rulesMonto = amount.getDouble("value");
                System.out.println("llego aqui");

                validationId = dao.validateAlert(AlercustomerId_, Alertcode_, AlertEnabled_, AlertAccountId_, rulesMonto, 0);

            } else if (Alertcode_.toUpperCase().contains("LOAN_PAYMENT_DUE") || Alertcode_.toUpperCase().contains("TIME_DEPOSIT_MATURING")) {
                AlercustomerId_ = jsonRecibido.getString("customerId");
                Alertcode_ = jsonRecibido.getString("alertCode");
                AlertEnabled_ = jsonRecibido.getBoolean("enabled");
                AlertAccountId_ = jsonRecibido.getString("accountId");

                validationId = dao.validateAlert(AlercustomerId_, Alertcode_, AlertEnabled_, AlertAccountId_, 0.0, 0);

            } else if (Alertcode_.toUpperCase().contains("MONETARY_TRANSACTION_INSTRUCTION")) {
                AlercustomerId_ = jsonRecibido.getString("customerId");
                Alertcode_ = jsonRecibido.getString("alertCode");
                AlertEnabled_ = jsonRecibido.getBoolean("enabled");

                //AlertAccountId_ = jsonRecibido.getString("accountId"); 
                validationId = dao.validateAlert(AlercustomerId_, Alertcode_, AlertEnabled_, AlertAccountId_, 0.0, 1);

            } else if (Alertcode_.toUpperCase().contains("LAST_RECURRING_TRANSACTION")) {
                AlercustomerId_ = jsonRecibido.getString("customerId");
                Alertcode_ = jsonRecibido.getString("alertCode");
                AlertEnabled_ = jsonRecibido.getBoolean("enabled");
                AlertAccountId_ = jsonRecibido.getString("accountId");

                validationId = dao.validateAlert(AlercustomerId_, Alertcode_, AlertEnabled_, AlertAccountId_, 0.0, 1);
            }

            //String validationId = dao.validateAlert(AlercustomerId_, Alertcode_, AlertEnabled_, AlertAccountId_, AlertRulesProperty, AlertRulesAmount_, AlertsRulesOperator_, AlertsRulesType_);
            com.github.cliftonlabs.json_simple.JsonObject json = new com.github.cliftonlabs.json_simple.JsonObject();
            json.put("validationId", validationId);
            json.put("fees", null);
            json.put("effectiveDate", null);
            return Response.status(Response.Status.OK).entity(json).build();
        } catch (Exception e) {
            System.out.println("Error al convertir json:" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/subscription/set/execute")
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public Response alertExecute(String cadena, @HeaderParam("authorization") String authString) {
        System.out.println("CADENA: " + cadena);
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject jsonRecibido = new JSONObject(cadena);
        System.out.println("SubscriptionAlertExecute:" + jsonRecibido);
        JsonObject Error = new JsonObject();
        AlertsDAO dao = new AlertsDAO();

        /*if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        try {
            String validationId = jsonRecibido.getString("validationId");
            String estatus = dao.executeAlert(validationId);
            com.github.cliftonlabs.json_simple.JsonObject json = new com.github.cliftonlabs.json_simple.JsonObject();
            json.put("status", estatus);
            return Response.status(Response.Status.OK).entity(json).build();
        } catch (Exception e) {
            System.out.println("Error al convertir json:" + e.getMessage());
        }
        return null;
    }
}
