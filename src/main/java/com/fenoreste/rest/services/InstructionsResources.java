package com.fenoreste.rest.services;

import com.fenoreste.rest.Auth.Security;
import com.fenoreste.rest.Dao.TransfersDAO;
import com.fenoreste.rest.Entidades.transferencias_completadas_siscoop;
import DTO.MonetaryInstructionDTO;
import DTO.OrderWsSPEI;
import DTO.validateMonetaryInstructionDTO;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("api/instructions")
public class InstructionsResources {

    @POST
    @Path("/monetary")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response MonetaryInstruction(String cadena, @HeaderParam("authorization") String authString) throws JSONException {
        System.out.println("cadenaMonetaryInstruction:" + cadena);
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject jrecibido = new JSONObject(cadena);
        String customerId = "";
        JSONArray filters = jrecibido.getJSONArray("filters");
        System.out.println("JSONFilters:" + filters);

        //Leyendo request 
        String FInicio = "", FFinal = "";
        try {
            customerId = jrecibido.getString("customerId");
            for (int i = 0; i < filters.length(); i++) {
                if (i == 2) {
                    JSONObject jsonFilter = filters.getJSONObject(i);
                    System.out.println("fecha:" + jsonFilter.getString("value"));
                    FInicio = jsonFilter.getString("value");
                }
                if (i == 3) {
                    JSONObject jsonFilter = filters.getJSONObject(i);
                    System.out.println("fecha:" + jsonFilter.getString("value"));
                    FFinal = jsonFilter.getString("value");
                }
                //System.out.println("JSONFilter:"+jsonFilters);
            }
            System.out.println("CustomerId:" + customerId);
            System.out.println("FechaInicio:" + FInicio);
            System.out.println("FechaFinal:" + FFinal);

        } catch (Exception e) {
            System.out.println("Error en tranformar json:" + e.getMessage());
            //return Response.status(Response.Status.BAD_GATEWAY).entity(e.getMessage()).build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        JsonObject Error = new JsonObject();
        TransfersDAO dao = new TransfersDAO();

        if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }

        try {
            //customerId = jrecibido.getString("customerId");
            //page = jrecibido.getInt("page");
            //pageSize = jrecibido.getInt("pageSize");
            List<MonetaryInstructionDTO> ListaTranferencias = dao.monetaryInistruction(customerId, FInicio, FFinal);
            JSONArray json = new JSONArray();
            javax.json.JsonArrayBuilder ArrayInstruction = Json.createArrayBuilder();
            for (int i = 0; i < ListaTranferencias.size(); i++) {
                javax.json.JsonObject jprincipal = null;
                String numbers = ListaTranferencias.get(i).getDebitAccount().substring(Math.max(0, ListaTranferencias.get(i).getDebitAccount().length() - 4));
                System.out.println("Numbers:" + numbers);
                jprincipal = Json.createObjectBuilder()
                        .add("monetaryInstructionId", String.valueOf(ListaTranferencias.get(i).getMonetaryId()))
                        .add("originatorTransactionType", ListaTranferencias.get(i).getOriginatorTransactionType())
                        .add("debitAccount", Json.createObjectBuilder()
                                .add("accountId", ListaTranferencias.get(i).getDebitAccount())
                                .add("accountNumber", ListaTranferencias.get(i).getDebitAccount())
                                .add("displayAccountNumber", "***************" + numbers)
                                .add("accountType", ListaTranferencias.get(i).getTypeNameId())
                                .build())
                        .add("creditDetails", Json.createObjectBuilder()
                                .add("instructionType", "single")
                                .add("creditAccount", Json.createObjectBuilder()
                                        .add("accountSchemaType", "internal")
                                        .add("accountId", ListaTranferencias.get(i).getCreditAccount())
                                        .add("accountNumber", ListaTranferencias.get(i).getCreditAccount())
                                        .build()).build())
                        .add("nextExecution", Json.createObjectBuilder()
                                .add("executionDate", ListaTranferencias.get(i).getExecutionDate().replace("/", "-"))
                                .add("executionAmount", Json.createObjectBuilder()
                                        .add("amount", ListaTranferencias.get(i).getMonto())
                                        .add("currencyCode", "MXN")
                                        .build()))
                        .add("frequency", Json.createObjectBuilder()
                                .add("frequencyType", "none").build())
                        .build();
                ArrayInstruction.add(jprincipal);
            }
            System.out.println("Json:" + json);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse("2021-03-23T18:21+01:00");

            javax.json.JsonObject jprincipal = Json.createObjectBuilder().add("totalRecords", ListaTranferencias.size())
                    .add("instructions", ArrayInstruction).build();

            return Response.status(Response.Status.OK).entity(jprincipal).build();
        } catch (Exception e) {
            System.out.println("Error en metodo:" + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("/monetary/validate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response validateMonetaryInstruction(String cadena, @HeaderParam("authorization") String authString) throws JSONException {
        validateMonetaryInstructionDTO dto = new validateMonetaryInstructionDTO();
        dto.setExecutionDate("");
        String feees[] = new String[3];
        dto.setFees(feees);
        dto.setValidationId("");
        ArrayList lista = new ArrayList();
        javax.json.JsonObject jsonResponse = null;

        Security scr = new Security();
        System.out.println("cadena:" + cadena);
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject request = new JSONObject(cadena);
        String customerId = "", tipoTranferencia = "", cuentaOrigen = "", cuentaDestino = "", comentario = "", propCuenta = "", fechaEjecucion = "", tipoEjecucion = "";
        Double monto = 0.0;
        String value = "";
        boolean bandera1 = false, bandera2 = false;
        Calendar c1 = Calendar.getInstance();
        String dia = Integer.toString(c1.get(5));
        String mes = Integer.toString(c1.get(2) + 1);
        String annio = Integer.toString(c1.get(1));
        String FechaTiempoReal = String.format("%04d", Integer.parseInt(annio)) + "-" + String.format("%02d", Integer.parseInt(mes)) + "-" + String.format("%02d", Integer.parseInt(dia));
        int identificadorTransferencia = 0;
        
        JSONObject mon_op = request.getJSONObject("monetaryOptions");
        JSONObject fec_exe = mon_op.getJSONObject("execution");
        fechaEjecucion = fec_exe.getString("executionDate");
        //System.out.println("FECHA EJECUCIONNNNNNNNNNNNNNNNNNNN: " + fechaEjecucion);

        try {
            //Transferencias TIPOS BILLER(Pago de servicios)
            if (request.getString("originatorTransactionType").toUpperCase().contains("BIL")) {
                customerId = request.getString("customerId");
                tipoTranferencia = request.getString("originatorTransactionType");
                cuentaOrigen = request.getString("debitAccountId");
                JSONObject credit = request.getJSONObject("creditAccount");
                cuentaDestino = credit.getString("billerCode");
                comentario = credit.getString("agreementCode");
                JSONObject billerFields = credit.getJSONObject("billerFields");
                JSONObject fieldTxt = billerFields.getJSONObject("01");
                value = fieldTxt.getString("value");
                JSONObject montoOP = request.getJSONObject("monetaryOptions");
                JSONObject montoR = montoOP.getJSONObject("amount");
                monto = montoR.getDouble("amount");
                JSONObject execution = montoOP.getJSONObject("execution");
                fechaEjecucion = execution.getString("executionDate");
                tipoEjecucion = execution.getString("executionType");
                identificadorTransferencia = 4;
            } //Transferencias SPEI 
            else if (request.getString("originatorTransactionType").toUpperCase().contains("DOMESTIC_PAYMENT")) {

                OrderWsSPEI orden = new OrderWsSPEI();
                orden.setCIF(request.getString("customerId"));
                orden.setClabeSolicitante(request.getString("debitAccountId"));
                JSONObject customer = request.getJSONObject("customerName");
                orden.setNombreSolicitante(customer.getString("value"));
                JSONObject customerRFC = request.getJSONObject("customerRFC");
                JSONObject customerEmail = request.getJSONObject("customerEmail");
                orden.setCorreoElectronicoSolicitante(customerEmail.getString("value"));
                //AHORA LLENO BENEFICIARIO
                JSONObject nombreBeneficiario = request.getJSONObject("creditor");
                orden.setNombreBeneficiario(nombreBeneficiario.getString("name"));
                JSONObject tipoCuentaBeneficiario = request.getJSONObject("benefAccountType");
                orden.setTipoCuentaBeneficiario(tipoCuentaBeneficiario.getInt("value"));
                JSONObject creditAccount = request.getJSONObject("creditAccount");
                orden.setCuentaTarjetaBeneficiario(creditAccount.getString("accountNumber"));
                JSONObject rfcBeneficiario = request.getJSONObject("customerRFC");
                orden.setRfcCurpBeneficario(rfcBeneficiario.getString("value"));
                orden.setInstitucionContraparte(Integer.parseInt(creditAccount.getString("bic")));
                JSONObject beneficiaryEmail = request.getJSONObject("beneficiaryEmail");
                orden.setCorreoElectronicoBeneficiario(beneficiaryEmail.getString("value"));
                JSONObject amount = request.getJSONObject("monetaryOptions").getJSONObject("amount");
                orden.setMonto(Double.parseDouble(amount.getString("amount")));
                JSONObject iva = request.getJSONObject("iva");
                orden.setIVA(iva.getDouble("value"));
                JSONObject comision = request.getJSONObject("commission");
                orden.setComision(comision.getDouble("value"));
                JSONObject conceptoPago = request.getJSONObject("description");
                orden.setConceptoPago(conceptoPago.getString("value"));
                JSONObject numeroReferencia = request.getJSONObject("referenceNumber");
                orden.setNumeroReferencia(numeroReferencia.getInt("value"));
                identificadorTransferencia = 5;

            } else {//transferencias NORMALES     

                customerId = request.getString("customerId");
                tipoTranferencia = request.getString("originatorTransactionType");
                cuentaOrigen = request.getString("debitAccountId");
                JSONObject credit = request.getJSONObject("creditAccount");
                cuentaDestino = credit.getString("accountId");
                comentario = request.getString("debtorComments");
                JSONObject montoOP = request.getJSONObject("monetaryOptions");
                JSONObject montoR = montoOP.getJSONObject("amount");
                monto = Double.parseDouble(montoR.getString("amount"));
                JSONObject execution = montoOP.getJSONObject("execution");
                fechaEjecucion = execution.getString("executionDate");
                tipoEjecucion = execution.getString("executionType");

                //Validamos entre programadas y en tiempo actual 
                if (!fechaEjecucion.equals(FechaTiempoReal)) {
                    //Es una programada entre mis cuentas
                    if (tipoTranferencia.toUpperCase().contains("TRANSFER_OWN")) {
                        identificadorTransferencia = 6;
                    } else if (tipoTranferencia.toUpperCase().contains("INTRABA")) {
                        identificadorTransferencia = 7;
                    } else if (tipoTranferencia.toUpperCase().contains("LOAN_PAY")) {
                        identificadorTransferencia = 8;
                    }

                } else {
                    //Si es una transferencias entre mis cuentas propias
                    if (tipoTranferencia.toUpperCase().contains("TRANSFER_OWN")) {
                        identificadorTransferencia = 1;
                    } else if (tipoTranferencia.toUpperCase().contains("INTRABANK")) {//Si es una trasnferencia a terceros dentro de la entidad
                        identificadorTransferencia = 2;
                    } else if (tipoTranferencia.toUpperCase().contains("LOAN_PAY")) {//Es un pago a prestamo a cuentas propias
                        identificadorTransferencia = 3;
                    }
                }
            }
        } catch (Exception e) {
            JsonObject json = new JsonObject();
            json.put("Error", "Parametros desconocidos:" + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        JsonObject Error = new JsonObject();
        TransfersDAO dao = new TransfersDAO();

        if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }

        try {
            System.out.println("FECHA HOY:  " + FechaTiempoReal + " FECHA EJECUCION: " + fechaEjecucion);
            
            if (FechaTiempoReal.equals(fechaEjecucion)) {
                System.out.println("FECHAS IGUALES");
                
                //Es una transferencia a mis cuentas propias
                if (identificadorTransferencia == 1) {
                    System.out.println("ennnnnnnnnntrooooooooooooooooooooooooooooooooooooooooooooooo");
                    dto = dao.validateMonetaryInstruction(customerId, cuentaOrigen, cuentaDestino, monto, comentario, propCuenta, fechaEjecucion, tipoEjecucion, tipoTranferencia, identificadorTransferencia);
                    //dto = dao.validateMonetaryInstruction(customerId,cuentaOrigen, cuentaDestino, monto, "Pago de servicios:" + comentario, "Codigo recibo:" + value, fechaEjecucion, tipoEjecucion,identificadorTransferencia);
                    jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                            .add("fees", Json.createArrayBuilder().build())
                            .add("executionDate", dto.getExecutionDate())
                            .build();
                } else if (identificadorTransferencia == 2) {
                    dto = dao.validateMonetaryInstruction(customerId, cuentaOrigen, cuentaDestino, monto, comentario, propCuenta, fechaEjecucion, tipoEjecucion, tipoTranferencia, identificadorTransferencia);
                    jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                            .add("fees", Json.createArrayBuilder().build())
                            .add("executionDate", dto.getExecutionDate())
                            .build();
                } else if (identificadorTransferencia == 3) {
                    dto = dao.validateMonetaryInstruction(customerId, cuentaOrigen, cuentaDestino, monto, comentario, propCuenta, fechaEjecucion, tipoEjecucion, tipoTranferencia, identificadorTransferencia);
                    System.out.println("dto:" + dto);
                    jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                            .add("fees", Json.createArrayBuilder().build())
                            .add("executionDate", dto.getExecutionDate())
                            .build();
                } else if (identificadorTransferencia == 4) {
                    dto = dao.validateMonetaryInstruction(customerId, cuentaOrigen, cuentaDestino, monto, "Pago de servicio:" + comentario, propCuenta, fechaEjecucion, tipoEjecucion, tipoTranferencia, identificadorTransferencia);
                    jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                            .add("fees", Json.createArrayBuilder().build())
                            .add("executionDate", dto.getExecutionDate())
                            .build();
                } else if (identificadorTransferencia == 5) {
                    dto = dao.validateMonetaryInstruction(customerId, cuentaOrigen, cuentaDestino, monto, comentario, cuentaDestino, fechaEjecucion, tipoEjecucion, tipoTranferencia, identificadorTransferencia);
                    jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                            .add("fees", Json.createArrayBuilder().build())
                            .add("executionDate", dto.getExecutionDate())
                            .build();
                } else if (identificadorTransferencia == 6) {
                    dto = dao.validateMonetaryInstruction(customerId, cuentaOrigen, cuentaDestino, monto, "Txt Programada" + comentario, cuentaDestino, fechaEjecucion, tipoEjecucion, tipoTranferencia, identificadorTransferencia);
                    jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                            .add("fees", Json.createArrayBuilder().build())
                            .add("executionDate", dto.getExecutionDate())
                            .build();
                } else if (identificadorTransferencia == 7) {
                    dto = dao.validateMonetaryInstruction(customerId, cuentaOrigen, cuentaDestino, monto, "Txt Programada" + comentario, cuentaDestino, fechaEjecucion, tipoEjecucion, tipoTranferencia, identificadorTransferencia);
                    jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                            .add("fees", Json.createArrayBuilder().build())
                            .add("executionDate", dto.getExecutionDate())
                            .build();
                } else if (identificadorTransferencia == 8) {
                    dto = dao.validateMonetaryInstruction(customerId, cuentaOrigen, cuentaDestino, monto, "Txt Programada" + comentario, cuentaDestino, fechaEjecucion, tipoEjecucion, tipoTranferencia, identificadorTransferencia);
                    jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                            .add("fees", Json.createArrayBuilder().build())
                            .add("executionDate", dto.getExecutionDate())
                            .build();
                }
            } else {
                System.out.println("FECHAS DIFERENTES");
                jsonResponse = Json.createObjectBuilder().add("type", "urn:vn:error-codes:VAL00001")
                                                                                    .add("title", "Validation failed on submitted data")
                                                                                    .add("errors", (JsonValue) Json.createArrayBuilder()
                                                                                            .add((JsonValue) Json.createObjectBuilder()
                                                                                                    .add("code", "W0015")
                                                                                                    .add("message", "Fecha de ejecución inválida").build()).build())
                                                                                    .build();
            }

            /*
            //Preguntamos el tipo de validacion
            //si es un pago de servicio
            if (bandera1) {
                //Guardamos el pago de servicio
                
                jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                        .add("fees", Json.createArrayBuilder().build())
                        .add("executionDate", fechaEjecucion)
                        .build();
                return Response.status(Response.Status.OK).entity(jsonResponse).build();

//Si es una transferencia normal
            } else if (bandera2) {
                System.out.println("Entro a transferencias");
                //Si es una programada
                System.out.println("FechaActual:" + FechaTiempoReal + ",FechaEjecucion:" + fechaEjecucion);
                if (!FechaTiempoReal.equals(fechaEjecucion)) {
                    System.out.println("Entro a programadas:" + customerId);
                    //Guardamos la validacion
                   // dto = dao.validateMonetaryInstruction(customerId, tipoTranferencia, cuentaOrigen, cuentaDestino, monto, comentario, "", fechaEjecucion, tipoEjecucion);
                    jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                            .add("fees", Json.createArrayBuilder().build())
                            .add("executionDate", fechaEjecucion)
                            .build();
                    return Response.status(Response.Status.OK).entity(jsonResponse).build();
                } else {//Si es una normal
                    System.out.println("Entro a las de tiempo real");
                   // dto = dao.validateMonetaryInstruction(customerId, tipoTranferencia, cuentaOrigen, cuentaDestino, monto, comentario, "", fechaEjecucion, tipoEjecucion);
                    jsonResponse = Json.createObjectBuilder().add("validationId", dto.getValidationId())
                            .add("fees", Json.createArrayBuilder().build())
                            .add("executionDate", dto.getExecutionDate())
                            .build();
                    return Response.status(Response.Status.OK).entity(jsonResponse).build();
                }

            }*/
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
        }
        return Response.status(Response.Status.OK).entity(jsonResponse).build();
    }

    @POST
    @Path("/monetary/execute")
    /*@Path("/monetary/executar_transaccion")*/
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response executeMonetaryInstruction(String cadena, @HeaderParam("authorization") String authString) throws JSONException {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject request = new JSONObject(cadena);
        String validationId = "";
        JsonObject jsonb = new JsonObject();
        try {
            validationId = request.getString("validationId");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        System.out.println("validationId:" + validationId);

        JsonObject Error = new JsonObject();
        TransfersDAO dao = new TransfersDAO();

        if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }

        String msj = "";
        String validationProgramadas = "45KLF09612FGHCVXX";//Se puso esa cadena porque para programadas no almaceno datos en la base responde dummy y esa cadena se usa
        try {
            msj = dao.executeMonetaryInstruction(validationId);
            jsonb.put("status", msj);
            return Response.status(Response.Status.OK).entity(jsonb).build();
        } catch (Exception e) {
            System.out.println("Error en response:" + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("/monetary/details")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response MonetaryDetails(@HeaderParam("authorization") String authString, String cadena) throws JSONException {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JSONObject request = new JSONObject(cadena);

        JsonObject Error = new JsonObject();
        TransfersDAO dao = new TransfersDAO();

        if (!dao.actividad_horario()) {
            Error.put("ERROR", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA, HORA O CONTACTE A SU PROVEEEDOR");
            System.out.println("HORARIO ACTIVIDAD: " + Error);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error).build();
        }

        try {
            String validationId = request.getString("monetaryInstructionId");
            transferencias_completadas_siscoop transferencia = dao.detailsMonetary(validationId);
            javax.json.JsonObject jsonResponse = null;
            String numbers = transferencia.getCuentaorigen().substring(Math.max(0, transferencia.getCuentaorigen().length() - 4));
            System.out.println("Numbers:" + numbers);
            System.out.println("IDProduc" + transferencia.getCuentaorigen().substring(6, 11).contains("110"));
            String accountType = "";
            if (transferencia.getCuentaorigen().substring(6, 11).contains("110")) {
                accountType = "SAVINGS";
            }
            jsonResponse = Json.createObjectBuilder()
                    /*.add("property1",Json.createObjectBuilder()
                                        .add("value","Value")
                                        .add("valueType","string")
                                        .add("description","Description").build())*/
                    .add("details", Json.createObjectBuilder()
                            /*.add("property1",Json.createObjectBuilder()
                                        .add("value","Value")
                                        .add("valueType","string")
                                        .add("description","Description").build())*/
                            .add("monetaryInstructionId", String.valueOf(transferencia.getCustomerId()))
                            .add("customerId", transferencia.getCustomerId())
                            .add("originatorTransactionType", transferencia.getTipotransferencia())
                            .add("debitAccount", Json.createObjectBuilder()
                                    .add("accountId:", transferencia.getCuentaorigen())
                                    .add("accountNumber", transferencia.getCuentaorigen())
                                    .add("displayAccountNumber", "**************" + numbers)
                                    .add("accountType", accountType)
                                    //.add("valueType","string")
                                    /*.add("property1",Json.createObjectBuilder()
                                        .add("value","Value")
                                        .add("valueType","string")
                                        .add("description","Description").build())*/
                                    .build())
                            .add("creditAccount", Json.createObjectBuilder()
                                    .add("accountSchemaType", "internal")
                                    .add("accountNumber", transferencia.getCuentadestino())
                                    .add("accountType", accountType)
                                    .add("accountId:", transferencia.getCuentadestino())
                                    //.add("valueType","string")
                                    .build())
                            .add("monetary", Json.createObjectBuilder()
                                    .add("amount", Json.createObjectBuilder()
                                            .add("amount", transferencia.getMonto())
                                            .add("currencyCode", "MXN")
                                            .build())
                                    .add("execution", Json.createObjectBuilder()
                                            .add("executionType", "specific")
                                            .add("executionDate", dao.dateToString(transferencia.getFechaejecucion()).replace("/", "-"))
                                            .build())
                                    .add("frequency", Json.createObjectBuilder()
                                            .add("frequencyType", "none")
                                            .build())
                                    .add("fees", Json.createArrayBuilder())
                                    .add("nextExecution", Json.createObjectBuilder()
                                            .add("executionDate", dao.dateToString(transferencia.getFechaejecucion()).replace("/", "-"))
                                            .add("executionAmount", Json.createObjectBuilder()
                                                    .add("amount", transferencia.getMonto())
                                                    .add("currencyCode", "MXN")
                                                    .build())
                                            .build())
                                    /*.add("valueType","string")*/.build())
                            .build()
                    )
                    .build();

            return Response.status(Response.Status.OK).entity(jsonResponse).build();
        } catch (Exception e) {
            System.out.println("Error al obtener atributo json:" + e.getMessage());
        }
        return null;
    }

    @POST
    @Path("/monetary/instruction/history")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response MonetaryHistory(@HeaderParam("authorization") String auth, String cadena) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(auth)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            JSONObject jsonR = new JSONObject(cadena);
            String monetaryInstructionId = "";
            int page = 0, pageSize = 0;
            monetaryInstructionId = jsonR.getString("monetaryInstructionId");
            page = jsonR.getInt("page");
            pageSize = jsonR.getInt("pageSize");

            javax.json.JsonObject json = Json.createObjectBuilder().add("records", Json.createArrayBuilder().add(Json.createObjectBuilder()
                    .add("status", "pending")
                    .add("amount", Json.createObjectBuilder()
                            .add("amount", "10.0")
                            .add("currencyCode", "MXN")
                            .build())
                    .add("inputDate", "2021-05-12")
                    .add("executionDate", "2021-05-12")
                    .build()))
                    .build();

            return Response.status(Response.Status.OK).entity(json).build();

        } catch (Exception ex) {
            System.out.println("Error al crear json:" + ex.getMessage());
        }
        return null;

    }

    @POST
    @Path("/instructions/monetary/cancel/validate")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response MonetaryCancellation(@HeaderParam("authorization") String authString, String cadena) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            JSONObject jsonR = new JSONObject(cadena);
            String monetaryInstructionId = "";
            int page = 0, pageSize = 0;
            monetaryInstructionId = jsonR.getString("monetaryInstructionId");

            javax.json.JsonObject json = Json.createObjectBuilder().add("validationId", "340JKLWEDFGDGGDF")
                    .build();

            return Response.status(Response.Status.OK).entity(json).build();

        } catch (Exception ex) {
            System.out.println("Error al crear json:" + ex.getMessage());
        }
        return null;

    }

    @POST
    @Path("/monetary/cancel/execute")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response MonetaryCancellationE(@HeaderParam("Authorization") String authString, String cadena) {
        Security scr = new Security();
        if (!scr.isUserAuthenticated(authString)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            JSONObject jsonR = new JSONObject(cadena);
            String monetaryInstructionId = "";
            int page = 0, pageSize = 0;
            monetaryInstructionId = jsonR.getString("validationId");

            javax.json.JsonObject json = Json.createObjectBuilder().add("status", "completed")
                    .build();

            return Response.status(Response.Status.OK).entity(json).build();

        } catch (Exception ex) {
            System.out.println("Error al crear json:" + ex.getMessage());
        }
        return null;

    }

}
