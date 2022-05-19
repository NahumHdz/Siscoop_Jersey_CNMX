package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import DTO.AccountHoldersDTO;
import DTO.AccountHoldersValidateDTO;
import DTO.Auxiliares_dDTO;
import DTO.DetailsAccountDTO;
import DTO.HoldsDTO;
import com.fenoreste.rest.Dao.AccountsDAO;
import com.fenoreste.rest.Dao.TransfersDAO;
import com.fenoreste.rest.Entidades.AuxiliaresD;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.codehaus.jettison.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("api/account")
public class AccountsResources {

    @POST
    @Path("/holders")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response accountHolders(String cadena, @HeaderParam("authorization") String authString) throws JSONException {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject request = new JSONObject(cadena);
        String accountId = "";
        JsonObject jsonb = new JsonObject();
        List<AccountHoldersDTO> listaHolders = null;
        try {
            accountId = request.getString("accountId");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        System.out.println("accountId:" + accountId);
        JsonObject Error = new JsonObject();
        TransfersDAO dao = new TransfersDAO();
        String msj = "";

        /*if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        try {

            listaHolders = dao.accountHolders(accountId);
            jsonb.put("holders", listaHolders);
            return Response.status(Response.Status.OK).entity(jsonb).build();
        } catch (Exception e) {
            System.out.println("Error en response:" + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("/internal/validate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response validateInternalAccount(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();

        System.out.println("Request cadenaaaaaaaaaaaaaaaa internal validate:" + cadena);
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        JsonObject Error = new JsonObject();
        AccountsDAO acDao = new AccountsDAO();
        String accountId = "";

        /*if (!acDao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        System.out.println("Cadena:" + cadena);
        try {
            JSONObject jsonRecibido = new JSONObject(cadena);
            System.out.println("JsonRecibido:" + jsonRecibido);
            accountId = jsonRecibido.getString("accountNumber");
            if (accountId.equals("053472372372")) {

            } else {
                int p = Integer.parseInt(accountId.substring(6, 11));
                List<AccountHoldersValidateDTO> listaHolder = acDao.validateInternalAccount(accountId);
                AccountHoldersValidateDTO holder = listaHolder.get(0);
                javax.json.JsonObject create = null;
                create = Json.createObjectBuilder().add("accountId", accountId)
                        .add("accountType", acDao.accountType(p).toUpperCase())
                        .add("holders", Json.createArrayBuilder()
                                .add((JsonValue) Json.createObjectBuilder()
                                        .add("customerId", holder.getCustomerId() /*"01010110021543"*/)
                                        .add("name", holder.getName())
                                        .add("relationCode", holder.getRelationCode()).build()))
                        .add("displayAccountNumber", "***************" + accountId.substring(15, 19)).build();
                /*.add("displayAccountNumber", "*******510").build();*/
                return Response.status(Response.Status.OK).entity(create).build();

            }

        } catch (Exception e) {
            System.out.println("Error al obtener objetos Json:" + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("/statements")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response statements(String cadena, @HeaderParam("authorization") String authString) {

        Security scr = new Security();
        System.out.println("CADENA: " + cadena);
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        JsonObject Error = new JsonObject();
        AccountsDAO acDao = new AccountsDAO();
        String accountId = "";
        int pageSize = 0;
        int pageStartIndex = 0;

        /*if (!acDao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        try {
            JSONObject jsonRecibido = new JSONObject(cadena);
            JSONArray listaFil = jsonRecibido.getJSONArray("filters");
            System.out.println("ListaFil:" + listaFil);
            //System.out.println("Cadenaaaaa:" + cadena);
            String id = "";
            String fd = "";
            for (int i = 0; i < listaFil.length(); i++) {
                JSONObject js = (JSONObject) listaFil.get(0);
                JSONObject js1 = (JSONObject) listaFil.get(1);
                id = js.getString("value");
                fd = js1.getString("value");
                System.out.println("id:" + id + ",fd:" + fd);
            }
            accountId = jsonRecibido.getString("accountId");
            System.out.println("AccountId:" + accountId);
            String nombrePDF = acDao.statements(accountId, id, fd, pageStartIndex, pageSize);
            System.out.println("nombrePDF:" + nombrePDF);
            JsonObject create = null;
            JsonArrayBuilder listaJson = Json.createArrayBuilder();

            javax.json.JsonObject jsi = Json.createObjectBuilder().add("statementId", accountId).add("dateFrom", id).add("dateTo", fd).add("displayName", accountId).add("availableFormats", Json.createArrayBuilder().add((JsonValue) Json.createObjectBuilder().add("type", "PDF").add("fileId", nombrePDF.replace(".pdf", "")).build())).build();
            listaJson.add((JsonValue) jsi);

            javax.json.JsonObject Found = Json.createObjectBuilder().add("statements", listaJson).build();
            return Response.status(Response.Status.OK).entity(Found).build();
        } catch (Exception e) {
            System.out.println("Error al obtener objetos Json:" + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("/holds")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response Holds(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject jsonre = new JSONObject(cadena);
        String accountId = "";
        JsonObject Error = new JsonObject();
        AccountsDAO dao = new AccountsDAO();

        /*if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        try {
            accountId = jsonre.getString("accountId");
            List<HoldsDTO> lista = dao.holds(accountId);
            JsonObject create = null;
            JsonArrayBuilder listaJson = Json.createArrayBuilder();
            for (int i = 0; i < lista.size(); i++) {
                HoldsDTO dto = lista.get(i);
                String ff = String.valueOf(dto.getEntryDate()) + " 00:00:00";
                Timestamp tss = Timestamp.valueOf(ff);
                System.out.println("tss:" + tss);
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(dto.getEntryDate() + "T00:00:00.000-06:00");
                String feR = String.valueOf(zonedDateTime);
                System.out.println("feR:" + feR);
                javax.json.JsonObject jsi = Json.createObjectBuilder().add("holdId", dto.getHoldId())
                                                                                                  .add("amount", Json.createObjectBuilder()
                                                                                                          .add("amount", dto.getAmount().doubleValue())
                                                                                                          .add("currencyCode", "MXN").build())
                                                                                                  .add("entryDate", feR)
                                                                                                  .add("description", dto.getDescritpion()).build();
                listaJson.add((JsonValue) jsi);
            }
            javax.json.JsonObject Found = Json.createObjectBuilder().add("holds", listaJson).build();
            return Response.status(Response.Status.OK).entity(Found).build();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        }
    }

    @POST
    @Path("/history")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response History(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        System.out.println("CADENA HISTORY: " + cadena);

        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        String accountId = "", initialDate = "", finalDate = "";
        int pageSize = 0, pageStartIndex = 0;

        JsonObject Error = new JsonObject();
        AccountsDAO dao = new AccountsDAO();
        JSONObject jsonRecibido = new JSONObject(cadena);
        List<String> lista_montos = new ArrayList<>();
        List<String> lista_fechas = new ArrayList<>();
        String transaction_type = "";
        int count = 0;

        /*if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        try {
            JSONArray listaFil = jsonRecibido.getJSONArray("filters");
            System.out.println("ListaFil:" + listaFil);
            initialDate = "";
            finalDate = "";
            accountId = jsonRecibido.getString("accountId");
            pageSize = jsonRecibido.getInt("pageSize");
            pageStartIndex = jsonRecibido.getInt("page");
            for (int i = 0; i < listaFil.length(); i++) {
                /*JSONObject js = (JSONObject) listaFil.get(0);
                JSONObject js1 = (JSONObject) listaFil.get(1);
                initialDate = js.getString("value");
                finalDate = js1.getString("value");
                System.out.println("id:" + initialDate + ",fd:" + finalDate);*/
                JSONObject json_pos_i = listaFil.getJSONObject(i);
                if (json_pos_i.getString("property").contains("executionDate")) {
                    lista_fechas.add(json_pos_i.getString("value"));
                } else if (json_pos_i.getString("property").contains("amount")) {
                    lista_montos.add(String.valueOf(json_pos_i.getInt("value")));
                } else if (json_pos_i.getString("property").contains("transactionType")) {
                    transaction_type = json_pos_i.getString("value");
                } else if (json_pos_i.getString("property").contains("count")) {
                    count = Integer.parseInt(String.valueOf(json_pos_i.getInt("value")));
                }
            }

            List<Auxiliares_dDTO> lista = dao.History(accountId, lista_fechas, lista_montos, transaction_type, count, pageSize, pageStartIndex);
            //List<AuxiliaresD> lista_size = dao.History_Size(accountId, initialDate, finalDate);
            JsonObject create = null;
            JsonArrayBuilder listaJson = Json.createArrayBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss");
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaDate = null;
            String fe = "";
            double monto = 0.0;
            String referencia = "";
            String pol = "";
            String descrip = "";
            int to_reg = 0;
            for (int j = 0; j < lista.size(); j++) {
                to_reg = lista.get(j).getTotal_lista();
                Auxiliares_dDTO ax = lista.get(j);
                /*fechaDate = formato.parse(String.valueOf(ax.getAuxiliaresDPK().getFecha()).substring(0, 10));
                fe = sdf.format(fechaDate);*/
                fe = sdf.format(ax.getAuxiliaresDPK().getFecha());
                System.out.println("REGISTROS " + " Fecha: " + fe + " CargoAbono: " + ax.getCargoabono() + " Monto: " + ax.getMonto() + " Saldoec " + ax.getSaldoec());

                ZonedDateTime zonedDateTime = ZonedDateTime.parse(fe.replace("/", "-") + "T00:00:00.000-06:00");
                String feR = String.valueOf(zonedDateTime);
                //System.out.println("DTOCtaOrigen:" + dto.getCuentaorigen());
                referencia = ax.getIdorigenc() + "-" + ax.getPeriodo() + "-" + ax.getIdtipo() + "-" + ax.getIdpoliza();
                //Descripcion poliza
                String o = String.format("%06d", ax.getIdorigenc()), t = String.format("%02d", ax.getIdtipo()), p = String.format("%05d", ax.getIdpoliza());
                pol = o + "-" + t + "-" + p;
                //Pintamos los saldos + y -s 
                if (ax.getCargoabono() == 0) {
                    monto = -ax.getMonto().doubleValue();
                } else if (ax.getCargoabono() == 1) {
                    monto = ax.getMonto().doubleValue();
                }
                //Descripcion
                if (ax.getCargoabono()==0) {
                    descrip = "RETIRO";
                } else if (ax.getCargoabono()==1) {
                    descrip = "DEPÓSITO";
                }
                javax.json.JsonObject jsi = Json.createObjectBuilder().add("transactionId", referencia.replace("-", ""))
                                                                                                  .add("amount", Json.createObjectBuilder()
                                                                                                          .add("amount", monto)
                                                                                                          .add("currencyCode", "MXN").build())
                                                                                                  .add("postingDate", feR)
                                                                                                  .add("valueDate", fe.replace("/", "-"))
                                                                                                  .add("runningBalance", Json.createObjectBuilder()
                                                                                                          .add("amount", ax.getSaldoec())
                                                                                                          .add("currencyCode", "MXN").build())
                                                                                                  /*.add("description", ax.getTicket())*/
                                                                                                  .add("description", descrip)
                                                                                                  .add("originatorReferenceId", referencia)
                                                                                                  .add("originatorCode", pol) /*referencia.replace("-", "")*/
                                                                                                  .add("description2", Json.createObjectBuilder()
                                                                                                          .add("value", pol) /*referencia.replace("-", "")*/
                                                                                                          .add("valueType", "string")
                                                                                                          .add("isSensitive", false).build()).build();
                listaJson.add(jsi);

            }

            javax.json.JsonObject Found = Json.createObjectBuilder().add("totalRecords", to_reg).add("queryId", "").add("transactions", listaJson).build();
            return Response.status(Response.Status.OK).entity(Found).build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("/details")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response Details(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String accountId = "";
        JsonObject Error = new JsonObject();
        AccountsDAO dao = new AccountsDAO();
        JSONObject jsonRecibido = new JSONObject(cadena);

        /*if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        try {
            accountId = jsonRecibido.getString("accountId");
            DetailsAccountDTO dto = dao.detailsAccount(accountId);
            System.out.println("DTO:" + dto);
            javax.json.JsonObject jsi = null;
            if (dto.getAccountType().toUpperCase().contains("TIM") || dto.getAccountType().toUpperCase().contains("LOA")) {
                jsi = Json.createObjectBuilder().add("accountDetails", Json.createObjectBuilder()
                        .add("accountId", dto.getAccountId())
                        .add("accountNumber", dto.getAccountNumber())
                        .add("displayAccountNumber", dto.getDisplayAccountNumber())
                        .add("accountType", dto.getAccountType().trim())
                        .add("currencyCode", dto.getCurrencyCode())
                        .add("productCode", dto.getProductCode())
                        .add("status", dto.getStatus())
                        .add("branch", (JsonValue) Json.createObjectBuilder()
                                .add("value", dto.getSucursal())
                                .add("valueType", "string")
                                .add("isSensitive", false)
                                .build())
                        .add("openedDate", (JsonValue) Json.createObjectBuilder()
                                .add("value", dto.getOpenedDate())
                                .add("valueType", "date")
                                .add("isSensitive", false)
                                .build())
                        .add("iban", (JsonValue) Json.createObjectBuilder()
                                .add("description", "IBAN")
                                .add("isSensitive", true)
                                .add("valueType", "string")
                                .add("value", dto.getAccountId().substring(0, 2) + "***************" + dto.getAccountId().substring(17, 19))
                                .build())
                        .add("interestRate", Json.createObjectBuilder()
                                .add("value", dto.getTasa())
                                .add("valueType", "decimal")
                                .add("isSensitive", false).build())
                        .add("nextPaymentAmount", Json.createObjectBuilder()
                                .add("value", dto.getProximoMontoInteres())
                                .add("valueType", "decimal")
                                .add("isSensitive", false).build())
                        .add("nextPaymentDate", Json.createObjectBuilder()
                                .add("value", dto.getProximaFechaPago())
                                .add("valueType", "date")
                                .add("isSensitive", false).build())
                        .add("maturityDate", Json.createObjectBuilder()
                                .add("value", dto.getFechaVencimiento())
                                .add("valueType", "date")
                                .add("isSensitive", false).build())
                        .add("expiryDate", Json.createObjectBuilder()
                                .add("value", dto.getFechaVencimiento())
                                .add("valueType", "date")
                                .add("isSensitive", false).build())
                        .add("loanAmount", Json.createObjectBuilder()
                                .add("value", dto.getMontoDesembolso())
                                .addNull("description")
                                .add("valueType", "decimal")
                                .add("isSensitive", false).build())
                        .build()).build();
            } else {
                jsi = Json.createObjectBuilder().add("accountDetails", Json.createObjectBuilder()
                        .add("accountId", dto.getAccountId())
                        .add("accountNumber", dto.getAccountNumber())
                        .add("displayAccountNumber", dto.getDisplayAccountNumber())
                        .add("accountType", dto.getAccountType().trim())
                        .add("currencyCode", dto.getCurrencyCode())
                        .add("productCode", dto.getProductCode())
                        .add("status", dto.getStatus())
                        .add("branch", (JsonValue) Json.createObjectBuilder()
                                .add("value", dto.getSucursal())
                                .add("valueType", "string")
                                .add("isSensitive", false)
                                .build())
                        .add("openedDate", (JsonValue) Json.createObjectBuilder()
                                .add("value", dto.getOpenedDate())
                                .add("valueType", "date")
                                .add("isSensitive", false)
                                .build())
                        .add("iban", (JsonValue) Json.createObjectBuilder()
                                .add("description", "IBAN")
                                .add("isSensitive", true)
                                .add("valueType", "string")
                                .add("value", dto.getAccountId().substring(0, 2) + "***************" + dto.getAccountId().substring(17, 19))
                                .build())).build();
            }

            return Response.status(Response.Status.OK).entity(jsi).build();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("/create/validate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response validateCreate(String cadena) {
        try {
            org.codehaus.jettison.json.JSONObject jsonR = new org.codehaus.jettison.json.JSONObject(cadena);
            String accountType = "", customerId = "", productType = "";
            int page = 0, pageSize = 0;
            accountType = jsonR.getString("accountType");
            customerId = jsonR.getString("customerId");
            productType = jsonR.getString("productCode");

            String validationId = RandomAlfa();

            javax.json.JsonObject json = Json.createObjectBuilder().add("validationId", validationId)
                    .add("fees", Json.createArrayBuilder())
                    .add("executionDate", "2021-05-12")
                    .build();

            return Response.status(Response.Status.OK).entity(json).build();

        } catch (JSONException ex) {
            System.out.println("Error al crear json:" + ex.getMessage());
        }
        return null;

    }

    @POST
    @Path("/create/execute")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response executeCreate(String cadena) {
        try {
            org.codehaus.jettison.json.JSONObject jsonR = new org.codehaus.jettison.json.JSONObject(cadena);
            String validationId = "";

            validationId = jsonR.getString("validationId");

            javax.json.JsonObject json = Json.createObjectBuilder().add("status", "completed")
                    .add("accountId", "TIME")
                    .add("accountNumber", "67564236465646")
                    .build();

            return Response.status(Response.Status.OK).entity(json).build();

        } catch (JSONException ex) {
            System.out.println("Error al crear json:" + ex.getMessage());
        }
        return null;

    }

    @POST
    @Path("/status/change/validate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response changeStatus(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject jsonRecibido = new JSONObject(cadena);
        javax.json.JsonObject json = null;
        try {
            json = Json.createObjectBuilder().add("validationId", "0967998686787").add("fees", Json.createArrayBuilder()).add("effectiveDate", "").build();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return Response.status(Response.Status.OK).entity(json).build();
    }

    @POST
    @Path("/status/change/execute")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response changeStatusExecute(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject jsonRecibido = new JSONObject(cadena);
        javax.json.JsonObject json = null;
        try {
            json = Json.createObjectBuilder().add("status", "completed").build();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return Response.status(Response.Status.OK).entity(json).build();
    }

    public String RandomAlfa() {
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";

        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();

        String cadena = "";
        for (int i = 0; i < 15; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            cadena = cadena + rndChar;
        }
        System.out.println("Cadena:" + cadena);
        return cadena;
    }

}
