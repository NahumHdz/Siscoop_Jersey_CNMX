/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.Util;

import DTO.opaDTO;
import com.fenoreste.rest.Entidades.Auxiliares;
import com.fenoreste.rest.Entidades.AuxiliaresPK;
import com.fenoreste.rest.Entidades.datos_temporales_alertas;
import com.fenoreste.rest.Entidades.e_Alerts;
import com.fenoreste.rest.Entidades.tipos_cuenta_siscoop;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author wilmer
 */
public class TimerBeepClock implements Runnable {

    public void run() {
        Toolkit.getDefaultToolkit().beep();
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("HH:mm:ss a");
        String hora = dateFormatLocal.format(new Date());

        //Eliminamos todos los PDF a las 7:00AM
        if (hora.replace(" ", "").equals("07:00:00AM")) {
            eliminarPorExtension(ruta(), "pdf");
            eliminarPorExtension(ruta(), "html");
            eliminarPorExtension(ruta(), "txt");
            System.out.println("ARCHIVOS PDF ELIMINADOS CORRECTAMENTE");
        }

        //Actualizar Hora Servidor ---------------***************************************************---SOLO PARA PRUEBAS---***************************************************---------------------
        /*if (hora.replace(" ", "").equals("00:01:00AM")) {
            try {
                actualizarFechaServidorBD();
            } catch (Exception e) {
                System.out.println("ERROR AL ACTUALIZAR LA FECHA Y HORA EN LA BASE");
            }
        }*/

        //Ejecutar Alertas
        if (hora.replace(" ", "").equals("08:00:00AM")) {
            try {
                ejecutarAlerta();
            } catch (Exception e) {
                System.out.println("ERROR AL EJECUTAR ALERTAS");
            }
        }
    }

    //Metodo para eliminar todos los PDF
    public static void eliminarPorExtension(String path, final String extension) {
        File[] archivos = new File(path).listFiles(new FileFilter() {
            public boolean accept(File archivo) {
                if (archivo.isFile()) {
                    return archivo.getName().endsWith('.' + extension);
                }
                return false;
            }
        });
        for (File archivo : archivos) {
            archivo.delete();
        }
    }

    //Cambio de Fecha y Hora de Base para las pruebas
    /*public void actualizarFechaServidorBD() {
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String fechaActual = dateFormatLocal.format(new Date());
        //Timestamp timestamp = Timestamp.valueOf(fechaActual);
        //System.out.println("ACTUALLLLLLLLLLLLLLLLLLLLLL: " + timestamp);
        EntityManager em = AbstractFacade.conexion();
        try {
            em.getTransaction().begin();
            em.createNativeQuery("UPDATE origenes SET fechatrabajo = '" + fechaActual + "'").setParameter("fecha", fechaActual).executeUpdate();
            em.getTransaction().commit();
            System.out.println("FECHA DE BASE ACTUALIZADA CORRECTAMENTE");
        } catch (Exception e) {
            System.out.println("ERROR AL ACTUALIZAR LA FECHA Y HORA EN LA BASE: " + e.getMessage());
        }
    }*/

    //Parao obtener la ruta del servidor
    public static String ruta() {
        String home = System.getProperty("user.home");
        String separador = System.getProperty("file.separator");
        String actualRuta = home + separador + "Banca" + separador;
        return actualRuta;
    }

    public void ejecutarAlerta() {
        //EntityManagerFactory emf = AbstractFacade.conexion();
        EntityManager em = AbstractFacade.conexion();
        String uri = "https://cnmuat.siscoop.mx:9943/alertsengine/api/alerts/event/create";
        String output = "";
        Utilidades util = new Utilidades();
        try {
            //Buscamos todas las alertas que esten registradas con estatus false
            String listaAlertas = "SELECT * FROM e_alertas WHERE alert_enviado = false";
            Query query = em.createNativeQuery(listaAlertas, e_Alerts.class);
            List<e_Alerts> ListaAlertas = query.getResultList();
            String accountNumber = "";

            //En la tabla que se usa para guardar datos temporales preparamos las alertas
            datos_temporales_alertas datos_a_procesar = new datos_temporales_alertas();

            //Obtenemos la fecha de trabajo
            String fechaTrabajo = "SELECT date(fechatrabajo) FROM origenes limit 1";
            Query queryOrigenes = em.createNativeQuery(fechaTrabajo);
            String fechaTrabajoReal = String.valueOf(queryOrigenes.getSingleResult());
            String fecha[] = fechaTrabajoReal.split("-");
            LocalDate fecha_trab = LocalDate.of(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[2]));
            System.out.println("FechaTrabajo: " + fecha_trab);
            System.out.println("Alertas a ejecutar:" + ListaAlertas.size());

            /*Corremos la lista de alertas que se han encontradas registradas y con estatus false
              y vamo a validar que cumplan con el codigo que traen para ver si ya se debe ejecutar*/
            if (ListaAlertas.size() > 0) {
                for (int i = 0; i < ListaAlertas.size(); i++) {
                    //en cada vuelta generamos un random
                    int numero = (int) (Math.random() * 100 + 1);
                    //Obtenemos el registro de alerta de la lista
                    e_Alerts alerta_validada = ListaAlertas.get(i);

                    //Balance BALANCE_BELOW (saldo por debajo)
                    if (alerta_validada.getAlertCode().toUpperCase().contains("BALANCE_BELOW")) {
                        //Obtenemos el saldo de la cuenta que esta solicitando ejecutar la alerta
                        opaDTO opa = util.opa(alerta_validada.getAccountId());
                        AuxiliaresPK aux_pk = new AuxiliaresPK(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar());
                        Auxiliares a = em.find(Auxiliares.class, aux_pk);

                        if (a.getSaldo().doubleValue() < alerta_validada.getMonto()) {
                            datos_a_procesar.setId(numero);
                            datos_a_procesar.setCustomerId(alerta_validada.getCustomerid());
                            datos_a_procesar.setAccountId(alerta_validada.getAccountId());
                            datos_a_procesar.setAlertCode(alerta_validada.getAlertCode());
                            datos_a_procesar.setMonto(alerta_validada.getMonto());
                            //Buscamos el tipo de cuenta
                            tipos_cuenta_siscoop productos_banca = em.find(tipos_cuenta_siscoop.class, opa.getIdproducto());
                            datos_a_procesar.setAccountType(productos_banca.getProducttypename());

                            em.getTransaction().begin();//Abres la transaccion
                            em.persist(datos_a_procesar);//Procesas datos
                            em.getTransaction().commit();//Cierras la transaccion
                        }
                    } //Balance BALANCE_ABOVE (saldo por encima)
                    else if (alerta_validada.getAlertCode().toUpperCase().contains("BALANCE_ABOVE")) {
                        //Obtenemos el saldo de la cuenta que esta solicitando ejecutar la alerta
                        opaDTO opa = util.opa(alerta_validada.getAccountId());
                        AuxiliaresPK aux_pk = new AuxiliaresPK(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar());
                        Auxiliares a = em.find(Auxiliares.class, aux_pk);

                        if (a.getSaldo().doubleValue() > alerta_validada.getMonto()) {
                            datos_a_procesar.setId(numero);
                            datos_a_procesar.setCustomerId(alerta_validada.getCustomerid());
                            datos_a_procesar.setAccountId(alerta_validada.getAccountId());
                            datos_a_procesar.setAlertCode(alerta_validada.getAlertCode());
                            datos_a_procesar.setMonto(alerta_validada.getMonto());
                            //Buscamos el tipo de cuenta
                            tipos_cuenta_siscoop productos_banca = em.find(tipos_cuenta_siscoop.class, opa.getIdproducto());
                            datos_a_procesar.setAccountType(productos_banca.getProducttypename());

                            em.getTransaction().begin();//Abres la transaccion
                            em.persist(datos_a_procesar);//Procesas datos
                            em.getTransaction().commit();//Cierras la transaccion
                        }
                    } //Balance LOAN_PAYMENT_DUE (3 dias antes del vencimiento de pago de crédito)
                    else if (alerta_validada.getAlertCode().toUpperCase().contains("LOAN_PAYMENT_DUE")) {
                        //Obtenemos el saldo de la cuenta que esta solicitando ejecutar la alerta
                        opaDTO opa = util.opa(alerta_validada.getAccountId());

                        String sai_auxiliar = "SELECT sai_auxiliar(" + opa.getIdorigenp() + "," + opa.getIdproducto() + "," + opa.getIdauxiliar() + ",'" + fecha_trab + "')";
                        System.out.println("PRESTAMO SAI_AUXILIAR: " + sai_auxiliar);
                        Query RsSai = em.createNativeQuery(sai_auxiliar);
                        String sai_aux = RsSai.getSingleResult().toString();
                        String[] parts = sai_aux.split("\\|");
                        List list = Arrays.asList(parts);
                        String fecha_sig_abon = list.get(10).toString();

                        String fecha_res = "";
                        String fecha_trabajo = "";

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime localDate = LocalDateTime.parse(fecha_sig_abon + " 00:00:00", dtf);//asi conviertes un string a local date time
                        fecha_res = String.valueOf(localDate.plusDays(-3));//asi le restas dias

                        LocalDateTime localDate2 = LocalDateTime.parse(fecha_trab + " 00:00:00", dtf);
                        fecha_trabajo = String.valueOf(localDate2);

                        System.out.println("PRESTAMO FECHA HOY: " + fecha_trabajo + " FECHA RESTA: " + fecha_res);

                        if (fecha_trabajo.equals(fecha_res)) {
                            datos_a_procesar.setId(numero);
                            datos_a_procesar.setCustomerId(alerta_validada.getCustomerid());
                            datos_a_procesar.setAccountId(alerta_validada.getAccountId());
                            datos_a_procesar.setAlertCode(alerta_validada.getAlertCode());
                            datos_a_procesar.setMonto(alerta_validada.getMonto());
                            //Buscamos el tipo de cuenta
                            tipos_cuenta_siscoop productos_banca = em.find(tipos_cuenta_siscoop.class, opa.getIdproducto());
                            datos_a_procesar.setAccountType(productos_banca.getProducttypename());

                            em.getTransaction().begin();//Abres la transaccion
                            em.persist(datos_a_procesar);//Procesas datos
                            em.getTransaction().commit();//Cierras la transaccion
                        } else {
                            System.out.println("YA PASO LA FECHA DE PAGO DEL PRESTAMO");
                        }
                    } //Balance TIME_DEPOSIT_MATURING (3 dias antes del vencimiento de una inversion)
                    else if (alerta_validada.getAlertCode().toUpperCase().contains("TIME_DEPOSIT_MATURING")) {
                        //Obtenemos el saldo de la cuenta que esta solicitando ejecutar la alerta
                        opaDTO opa = util.opa(alerta_validada.getAccountId());

                        String sai_auxiliar = "SELECT sai_auxiliar(" + opa.getIdorigenp() + "," + opa.getIdproducto() + "," + opa.getIdauxiliar() + ",'" + fecha_trab + "')";
                        System.out.println("INVERSION SAI_AUXILIAR: " + sai_auxiliar);
                        Query RsSai = em.createNativeQuery(sai_auxiliar);
                        String sai_aux = RsSai.getSingleResult().toString();
                        String[] parts = sai_aux.split("\\|");
                        List list = Arrays.asList(parts);
                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                        Date fechaDate = formato.parse(list.get(2).toString());
                        SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd");
                        String realDate = formato2.format(fechaDate);
                        String fecha_ven_inv = realDate.replace("/", "-");

                        String fecha_res = "";
                        String fecha_trabajo = "";

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime localDate = LocalDateTime.parse(fecha_ven_inv + " 00:00:00", dtf);//asi conviertes un string a local date time
                        fecha_res = String.valueOf(localDate.plusDays(-3));//asi le restas dias

                        LocalDateTime localDate2 = LocalDateTime.parse(fecha_trab + " 00:00:00", dtf);
                        fecha_trabajo = String.valueOf(localDate2);

                        System.out.println("INVERSION FECHA HOY: " + fecha_trabajo + " FECHA RESTA: " + fecha_res);

                        if (fecha_trabajo.equals(fecha_res)) {
                            datos_a_procesar.setId(numero);
                            datos_a_procesar.setCustomerId(alerta_validada.getCustomerid());
                            datos_a_procesar.setAccountId(alerta_validada.getAccountId());
                            datos_a_procesar.setAlertCode(alerta_validada.getAlertCode());
                            datos_a_procesar.setMonto(alerta_validada.getMonto());
                            //Buscamos el tipo de cuenta
                            tipos_cuenta_siscoop productos_banca = em.find(tipos_cuenta_siscoop.class, opa.getIdproducto());
                            datos_a_procesar.setAccountType(productos_banca.getProducttypename());

                            em.getTransaction().begin();//Abres la transaccion
                            em.persist(datos_a_procesar);//Procesas datos
                            em.getTransaction().commit();//Cierras la transaccion
                        } else {
                            System.out.println("YA PASO LA FECHA DEL VENCIMIENTO DE LA INVERSION");
                        }
                    }
                }
            } else {
                System.out.println("NINGUNA ALERTA A PROCESAR");
            }

            //Una ves que la tabla para las alertas que se deben ejecutar ya este llena 
            //Obtenemos esa lista
            List<datos_temporales_alertas> lista_datos_temporales = new ArrayList<>();
            try {
                String consulta_datos_temporales = "SELECT * FROM datos_temporales_alertas";
                Query query_datos_tmp = em.createNativeQuery(consulta_datos_temporales, datos_temporales_alertas.class);
                lista_datos_temporales = query_datos_tmp.getResultList();
            } catch (Exception e) {
                System.out.println("Error no hay alertas a procesar hoy");
            }

            if (lista_datos_temporales.size() > 0) {
                //Corremos la tabla donde estan las alertas a disparar 
                for (int x = 0; x < lista_datos_temporales.size(); x++) {
                    //Obtenemos el dato temporal
                    datos_temporales_alertas alerta_a_procesar = lista_datos_temporales.get(x);
                    LocalDateTime now = LocalDateTime.now();
                    String numbers = accountNumber.substring(alerta_a_procesar.getAccountId().length() - 4);
                    JSONObject json = new JSONObject();
                    json.put("alertCode", alerta_a_procesar.getAlertCode());
                    json.put("originatorCode", "OMNIA");
                    json.put("eventDate", now + "Z");
                    json.put("customerId", alerta_a_procesar.getCustomerId().trim());
                    json.put("accountNumber", alerta_a_procesar.getAccountId().trim());
                    json.put("accountType", alerta_a_procesar.getAccountType().trim().toUpperCase());
                    JSONObject amounts = new JSONObject();
                    amounts.put("amount", alerta_a_procesar.getMonto());
                    amounts.put("currencyCode", "MXN");
                    json.put("amount", amounts);
                    JSONArray arrayD = new JSONArray();
                    arrayD.put("SMS");
                    arrayD.put("PUSH");
                    arrayD.put("SECURE_MESSAGE");
                    arrayD.put("EMAIL");
                    json.put("deliveryChannels", arrayD);
                    JSONObject eventDateTime = new JSONObject();
                    eventDateTime.put("value", String.valueOf(now).substring(0, 10).replace("-", "/"));
                    eventDateTime.put("valueType", "string");
                    json.put("eventdatetime", eventDateTime);
                    JSONObject digitalBankingDpt = new JSONObject();
                    digitalBankingDpt.put("value", "digitalBankingDpt 1");
                    digitalBankingDpt.put("valueType", "string");
                    json.put("digitalBankingDpt", digitalBankingDpt);
                    JSONObject accountnickname = new JSONObject();
                    accountnickname.put("value", "*****" + numbers);
                    accountnickname.put("valueType", "string");
                    json.put("accountnickname", accountnickname);
                    JSONObject currency = new JSONObject();
                    currency.put("value", "MXN");
                    currency.put("valueType", "string");
                    json.put("currency", currency);

                    System.out.println("json_enviando_alert:" + json.toString());

                    System.out.println("Conectando ws de callBack.....");
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(6000);
                    conn.setReadTimeout(3000);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    //String request=" {\"clientBankIdentifiers\":[{\"value\":\"03020710217963\"}],\"productBankIdentifiers\":[{\"value\":\"0302070011027916986\"}]}";
                    OutputStream os = conn.getOutputStream();
                    os.write(json.toString().getBytes());
                    os.flush();
                    int codigoHTTP = conn.getResponseCode();
                    System.out.println("CodigoHTTP:" + codigoHTTP);
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    output = conn.getResponseMessage();
                    System.out.println("Output from Server .... \n");
                    if (codigoHTTP == 200) {
                        System.out.println("El codigo fue 200OK");
                        //Hasta aqui todo termino bien y eso significa que la alerta se envio correctamente entonces la eliminamos de la tabla
                        em.getTransaction().begin();
                        em.remove(alerta_a_procesar);   //Elimino el registro                     
                        em.getTransaction().commit();
                    }
                    conn.disconnect();
                }
            } else {
                System.out.println("NINGUNA ALERTA A ENVIAR");
            }
        } catch (Exception e) {
            em.close();
            //emf.close();
            System.out.println("Error al conectar a :" + e.getMessage());
        } finally {
            em.close();
            //emf.close();
        }
    }

    /*
        String request = "{\"alertCode\":\"" + alertCode + "\"," + "\n"
                + "\"originatorCode\":\"OMNIA\"," + "\n"
                + "\"eventDate\":\"" + nows + "\"," + "\n"
                + "\"customerId\":\"" + customerId + "\"," + "\n"
                + "\"accountNumber\":\"" + accountNumber + "\"," + "\n"
                + "\"accountType\":\"" + accountType + "\"," + "\n"
                + "\"amount\":" + "\n"
                + "{\"amount\":" + amount + "," + "\n"
                + "\"currencyCode\":\"MXN\"" + "\n"
                + "}," + "\n"
                + "\"deliveryChannels\":["
                + "\"SMS\"," + "\n"
                + "\"PUSH\"," + "\n"
                + "\"SECURE_MESSAGE\"," + "\n"
                + "\"EMAIL\"" + "\n"
                + "]," + "\n"
                + "\"eventdatetime\":{" + "\n"
                + "\"value\":\"" + "zxcxc" + "\"," + "\n"
                + "\"valueType\":\"string\"" + "\n"
                + "}," + "\n"
                + "\"digitalBankingDpt\":{" + "\n"
                + "\"value\":\"digitalBankingDpt 1\"," + "\n"
                + "\"valueType\":\"string\"" + "\n"
                + "}," + "\n"
                + "\"accountnickname\":{" + "\n"
                + "\"value\":\"********" + numbers + "\"," + "\n"
                + "\"valueType\":\"string\"" + "\n"
                + "}," + "\n"
                + "\"currency\":{" + "\n"
                + "\"value\":\"MXN\"," + "\n"
                + "\"valueType\":\"string\"" + "\n"
                + "}" + "\n"
                + "}";*/
 /*public static void main(String[] args) {
        ScheduledExecutorService scheduler
                = Executors.newSingleThreadScheduledExecutor();

        Runnable task = new TimerBeepClock();
        int initialDelay = 1;
        int periodicDelay = 1;
        scheduler.scheduleAtFixedRate(task, initialDelay, periodicDelay,TimeUnit.SECONDS);
    }*/

 /*public void envio_notificacion(String customerid) {
        EntityManagerFactory emf = AbstractFacade.conexion();
        EntityManager em = emf.createEntityManager();
        String uri = "https://cnmuat.siscoop.mx:9943/alertsengine/api/alerts/event/create";
        String output = "";
        Utilidades util = new Utilidades();
        try {
            //Buscamos si el socio tiene activado la alerta MONETARY_TRANSACTION_INSTRUCTION
            String listaAlertas = "SELECT * FROM e_alertas WHERE alertcode = 'MONETARY_TRANSACTION_INSTRUCTION' AND enabled = true AND customerid = '" + customerid + "'";
            Query query = em.createNativeQuery(listaAlertas, e_Alerts.class);
            List<e_Alerts> ListaAlertas = query.getResultList();
            String accountNumber = "";

            if (ListaAlertas.size() > 0) {
                LocalDateTime now = LocalDateTime.now();
                //String numbers = accountNumber.substring(alerta_a_procesar.getAccountId().length() - 4);
                JSONObject json = new JSONObject();
                json.put("alertCode", ListaAlertas.get(1));
                json.put("originatorCode", "OMNIA");
                json.put("eventDate", now + "Z");
                json.put("customerId", customerid);
                json.put("accountNumber", "");
                json.put("accountType", "");
                JSONObject amounts = new JSONObject();
                amounts.put("amount", 0);
                amounts.put("currencyCode", "MXN");
                json.put("amount", amounts);
                JSONArray arrayD = new JSONArray();
                arrayD.put("SMS");
                arrayD.put("PUSH");
                arrayD.put("SECURE_MESSAGE");
                arrayD.put("EMAIL");
                json.put("deliveryChannels", arrayD);
                JSONObject eventDateTime = new JSONObject();
                eventDateTime.put("value", String.valueOf(now).substring(0, 10).replace("-", "/"));
                eventDateTime.put("valueType", "string");
                json.put("eventdatetime", eventDateTime);
                JSONObject digitalBankingDpt = new JSONObject();
                digitalBankingDpt.put("value", "digitalBankingDpt 1");
                digitalBankingDpt.put("valueType", "string");
                json.put("digitalBankingDpt", digitalBankingDpt);
                JSONObject accountnickname = new JSONObject();
                accountnickname.put("value", ""); // "*****" + numbers
                accountnickname.put("valueType", "string");
                json.put("accountnickname", accountnickname);
                JSONObject currency = new JSONObject();
                currency.put("value", "MXN");
                currency.put("valueType", "string");
                json.put("currency", currency);

                System.out.println("json_enviando_alert:" + json.toString());

                System.out.println("Conectando ws de callBack.....");
                URL url = new URL(uri);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(3000);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                //String request=" {\"clientBankIdentifiers\":[{\"value\":\"03020710217963\"}],\"productBankIdentifiers\":[{\"value\":\"0302070011027916986\"}]}";
                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                int codigoHTTP = conn.getResponseCode();
                System.out.println("CodigoHTTP:" + codigoHTTP);
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                output = conn.getResponseMessage();
                System.out.println("Output from Server .... \n");
                if (codigoHTTP == 200) {
                    System.out.println("El codigo fue 200OK");
                    //Hasta aqui todo termino bien y eso significa que la alerta se envio correctamente entonces la eliminamos de la tabla
                    em.getTransaction().begin();
                    //em.remove(alerta_a_procesar);   //Elimino el registro
                    em.getTransaction().commit();
                }
                conn.disconnect();
            } else {
                System.out.println("ALERTA NO REGISTRADA");
            }
        } catch (Exception e) {
            em.close();
            emf.close();
            System.out.println("Error al conectar a :" + e.getMessage());
        } finally {
            em.close();
            emf.close();
        }
    }*/
}
