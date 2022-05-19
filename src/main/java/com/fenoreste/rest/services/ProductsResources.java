package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import com.fenoreste.rest.Dao.ProductsDAO;
import DTO.ProductsDTO;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.Calendar;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/api/products")
public class ProductsResources {

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getProductos(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        System.out.println("Cadena:Productos:::::" + cadena);
        System.out.println("Consultando productos....");
        JSONObject jsonr = null;
        String accountType = "", property = "";
        if (!cadena.equals("")) {
            try {
                jsonr = new JSONObject(cadena);
                JSONArray json = jsonr.getJSONArray("filters");
                JSONObject s = json.getJSONObject(0);
                accountType = s.getString("value");
            } catch (Exception e) {
                System.out.println("Error al convertir json:" + e.getMessage());
            }
        }
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        javax.json.JsonObject datosOK = null;
        JsonArrayBuilder arraycuentas = Json.createArrayBuilder();

        JsonObject Error = new JsonObject();
        ProductsDAO pr = new ProductsDAO();

        /*if (!pr.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        try {
            System.out.println("dentro del try");
            System.out.println("accountType:" + accountType);
            List<ProductsDTO> ListaProductos = pr.getProductos(accountType);
            System.out.println("paso");
            if (ListaProductos != null) {
                System.out.println("dentro del fort");
                for (int i = 0; i < ListaProductos.size(); i++) {
                    JsonObjectBuilder data = Json.createObjectBuilder();
                    ProductsDTO prod = ListaProductos.get(i);
                    String at = prod.getProducttypename();
                    String value = "";
                    if (at.contains("TIME")) {
                        at = "TIME";
                        javax.json.JsonObject responseIversiones = null;
                        if (Integer.parseInt(prod.getIdproducto()) == 200) {
                            value = "30";
                        }
                        if (Integer.parseInt(prod.getIdproducto()) == 201) {
                            value = "60";
                        }
                        if (Integer.parseInt(prod.getIdproducto()) == 202) {
                            value = "90";
                        }
                        if (Integer.parseInt(prod.getIdproducto()) == 203) {
                            value = "180";
                        }
                        responseIversiones = data.add("productCode", prod.getIdproducto())
                                .add("accountType", at.toUpperCase())
                                .add("description", prod.getDescripcion())
                                .add("currencyCode", "MXN")
                                .add("frequency", Json.createObjectBuilder()
                                        .add("value", value)
                                        .add("valueType", "integer")
                                        .add("isSensitive", false)
                                        .build())
                                .add("period", Json.createObjectBuilder()
                                        .add("value", "D")
                                        .add("valueType", "string")
                                        .add("isSensitive", false)
                                        .build())
                                .add("interestDispositionCode", Json.createObjectBuilder()
                                        .add("value", "T")
                                        .add("valueType", "string")
                                        .add("isSensitive", false)
                                        .build())
                                .build();//.add("frecuency", (JsonValue)Json.createObjectBuilder().add("value", 90).add("valueType", "integer").add("isSensitive", "false").build()).add("period", (JsonValue)Json.createObjectBuilder().add("value", "D").add("valueType", "string").add("isSensitive", "false").build()).build();

                        arraycuentas.add(responseIversiones);
                    } else {
                        datosOK = data.add("productCode", prod.getIdproducto()).add("accountType", at.toUpperCase()).add("description", prod.getDescripcion()).add("currencyCode", "MXN").build();//.add("frecuency", (JsonValue)Json.createObjectBuilder().add("value", 90).add("valueType", "integer").add("isSensitive", "false").build()).add("period", (JsonValue)Json.createObjectBuilder().add("value", "D").add("valueType", "string").add("isSensitive", "false").build()).build();
                        arraycuentas.add(datosOK);
                    }
                }
                javax.json.JsonObject Found = Json.createObjectBuilder().add("products", arraycuentas).build();
                System.out.println("Response productos:   \n" + Found);
                return Response.status(Response.Status.OK).entity(Found).build();
            }
            JsonObject Not_Found = new JsonObject();
            Not_Found.put("Error", "DATOS NO ENCONTRADOS");
            Response.status(Response.Status.NO_CONTENT).entity(Not_Found).build();
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @POST
    @Path("/product/rate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response Rates(String cadena, @HeaderParam("authorization") String authString) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject json = new JSONObject(cadena);
        Calendar c1 = Calendar.getInstance();
        String dia = Integer.toString(c1.get(5));
        String mes = Integer.toString(c1.get(2) + 1);
        String annio = Integer.toString(c1.get(1));
        String diaa = dia + "/" + mes + "/" + annio;

        JsonObject Error = new JsonObject();
        ProductsDAO dao = new ProductsDAO();

        /*if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }*/

        try {
            String accountType = "", customerId = "", productCode = "";
            int amount = 0;
            accountType = json.getString("accountType");
            customerId = json.getString("customerId");
            productCode = json.getString("productCode");
            amount = json.getInt("amount");
            System.out.println("llego");
            //List<String>lista=dao.Rates(accountType.replace(" ","").trim(),amount,customerId, productCode);
            boolean ba = dao.productRates(accountType.replace(" ", "").trim(), productCode);
            if (ba) {
                javax.json.JsonObject json1 = Json.createObjectBuilder().add("interestRate", 7.00)
                        .add("maturityDate", "2021-07-05")
                        .add("minInitialDepositAmount", 1000)
                        .build();
                return Response.status(Response.Status.OK).entity(json1).build();
            }

        } catch (Exception e) {
            System.out.println("Error al construir objeto:" + e.getMessage());
        }
        return null;
    }
}
