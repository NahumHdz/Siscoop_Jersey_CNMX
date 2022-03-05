/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.Dao;

import DTO.AccountHoldersDTO;
import DTO.validateMonetaryInstructionDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.Entidades.Auxiliares;
import com.fenoreste.rest.Entidades.tipos_cuenta_siscoop;
import com.fenoreste.rest.Entidades.transferencias_completadas_siscoop;
import com.fenoreste.rest.Entidades.validaciones_transferencias_siscoop;
import DTO.MonetaryInstructionDTO;
import DTO.OrderWsSPEI;
import DTO.ogsDTO;
import DTO.opaDTO;
import com.fenoreste.rest.Entidades.AuxiliaresPK;
import com.fenoreste.rest.Entidades.Persona;
import com.fenoreste.rest.Entidades.Procesa_pago_movimientos;
import com.fenoreste.rest.Entidades.Productos;
import com.fenoreste.rest.Entidades.Tablas;
import com.fenoreste.rest.Entidades.TablasPK;
import com.fenoreste.rest.Util.Utilidades;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Elliot
 */
public abstract class FacadeInstructions<T> {

    Utilidades util = new Utilidades();

    public FacadeInstructions(Class<T> entityClass) {

    }

    public List<MonetaryInstructionDTO> monetaryInistruction(String customerId, String fechaInicio, String fechaFinal) {
        EntityManager em = AbstractFacade.conexion();
        List<MonetaryInstructionDTO> listaMonetary = new ArrayList<>();

        try {
            String consulta = "SELECT * FROM e_transferenciassiscoop WHERE customerId='" + customerId + "' AND fechaejecucion BETWEEN '" + fechaInicio + "' AND '" + fechaFinal + "' AND UPPER(comentario1) LIKE '%PROGRAMA%'";
            System.out.println("consulta:" + consulta);
            Query query = em.createNativeQuery(consulta, transferencias_completadas_siscoop.class);

            List<transferencias_completadas_siscoop> ListaTransferencias = query.getResultList();//new ArrayList<transferencias_completadas_siscoop>();
            //ListaTransferencias = query.getResultList();

            for (int i = 0; i < ListaTransferencias.size(); i++) {
                MonetaryInstructionDTO dtoMonetary = new MonetaryInstructionDTO();
                dtoMonetary.setDebitAccount(ListaTransferencias.get(i).getCuentaorigen());
                dtoMonetary.setCreditAccount(ListaTransferencias.get(i).getCuentadestino());
                dtoMonetary.setExecutionDate(dateToString(ListaTransferencias.get(i).getFechaejecucion()));
                dtoMonetary.setMonto(ListaTransferencias.get(i).getMonto());
                int idproducto = Integer.parseInt(ListaTransferencias.get(i).getCuentaorigen().substring(6, 11));
                tipos_cuenta_siscoop tps = em.find(tipos_cuenta_siscoop.class, idproducto);
                dtoMonetary.setTypeNameId(tps.getProducttypename().trim().toUpperCase());
                dtoMonetary.setOriginatorTransactionType(ListaTransferencias.get(i).getTipotransferencia());
                dtoMonetary.setMonetaryId(Integer.parseInt(ListaTransferencias.get(i).getCustomerId()));
                listaMonetary.add(dtoMonetary);
            }
            System.out.println("ListaMonetary:" + listaMonetary);
        } catch (Exception e) {
            em.close();
        }
        em.close();
        return listaMonetary;

    }

    public validateMonetaryInstructionDTO validateMonetaryInstruction(String customerId,
            String cuentaorigen,
            String cuentadestino,
            Double montoTransferencia,
            String comentario,
            String propcuentadestino,
            String fechaejecucion,
            String tipoEjecucion,
            String tipoTransferencia, int identificadorTransferencia) {

        String mensageDinamico = "";

        validateMonetaryInstructionDTO validateMonetary = new validateMonetaryInstructionDTO();
        EntityManager em = AbstractFacade.conexion();
        //String[] fees = new String[0];
        String validationId = "";

        //String fxxe = String.valueOf(queryf.getSingleResult());
        Date hoy = new Date();
        validationId = RandomAlfa().toUpperCase();
        //si es una pago de servicio Nomas la guarda porque no se esta habilitado el servicio pero si debe validar que exista la cuenta origen y que tenga saldo
        if (identificadorTransferencia == 1) {//Transferencia entre mis cuentas
            mensageDinamico = validarTransferenciaEntreMisCuentas(customerId, cuentaorigen, montoTransferencia, cuentadestino);
        } else if (identificadorTransferencia == 2) {//Transferencia a terceros dentro de la entidad
            mensageDinamico = validarTransferenciasATercerosDE(customerId, cuentaorigen, montoTransferencia, cuentadestino);
        } else if (identificadorTransferencia == 3) {//Pago a prestamos dentro de la entidad
            mensageDinamico = validarPagoPrestamo(customerId, cuentaorigen, montoTransferencia, cuentadestino);
        } else if (identificadorTransferencia == 4) {//pago de servicios
            mensageDinamico = validarPagoServicio(customerId, cuentaorigen, montoTransferencia);
        } else if (identificadorTransferencia == 5) {//transferencias SPEI
            //mensageDinamico=validarTransferenciaSPEI(orden);
        } else if (identificadorTransferencia == 6) {
            mensageDinamico = validarTransferenciaEntreMisCuentas(customerId, cuentaorigen, montoTransferencia, cuentadestino);
        } else if (identificadorTransferencia == 7) {
            mensageDinamico = validarTransferenciasATercerosDE(customerId, cuentaorigen, montoTransferencia, cuentadestino);
        } else if (identificadorTransferencia == 8) {
            mensageDinamico = validarPagoPrestamo(customerId, cuentaorigen, montoTransferencia, cuentadestino);
        }

        System.out.println("Mensaje de validacion:" + mensageDinamico);
        try {
            if (mensageDinamico.toUpperCase().contains("EXITO")) {
                validaciones_transferencias_siscoop validacionesTransferencias = new validaciones_transferencias_siscoop();
                validacionesTransferencias.setValidationId(validationId);
                validacionesTransferencias.setTipotransferencia(tipoTransferencia);
                validacionesTransferencias.setTipoejecucion(tipoEjecucion);
                validacionesTransferencias.setMonto(montoTransferencia);
                validacionesTransferencias.setFechaejecucion(hoy);
                validacionesTransferencias.setEstatus(false);
                validacionesTransferencias.setCustomerId(customerId);
                validacionesTransferencias.setCuentaorigen(cuentaorigen);
                validacionesTransferencias.setCuentadestino(cuentadestino);
                validacionesTransferencias.setComentario1(comentario);
                validacionesTransferencias.setComentario2(comentario);
                
                validateMonetary.setValidationId(validacionesTransferencias.getValidationId());
                validateMonetary.setExecutionDate(fechaejecucion);

                em.getTransaction().begin();
                em.persist(validacionesTransferencias);
                em.getTransaction().commit();

                //transacction.setTipotransferencia(tipoejecucion);
            } else {
                validateMonetary.setValidationId(mensageDinamico);
                String fee[] = new String[2];
                validateMonetary.setFees(fee);
                validateMonetary.setExecutionDate("");
            }
        } catch (Exception e) {
            em.close();
            System.out.println("Error al generar validacion:" + e.getMessage());
            validateMonetary.setValidationId(e.getMessage());
            return validateMonetary;
        } finally {
            em.close();
        }

        return validateMonetary;
    }

    public String executeMonetaryInstruction(String validationId) {
        EntityManager em = AbstractFacade.conexion();
        String mensaje = "";
        try {
            //Buscamos la validacion guardada no ejecutada con el id que se nos proporciona
            String consulta = "SELECT * FROM v_transferenciassiscoop WHERE validationid='" + validationId + "' ORDER BY fechaejecucion DESC LIMIT 1";
            validaciones_transferencias_siscoop validacion_guardada = null;
            try {
                Query query = em.createNativeQuery(consulta, validaciones_transferencias_siscoop.class);
                validacion_guardada = (validaciones_transferencias_siscoop) query.getSingleResult();
            } catch (Exception e) {
                mensaje = "Id para validacion no existe";
                System.out.println("Error el id para validar ya no existe:" + e.getMessage());
            }

            if (mensaje.equals("")) {
                opaDTO opa = util.opa(validacion_guardada.getCuentaorigen());
                String running_balance = "SELECT saldo-" + validacion_guardada.getMonto() + " FROM auxiliares a WHERE "
                        + " a.idorigenp = " + opa.getIdorigenp() + " AND a.idproducto = " + opa.getIdproducto() + " AND a.idauxiliar = " + opa.getIdauxiliar();

                Query query1 = em.createNativeQuery(running_balance);
                Double saldo = Double.parseDouble(String.valueOf(query1.getSingleResult()));

                transferencias_completadas_siscoop ejecutar_transferencia = new transferencias_completadas_siscoop();
                ejecutar_transferencia.setCuentaorigen(validacion_guardada.getCuentaorigen());
                ejecutar_transferencia.setCuentadestino(validacion_guardada.getCuentadestino());
                ejecutar_transferencia.setTipotransferencia(validacion_guardada.getTipotransferencia());
                ejecutar_transferencia.setComentario1(validacion_guardada.getComentario1());
                ejecutar_transferencia.setComentario2(validacion_guardada.getComentario2());
                ejecutar_transferencia.setCustomerId(validacion_guardada.getCustomerId());
                ejecutar_transferencia.setFechaejecucion(validacion_guardada.getFechaejecucion());
                ejecutar_transferencia.setMonto(validacion_guardada.getMonto());
                ejecutar_transferencia.setTipoejecucion(validacion_guardada.getTipoejecucion());
                ejecutar_transferencia.setEstatus(true);
                ejecutar_transferencia.setRunningBalance(saldo);
                boolean banderaEstatusTransferencia = false;
                opaDTO opaD = util.opa(ejecutar_transferencia.getCuentadestino());
                //Obtengo los productos origen y destino
                //Origen

                String origenP = "SELECT * FROM auxiliares WHERE idorigenp=" + opa.getIdorigenp() + " AND idproducto=" + opa.getIdproducto() + " AND idauxiliar=" + opa.getIdauxiliar();
                Query queryOrigen = em.createNativeQuery(origenP, Auxiliares.class);
                Auxiliares aOrigen = (Auxiliares) queryOrigen.getSingleResult();

                //Destino
                if (validacion_guardada.getTipotransferencia().equals("BILL_PAYMENT")) {
                    System.out.println("PAGO SERVICIOSSSSSS");
                    mensaje = "completed";
                } else {

                    String destinoP = "SELECT * FROM auxiliares WHERE idorigenp=" + opaD.getIdorigenp() + " AND idproducto=" + opaD.getIdproducto() + " AND idauxiliar=" + opaD.getIdauxiliar();
                    Query queryDestino = em.createNativeQuery(destinoP, Auxiliares.class);
                    Auxiliares aDestino = (Auxiliares) queryDestino.getSingleResult();

                    //Obtengo el producto 
                    Productos prDestino = em.find(Productos.class, aDestino.getAuxiliaresPK().getIdproducto());

                    Procesa_pago_movimientos procesaDestino = new Procesa_pago_movimientos();

                    Procesa_pago_movimientos procesaOrigen = new Procesa_pago_movimientos();
                    //Obtener los datos para procesar la transaccion
                    long time = System.currentTimeMillis();
                    Timestamp timestamp = new Timestamp(time);
                    Query sesion = em.createNativeQuery("select text(pg_backend_pid())||'-'||trim(to_char(now(),'ddmmyy'))");
                    String sesionc = String.valueOf(sesion.getSingleResult());
                    int rn = (int) (Math.random() * 999999 + 1);
                    //Obtener HH:mm:ss.microsegundos

                    String fechaArray[] = timestamp.toString().substring(0, 10).split("-");
                    String fReal = fechaArray[2] + "/" + fechaArray[1] + "/" + fechaArray[0];
                    String referencia = String.valueOf(rn) + "" + ejecutar_transferencia.getCuentaorigen().substring(0, 5) + "" + ejecutar_transferencia.getCuentadestino().substring(0, 5) + fReal.replace("/", "");

                    //Leemos fechatrabajo e idusuario
                    String fechaTrabajo = "SELECT to_char(fechatrabajo,'yyyy-MM-dd HH:mm:ss') FROM ORIGENES LIMIT 1";
                    Query fechaTrabajo_ = em.createNativeQuery(fechaTrabajo);
                    String fechaTr_ = String.valueOf(fechaTrabajo_.getSingleResult());

                    TablasPK idusuarioPK = new TablasPK("bankingly_banca_movil", "usuario_banca_movil");
                    Tablas tbUsuario_ = em.find(Tablas.class, idusuarioPK);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDate = LocalDateTime.parse(fechaTr_, dtf);

                    //Timestamp hoyT=Timestamp.from(hoy);
                    //Insertamos a la tabla donde se obtienen los datos a procesar
                    //Origen
                    Timestamp ts = Timestamp.valueOf(localDate);
                    procesaOrigen.setAuxiliaresPK(aOrigen.getAuxiliaresPK());
                    procesaOrigen.setFecha(ts);
                    procesaOrigen.setIdusuario(Integer.parseInt(tbUsuario_.getDato1()));
                    procesaOrigen.setSesion(sesionc);
                    procesaOrigen.setReferencia(referencia + ts.toString().replace(" ", "").substring(10, 18).replace(":", ""));
                    procesaOrigen.setIdorigen(aOrigen.getIdorigen());
                    procesaOrigen.setIdgrupo(aOrigen.getIdgrupo());
                    procesaOrigen.setIdsocio(aOrigen.getIdsocio());
                    procesaOrigen.setCargoabono(0);
                    procesaOrigen.setMonto(ejecutar_transferencia.getMonto());
                    procesaOrigen.setIva(Double.parseDouble(aOrigen.getIva().toString()));
                    procesaOrigen.setTipo_amort(Integer.parseInt(String.valueOf(aOrigen.getTipoamortizacion())));

                    procesaOrigen.setSai_aux("");

                    //Guardamos la cuenta origen para la transferencia
                    em.getTransaction().begin();
                    em.persist(procesaOrigen);
                    em.getTransaction().commit();
                    em.clear();

                    //Destino
                    procesaDestino.setAuxiliaresPK(aDestino.getAuxiliaresPK());
                    procesaDestino.setFecha(ts);
                    procesaDestino.setIdusuario(Integer.parseInt(tbUsuario_.getDato1()));
                    procesaDestino.setSesion(sesionc);
                    procesaDestino.setReferencia(referencia + ts.toString().replace(" ", "").substring(10, 18).replace(":", ""));
                    procesaDestino.setIdorigen(aDestino.getIdorigen());
                    procesaDestino.setIdgrupo(aDestino.getIdgrupo());
                    procesaDestino.setIdsocio(aDestino.getIdsocio());
                    procesaDestino.setCargoabono(1);
                    procesaDestino.setMonto(ejecutar_transferencia.getMonto());
                    procesaDestino.setIva(Double.parseDouble(aDestino.getIva().toString()));
                    procesaDestino.setTipo_amort(Integer.parseInt(String.valueOf(aDestino.getTipoamortizacion())));
                    procesaDestino.setSai_aux("");

                    //Guardamos la cuenta destino para la transferencia
                    em.getTransaction().begin();
                    em.persist(procesaDestino);
                    em.getTransaction().commit();

                    //Ejecuto la distribucion del monto(Funciona final)
                    String procesar = "SELECT sai_bankingly_aplica_transaccion('" + fechaTr_.substring(0, 10) + "'," + procesaOrigen.getIdusuario() + ",'" + procesaOrigen.getSesion() + "','" + procesaOrigen.getReferencia() + "')";
                    System.out.println("Aplica Transaccion: " + procesar);
                    Query procesa_pago = em.createNativeQuery(procesar);
                    int respuestaProcesada = Integer.parseInt(String.valueOf(procesa_pago.getSingleResult()));

                    System.out.println("RespuestaProcesada:" + respuestaProcesada);

                    //Si la cuenta a la que se esta transfiriendo es un prestamo
                    if (prDestino.getTipoproducto() == 2) {
                        //Obtengo los datos(Seguro hipotecario,comisones cobranza,interes ect.)
                        String distribucion = "SELECT sai_bankingly_detalle_transaccion_aplicada('" + fechaTr_.substring(0, 10) + "'," + procesaOrigen.getIdusuario() + ",'" + procesaOrigen.getSesion() + "','" + procesaOrigen.getReferencia() + "')";
                        System.out.println("DistribucionConsulta:" + distribucion);
                        Query procesa_distribucion = em.createNativeQuery(distribucion);
                        String distribucionProcesada = String.valueOf(procesa_distribucion.getSingleResult());
                        System.out.println("Distribucion_Procesada:" + distribucionProcesada);
                        String ArrayDistribucion[] = distribucionProcesada.split("\\|");
                        //Retorno: Seguro hipotecario | Comision cobranza | IM | Iva IM | IO | Iva IO | A Capital
                        //Si es un prestamo tipo amortizacion 5 y tiene referencia de tipoporducto 5012,5011 si tiene seguro hipotecario
                        //Si es un tipoamortizacion 5 no tiene adelanto de intereses

                        // if(prDestino.getTipoproducto()==5){
                        /*mensajeBackendResult = "PAGO EXITOSO" + "\n"
                        + "SEGURO HIPOTECARIO    :" + ArrayDistribucion[0] + "\n "
                        + "COMISON COBRANZA      :" + ArrayDistribucion[1] + "\n "
                        + "INTERES MORATORIO     :" + ArrayDistribucion[2] + "\n "
                        + "IVA INTERES MORATORIO :" + ArrayDistribucion[3] + "\n"
                        + "INTERES ORDINARIO     :" + ArrayDistribucion[4] + "\n"
                        + "IVA INTERES ORDINARIO :" + ArrayDistribucion[5] + "\n"
                        + "CAPITAL               :" + ArrayDistribucion[5] + "\n"
                        + "ADELANTO DE INTERES   :" + ArrayDistribucion[6] + "\n";//Para adelanto de interese solo aplicaria para los productos configurados*/
 /*}else{
                        mensajeBackendResult="PAGO EXITOSO"  +"\n"+
                                         //"SEGURO HIPOTECARIO    :"+ArrayDistribucion[0]+"\n "+
                                         "COMISON COBRANZA      :"+ArrayDistribucion[1]+"\n "+
                                         "INTERES MORATORIO     :"+ArrayDistribucion[2]+"\n "+
                                         "IVA INTERES MORATORIO :"+ArrayDistribucion[3]+"\n"+
                                         "INTERES ORDINARIO     :"+ArrayDistribucion[4]+"\n"+
                                         "IVA INTERES ORDINARIO :"+ArrayDistribucion[5]+"\n"+
                                         "CAPITAL               :"+ArrayDistribucion[5]+"\n"+
                                         "ADELANTO DE INTERES   :"+ArrayDistribucion[6]+"\n";
                    }*/
                        //Query queryLimpiar = em.createNativeQuery("SELECT sai_bankingly_termina_transaccion (NULL,NULL,'" + procesaOrigen.getSesion() + "','" + procesaOrigen.getReferencia() + "')");
                        //int clean = Integer.parseInt(String.valueOf(queryLimpiar.getSingleResult()));
                    } else {
                        //mensajeBackendResult = "TRANSACCION EXITOSA";
                        System.out.println("Transaccion exitosa");
                        banderaEstatusTransferencia = true;
                        //Query queryLimpiar = em.createNativeQuery("SELECT sai_bankingly_termina_transaccion (NULL,NULL,'" + procesaOrigen.getSesion() + "'," + procesaOrigen.getReferencia() + "')");
                        //int clean=Integer.parseInt(String.valueOf(queryLimpiar.getSingleResult()));
                    }

                    if (respuestaProcesada == 2) {
                        banderaEstatusTransferencia = true;
                    }
                    if (banderaEstatusTransferencia) {
                        //Aplico la distribucion

                        String clean = "SELECT sai_bankingly_termina_transaccion('" + fechaTr_.substring(0, 10) + "'," + procesaOrigen.getIdusuario() + ",'" + procesaOrigen.getSesion() + "','" + procesaOrigen.getReferencia() + "')";
                        Query queryL = em.createNativeQuery(clean);
                        int registrosLimpiados = Integer.parseInt(String.valueOf(queryL.getSingleResult()));
                        System.out.println("Registros Limpiados con exito:" + registrosLimpiados);

                        em.getTransaction().begin();
                        validaciones_transferencias_siscoop val = em.find(validaciones_transferencias_siscoop.class, validacion_guardada.getValidationId());
                        em.remove(val);
                        em.getTransaction().commit();

                        em.getTransaction().begin();
                        em.persist(ejecutar_transferencia);
                        mensaje = "completed";
                        em.getTransaction().commit();

                        ////////ENVIO ALERTA
                        System.out.println("aqui termina biennnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
                        System.out.println("OGSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS: " + validacion_guardada.getCustomerId());
                        System.out.println("OPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA: " + validacion_guardada.getCuentaorigen());

                    }
                }
            } else {
                mensaje = "ERROR validationId no existe";
            }

        } catch (Exception e) {
            System.out.println("Error en execute:" + e.getMessage());
            em.close();
            em.getTransaction().rollback();
            return e.getMessage();
        } finally {
            em.close();
        }
        return mensaje;
    }

    public transferencias_completadas_siscoop detailsMonetary(String validationId) {
        EntityManager em = AbstractFacade.conexion();
        transferencias_completadas_siscoop transferencia = null;
        try {
            String consulta = "SELECT * FROM e_transferenciassiscoop WHERE id='" + validationId + "'";
            Query query = em.createNativeQuery(consulta, transferencias_completadas_siscoop.class);
            transferencia = (transferencias_completadas_siscoop) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error en buscar detalles una tranferencia programada:" + e.getMessage());
            em.close();
            return null;
        } finally {
            em.close();
        }
        return transferencia;
    }

    public List<AccountHoldersDTO> accountHolders(String accountId) {
        EntityManager em = AbstractFacade.conexion();
        List<AccountHoldersDTO> listaDTO = new ArrayList<AccountHoldersDTO>();
        opaDTO opa = util.opa(accountId);
        try {
            String consulta = "SELECT p.nombre||' '||p.appaterno||' '||p.apmaterno as nombre FROM auxiliares a "
                    + " INNER JOIN personas p USING(idorigen,idgrupo,idsocio)"
                    + " WHERE a.idorigenp = " + opa.getIdorigenp() + " AND a.idproducto = " + opa.getIdproducto() + " AND a.idauxiliar = " + opa.getIdauxiliar();
            Query query = em.createNativeQuery(consulta);
            System.out.println("Consulta:" + consulta);
            String nombre = (String) query.getSingleResult();
            System.out.println("Nombre:" + nombre);
            AccountHoldersDTO dto = null;
            if (!nombre.equals("")) {
                dto = new AccountHoldersDTO(nombre, "SOW");
            }
            listaDTO.add(dto);
            System.out.println("ListaDTO:" + listaDTO);

        } catch (Exception e) {
            em.close();
            System.out.println("Error:" + e.getMessage());
        } finally {
            em.close();
        }
        return listaDTO;
    }

    /*=============================== Validaciones =======================================*/
    public Date stringToDate(String cadena) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
        Date fechaDate = null;

        try {
            fechaDate = formato.parse(cadena);
        } catch (Exception ex) {
            System.out.println("Error fecha:" + ex.getMessage());
        }
        System.out.println("fechaDate:" + fechaDate);
        return fechaDate;
    }

    //Validar Trasnferencia entre mis cuentas
    private String validarTransferenciaEntreMisCuentas(String socio, String opaOrigen, Double montoTransferencia, String opaDestino) {
        EntityManager em = AbstractFacade.conexion();
        opaDTO opa_orig = util.opa(opaOrigen);
        opaDTO opa_dest = util.opa(opaDestino);
        String cuentaOrigen = "SELECT * FROM auxiliares a "
                + " WHERE a.idorigenp = " + opa_orig.getIdorigenp() + " AND a.idproducto = " + opa_orig.getIdproducto() + " AND a.idauxiliar = " + opa_orig.getIdauxiliar();

        String cuentaDestino = "SELECT * FROM auxiliares a "
                + " WHERE a.idorigenp = " + opa_dest.getIdorigenp() + " AND a.idproducto = " + opa_dest.getIdproducto() + " AND a.idauxiliar = " + opa_dest.getIdauxiliar();
        String mensage = "";

        try {
            Auxiliares ctaOrigen = null;
            boolean bOrigen = false;

            try {
                Query query = em.createNativeQuery(cuentaOrigen, Auxiliares.class);
                ctaOrigen = (Auxiliares) query.getSingleResult();
                bOrigen = true;
            } catch (Exception e) {
                System.out.println("No Existe Cuenta Origen");
                mensage = "NO EXISTE CUENTA ORIGEN";
                bOrigen = false;
            }
            String ogsCtaOrigen = String.format("%06d", ctaOrigen.getIdorigen()) + String.format("%02d", ctaOrigen.getIdgrupo()) + String.format("%06d", ctaOrigen.getIdsocio());

            if (bOrigen) {
                Double saldo = Double.parseDouble(ctaOrigen.getSaldo().toString());
                if (ogsCtaOrigen.equals(socio)) {
                    Productos prOrigen = em.find(Productos.class, ctaOrigen.getAuxiliaresPK().getIdproducto());
                    //si el producto no es un prestamo              
                    if (prOrigen.getTipoproducto() == 0) {//Falta regla de negocio si se permiten transferencias desde todas las cuentas
                        //Verifico el estatus de la cuenta origen
                        if (ctaOrigen.getEstatus() == 2) {
                            //Valido saldo minimo y maximo del producto
                            String vali_max_min = "";
                            vali_max_min = limite_saldo_max_min(socio, opaOrigen, 0, montoTransferencia);
                            System.out.println("VALIDACION SALDO MAXIMO_MINIMO: " + vali_max_min);
                            if (vali_max_min.equals("")) {
                                System.out.println("SI SE PUEDE REALIZAR");

                                //verifico que el saldo del producto origen es mayor o igual a lo que se intenta transferir
                                if (saldo >= montoTransferencia) {
                                    Auxiliares ctaDestino = null;
                                    boolean bDestino = false;
                                    //Busco la cuenta destino

                                    try {
                                        Query queryDestino = em.createNativeQuery(cuentaDestino, Auxiliares.class);
                                        ctaDestino = (Auxiliares) queryDestino.getSingleResult();
                                        bDestino = true;
                                    } catch (Exception e) {
                                        System.out.println("Error al encontrar productoDestino:" + e.getMessage());
                                        bDestino = false;
                                    }

                                    if (bDestino) {
                                        //Busco el producto destino
                                        Productos productoDestino = em.find(Productos.class, ctaDestino.getAuxiliaresPK().getIdproducto());
                                        //Valido que la cuenta destino este activa
                                        if (ctaDestino.getEstatus() == 2) {
                                            //Valido que el producto destino no sea un prestamo
                                            if (productoDestino.getTipoproducto() == 0) {//validar regla que todos los productos puedan recibir tranferencias excepto prestamos
                                                //Valido que realmente el producto destino pertenezca al mismo socio(porque es entre mis cuentas 
                                                if (ctaOrigen.getIdorigen() == ctaDestino.getIdorigen() && ctaOrigen.getIdgrupo() == ctaDestino.getIdgrupo() && ctaOrigen.getIdsocio() == ctaDestino.getIdsocio()) {
                                                    //valido el minimo y maximo permitido para una transferencia
                                                    /*if (minMax(montoTransferencia).toUpperCase().contains("VALIDO")) {
                                                        if (MaxPordia(opaOrigen, montoTransferencia)) {*/
                                                    mensage = "validado con exito";
                                                    /*} else {
                                                            mensage = "MONTO TRASPASA EL PERMITIDO DIARIO";
                                                        }
                                                    } else {
                                                        mensage = "EL MONTO QUE INTENTA TRANSFERIR ES:" + minMax(montoTransferencia) + " AL PERMITIDO";
                                                    }*/
                                                } else {
                                                    mensage = "PRODUCTO DESTINO NO PERTENECE AL MISMO SOCIO";
                                                }
                                            } else if (productoDestino.getTipoproducto() == 2) {
                                                if (ctaOrigen.getIdorigen() == ctaDestino.getIdorigen() && ctaOrigen.getIdgrupo() == ctaDestino.getIdgrupo() && ctaOrigen.getIdsocio() == ctaDestino.getIdsocio()) {
                                                    mensage = "validado con exito";
                                                } else {
                                                    mensage = "PRODUCTO DESTINO NO PERTENECE AL MISMO SOCIO";
                                                }
                                            } else {
                                                mensage = "PRODUCTO DESTINO NO ACEPTA SOBRECARGOS";
                                            }

                                        } else {
                                            mensage = "PRODUCTO DESTINO ESTA INACTIVA";
                                        }
                                    } else {
                                        mensage = "NO SE ENCONTRO PRODUCTO DESTINO";
                                    }
                                } else {
                                    mensage = "FONDOS INSUFICIENTES PARA COMPLETAR LA TRANSACCION";
                                }
                            } else {
                                mensage = vali_max_min;
                                System.out.println("NO SE PUEDE REALIZAR RELGAS DE NEGOCIO APLICADAS");
                            }
                        } else {
                            mensage = "PRODUCTO ORIGEN INACTIVO";
                        }
                    } else {
                        mensage = "PRODUCTO ORIGEN NO PERMITE SOBRECARGOS";
                    }
                } else {
                    mensage = "CUENTA ORIGEN NO PERTENECE AL SOCIO:" + socio;
                }
            } else {
                mensage = "CUENTA ORIGEN NO EXISTE";
            }

        } catch (Exception e) {
            System.out.println("Error al validar transferencia entre mis cuentas:" + e.getMessage());
            if (!mensage.equals("NO EXISTE CUENTA ORIGEN")) {
                mensage = e.getMessage();
            }
            em.close();
            return mensage;
        } finally {
            em.close();
        }

        return mensage.toUpperCase();
    }

    //Validar Transferencia a terceros dentro de la entidad
    private String validarTransferenciasATercerosDE(String socio, String opaOrigen, Double montoTransferencia, String opaDestino) {
        EntityManager em = AbstractFacade.conexion();
        opaDTO opa_orig = util.opa(opaOrigen);
        opaDTO opa_dest = util.opa(opaDestino);

        String cuentaOrigen = "SELECT * FROM auxiliares a "
                + " WHERE a.idorigenp = " + opa_orig.getIdorigenp() + " AND a.idproducto = " + opa_orig.getIdproducto() + " AND a.idauxiliar = " + opa_orig.getIdauxiliar();

        String cuentaDestino = "SELECT * FROM auxiliares a "
                + " WHERE a.idorigenp = " + opa_dest.getIdorigenp() + " AND a.idproducto = " + opa_dest.getIdproducto() + " AND a.idauxiliar = " + opa_dest.getIdauxiliar();

        String mensage = "";
        try {
            Auxiliares ctaOrigen = null;
            boolean bOrigen = false;

            try {
                Query query = em.createNativeQuery(cuentaOrigen, Auxiliares.class);
                ctaOrigen = (Auxiliares) query.getSingleResult();
                bOrigen = true;
            } catch (Exception e) {
                System.out.println("No Existe Cuenta Origen");
                mensage = "NO EXISTE CUENTA ORIGEN";
                bOrigen = false;
            }

            if (bOrigen) {
                Double saldo = Double.parseDouble(ctaOrigen.getSaldo().toString());

                String ogsCtaOrigen = String.format("%06d", ctaOrigen.getIdorigen()) + String.format("%02d", ctaOrigen.getIdgrupo()) + String.format("%06d", ctaOrigen.getIdsocio());
                if (ogsCtaOrigen.equals(socio)) {
                    Productos prOrigen = em.find(Productos.class, ctaOrigen.getAuxiliaresPK().getIdproducto());
                    //si el producto no es un prestamo
                    if (prOrigen.getTipoproducto() == 0) {//Falta regla de negocio si se permiten transferencias desde todas las cuentas
                        //Verifico el estatus de la cuenta origen
                        if (ctaOrigen.getEstatus() == 2) {
                            //Valido saldo minimo y maximo del producto
                            String vali_max_min = "";
                            vali_max_min = limite_saldo_max_min(socio, opaOrigen, 0, montoTransferencia);
                            System.out.println("VALIDACION SALDO MAXIMO_MINIMO: " + vali_max_min);
                            if (vali_max_min.equals("")) {
                                System.out.println("SI SE PUEDE REALIZAR");

                                //verifico que el saldo del producto origen es mayor o igual a lo que se intenta transferir
                                if (saldo >= montoTransferencia) {
                                    Auxiliares ctaDestino = null;
                                    boolean bDestino = false;
                                    //Busco la cuenta destino

                                    try {
                                        Query queryDestino = em.createNativeQuery(cuentaDestino, Auxiliares.class);
                                        ctaDestino = (Auxiliares) queryDestino.getSingleResult();
                                        bDestino = true;
                                    } catch (Exception e) {
                                        System.out.println("Error al encontrar productoDestino:" + e.getMessage());
                                        bDestino = false;
                                    }

                                    if (bDestino) {
                                        //Busco el producto destino
                                        Productos productoDestino = em.find(Productos.class, ctaDestino.getAuxiliaresPK().getIdproducto());
                                        //Valido que la cuenta destino este activa
                                        if (ctaDestino.getEstatus() == 2) {
                                            //Valido que el producto destino no sea un prestamo
                                            if (productoDestino.getTipoproducto() == 0 || productoDestino.getTipoproducto() == 2) {//validar regla que todos los productos puedan recibir tranferencias excepto prestamos
                                                //Valido saldo maximo del producto
                                                String vali_maxmin = "";
                                                String o_d = String.format("%06d", ctaDestino.getIdorigen());
                                                String g_d = String.format("%02d", ctaDestino.getIdgrupo());
                                                String s_d = String.format("%06d", ctaDestino.getIdsocio());
                                                String socio_destino = o_d + g_d + s_d;

                                                vali_maxmin = limite_saldo_max_min(socio_destino, opaDestino, 1, montoTransferencia);
                                                System.out.println("VALIDACION SALDO MAXIMO: " + vali_maxmin);
                                                if (vali_maxmin.equals("")) {
                                                    System.out.println("SI SE PUEDE REALIZAR TRANSFERENCIA A TERCERO");

                                                    //valido el minimo y maximo permitido para una transferencia
                                                    /*if (minMax(montoTransferencia).toUpperCase().contains("VALIDO")) {
                                                        if (MaxPordia(opaOrigen, montoTransferencia)) {*/
                                                    mensage = "validado con exito";
                                                    /*} else {
                                                            mensage = "MONTO TRASPASA EL PERMITIDO DIARIO";
                                                        }
                                                    } else {
                                                        mensage = "EL MONTO QUE INTENTA TRANSFERIR ES:" + minMax(montoTransferencia) + " AL PERMITIDO";
                                                    }*/
                                                } else {
                                                    mensage = vali_maxmin.replace("-", "");
                                                    System.out.println("NO SE PUEDE REALIZAR TRANSFERENCIA A TERCERO");
                                                }
                                            } else {
                                                mensage = "PRODUCTO DESTINO NO ACEPTA SOBRECARGOS";
                                            }
                                        } else {
                                            mensage = "PRODUCTO DESTINO ESTA INACTIVA";
                                        }
                                    } else {
                                        mensage = "NO SE ENCONTRO PRODUCTO DESTINO";
                                    }

                                } else {
                                    mensage = "FONDOS INSUFICIENTES PARA COMPLETAR LA TRANSACCION";
                                }
                            } else {
                                mensage = vali_max_min;
                                System.out.println("NO SE PUEDE REALIZAR");
                            }
                        } else {
                            mensage = "PRODUCTO ORIGEN INACTIVO";
                        }
                    } else {
                        mensage = "PRODUCTO ORIGEN NO PERMITE SOBRECARGOS";
                    }
                } else {
                    mensage = "CUENTA DESTINO NO PERTENECE AL SOCIO:" + socio;
                }
            } else {
                mensage = "CUENTA ORIGEN NO EXISTE";
            }

        } catch (Exception e) {
            em.close();
            System.out.println("Error al realizar transferencia a tercero:" + e.getMessage());
            if (!mensage.contains("CUENTA ORIGEN")) {
                mensage = e.getMessage();
            }
            return mensage;
        } finally {
            em.close();
        }

        return mensage.toUpperCase();
    }

    //Validar pago de prestamo propio
    private String validarPagoPrestamo(String socio, String opaOrigen, Double montoTransferencia, String opaDestino) {
        EntityManager em = AbstractFacade.conexion();
        opaDTO opa_orig = util.opa(opaOrigen);
        opaDTO opa_dest = util.opa(opaDestino);
        String cuentaOrigen = "SELECT * FROM auxiliares a "
                + " WHERE a.idorigenp = " + opa_orig.getIdorigenp() + " AND a.idproducto = " + opa_orig.getIdproducto() + " AND a.idauxiliar = " + opa_orig.getIdauxiliar();

        String cuentaDestino = "SELECT * FROM auxiliares a "
                + " WHERE a.idorigenp = " + opa_dest.getIdorigenp() + " AND a.idproducto = " + opa_dest.getIdproducto() + " AND a.idauxiliar = " + opa_dest.getIdauxiliar();

        String mensage = "";
        try {
            Auxiliares ctaOrigen = null;
            boolean bOrigen = false;
            try {
                Query query = em.createNativeQuery(cuentaOrigen, Auxiliares.class);
                ctaOrigen = (Auxiliares) query.getSingleResult();
                bOrigen = true;
            } catch (Exception e) {
                System.out.println("No Existe Cuenta Origen");
                mensage = " NO EXISTE LA CUENTA ORIGEN";
                bOrigen = false;
            }

            if (bOrigen) {
                Double saldo = Double.parseDouble(ctaOrigen.getSaldo().toString());
                String ogsCtaOrigen = String.format("%06d", ctaOrigen.getIdorigen()) + String.format("%02d", ctaOrigen.getIdgrupo()) + String.format("%06d", ctaOrigen.getIdsocio());
                if (ogsCtaOrigen.equals(socio)) {
                    Productos prOrigen = em.find(Productos.class, ctaOrigen.getAuxiliaresPK().getIdproducto());
                    //si el producto no es un prestamo
                    if (prOrigen.getTipoproducto() == 0) {//Falta regla de negocio si se permiten transferencias desde todas las cuentas
                        //Verifico el estatus de la cuenta origen
                        if (ctaOrigen.getEstatus() == 2) {
                            //Valido saldo minimo y maximo del producto
                            String vali_max_min = "";
                            vali_max_min = limite_saldo_max_min(socio, opaOrigen, 0, montoTransferencia);
                            System.out.println("VALIDACION SALDO MAXIMO_MINIMO: " + vali_max_min);
                            if (vali_max_min.equals("")) {
                                System.out.println("SI SE PUEDE REALIZAR EL PAGO A PRESTAMO");

                                //verifico que el saldo del producto origen es mayor o igual a lo que se intenta transferir
                                if (saldo >= montoTransferencia) {
                                    Auxiliares ctaDestino = null;
                                    boolean bDestino = false;
                                    //Busco la cuenta destino
                                    try {
                                        Query queryDestino = em.createNativeQuery(cuentaDestino, Auxiliares.class);
                                        ctaDestino = (Auxiliares) queryDestino.getSingleResult();
                                        bDestino = true;
                                    } catch (Exception e) {
                                        System.out.println("Error al encontrar productoDestino:" + e.getMessage());
                                        bDestino = false;
                                    }
                                    if (bDestino) {
                                        //Busco el producto destino
                                        Productos productoDestino = em.find(Productos.class, ctaDestino.getAuxiliaresPK().getIdproducto());
                                        //Valido que la cuenta destino este activa
                                        if (ctaDestino.getEstatus() == 2) {
                                            //Valido que el producto destino sea un prestamo
                                            if (productoDestino.getTipoproducto() == 2) {//validar regla que todos los productos puedan recibir tranferencias excepto prestamos
                                                //Valido que realmente el producto destino pertenezca al mismo socio(porque es entre mis cuentas 
                                                if (ctaOrigen.getIdorigen() == ctaDestino.getIdorigen() && ctaOrigen.getIdgrupo() == ctaDestino.getIdgrupo() && ctaOrigen.getIdsocio() == ctaDestino.getIdsocio()) {
                                                    //Valido que el monto a transferir sea menor o igual al saldo del prestamo
                                                    Double Saldo_Destino = Double.valueOf(String.valueOf(ctaDestino.getSaldo()));
                                                    if (montoTransferencia <= Saldo_Destino) {
                                                        //valido el minimo y maximo permitido para una transferencia
                                                        /*if (minMax(montoTransferencia).toUpperCase().contains("VALIDO")) {
                                                            if (MaxPordia(opaOrigen, montoTransferencia)) {*/
                                                        mensage = "validado con exito";
                                                        /*} else {
                                                                mensage = "MONTO TRASPASA EL PERMITIDO DIARIO";
                                                            }
                                                        } else {
                                                            mensage = "EL MONTO QUE INTENTA TRANSFERIR ES: " + minMax(montoTransferencia) + " AL PERMITIDO";
                                                        }*/
                                                    } else {
                                                        mensage = "EL MONTO DE TRANSFERENCIA ES MAYOR AL SALDO DEL PRESTAMO";
                                                    }
                                                } else {
                                                    mensage = "PRODUCTO DESTINO NO PERTENECE AL MISMO SOCIO";
                                                }
                                            } else {
                                                mensage = "PRODUCTO DESTINO NO ES UN PRESTAMO";
                                            }
                                        } else {
                                            mensage = "PRODUCTO DESTINO ESTA INACTIVA";
                                        }
                                    } else {
                                        mensage = "NO SE ENCONTRO PRODUCTO DESTINO";
                                    }
                                } else {
                                    mensage = "FONDOS INSUFICIENTES PARA COMPLETAR LA TRANSACCION";
                                }
                            } else {
                                mensage = vali_max_min;
                                System.out.println("NO SE PUEDE REALIZAR EL PAGO A PRESTAMO");
                            }
                        } else {
                            mensage = "PRODUCTO ORIGEN INACTIVO";
                        }
                    } else {
                        mensage = "PRODUCTO ORIGEN NO PERMITE SOBRECARGOS";
                    }
                } else {
                    mensage = "CUENTA ORIGEN NO PERTENCE AL SOCIO:" + socio;
                }
            } else {
                mensage = "CUENTA ORIGEN NO EXISTE";
            }

        } catch (Exception e) {
            em.close();
            System.out.println("Error al realizar pago de prestamo:" + e.getMessage());
            if (!mensage.contains("CUENTA ORIGEN")) {
                mensage = e.getMessage();
            }
            return mensage;
        } finally {
            em.close();
        }

        return mensage.toUpperCase();
    }

    //Validar pago de servicio(solo se valida la cuenta origen)
    private String validarPagoServicio(String socio, String opaOrigen, Double TotalPagoServicio) {
        EntityManager em = AbstractFacade.conexion();
        opaDTO opa_orig = util.opa(opaOrigen);
        String cuentaOrigen = "SELECT * FROM auxiliares a "
                + " WHERE a.idorigenp = " + opa_orig.getIdorigenp() + " AND a.idproducto = " + opa_orig.getIdproducto() + " AND a.idauxiliar = " + opa_orig.getIdauxiliar();

        String mensage = "";
        try {
            Auxiliares ctaOrigen = null;
            boolean bOrigen = false;
            try {
                Query query = em.createNativeQuery(cuentaOrigen, Auxiliares.class);
                ctaOrigen = (Auxiliares) query.getSingleResult();
                bOrigen = true;
            } catch (Exception e) {
                System.out.println("No Existe Cuenta Origen");
                mensage = "NO EXISTE CUENTA ORIGEN";
                bOrigen = false;
            }

            if (bOrigen) {
                Double saldo = Double.parseDouble(ctaOrigen.getSaldo().toString());
                String ogsCtaOrigen = String.format("%06d", ctaOrigen.getIdorigen()) + String.format("%02d", ctaOrigen.getIdgrupo()) + String.format("%06d", ctaOrigen.getIdsocio());
                if (ogsCtaOrigen.equals(socio)) {
                    Productos prOrigen = em.find(Productos.class, ctaOrigen.getAuxiliaresPK().getIdproducto());
                    //si el producto no es un prestamo
                    if (prOrigen.getTipoproducto() == 0) {//Falta regla de negocio si se permiten transferencias desde todas las cuentas
                        //Verifico el estatus de la cuenta origen
                        if (ctaOrigen.getEstatus() == 2) {
                            //verifico que el saldo del producto origen es mayor o igual a lo que se intenta transferir
                            if (saldo >= TotalPagoServicio) {
                                //valido el minimo y maximo permitido para una transferencia
                                /*if (minMax(TotalPagoServicio).toUpperCase().contains("VALIDO")) {
                                    if (MaxPordia(opaOrigen, TotalPagoServicio)) {*/
                                mensage = "validado con exito";
                                /*} else {
                                        mensage = "MONTO TRASPASA EL PERMITIDO DIARIO";
                                    }
                                } else {
                                    mensage = "EL MONTO QUE INTENTA TRANSFERIR ES:" + minMax(TotalPagoServicio) + " AL PERMITIDO";
                                }*/
                            } else {
                                mensage = "FONDOS INSUFICIENTES PARA COMPLETAR LA TRANSACCION";
                            }
                        } else {
                            mensage = "PRODUCTO ORIGEN INACTIVO";
                        }
                    } else {
                        mensage = "PRODUCTO ORIGEN NO PERMITE SOBRECARGOS";
                    }
                } else {
                    mensage = "CUENTA ORIGEN NO PERTENCE AL SOCIO:" + socio;
                }
            } else {
                mensage = "CUENTA ORIGEN NO EXISTE";
            }

        } catch (Exception e) {
            em.close();
            System.out.println("Error al realizar pago de prestamo:" + e.getMessage());
            if (!mensage.contains("CUENTA ORIGEN")) {
                mensage = e.getMessage();
            }
            return mensage;
        } finally {
            em.close();
        }

        return mensage.toUpperCase();
    }

    public validateMonetaryInstructionDTO validacionesOrdenSPEI(OrderWsSPEI orden) {
        EntityManager em = AbstractFacade.conexion();
        try {
            String validationId = "";
            //Validamos que exista el solicitante
            if (validarTransferenciaSPEI(orden).toUpperCase().contains("EXITO")) {
                validationId = RandomAlfa().toUpperCase();
            } else {
                validationId = validarTransferenciaSPEI(orden);
            }
        } catch (Exception e) {

        }

        return null;

    }

    //Valida el monto maximo permitido por dia
    public boolean MaxPordia(String opa, Double montoI) {
        EntityManager em = AbstractFacade.conexion();
        Calendar c1 = Calendar.getInstance();
        String dia = Integer.toString(c1.get(5));
        String mes = Integer.toString(c1.get(2) + 1);
        String annio = Integer.toString(c1.get(1));
        String fechaActual = String.format("%04d", Integer.parseInt(annio)) + "/" + String.format("%02d", Integer.parseInt(mes)) + "/" + String.format("%02d", Integer.parseInt(dia));
        TablasPK tbPk = new TablasPK("bankingly_banca_movil", "montomaximo");
        Tablas tb = em.find(Tablas.class,
                tbPk);
        try {
            //Busco el total de monto de transferencias por dia
            String consultaTransferencias = "SELECT sum(monto) FROM e_transferenciassiscoop WHERE"
                    + " cuentaorigen='" + opa + "' AND to_char(date(fechaejecucion),'yyyy/MM/dd')='" + fechaActual + "'";
            Query query = em.createNativeQuery(consultaTransferencias);
            String montoObtenidodb = "";
            if (query.getSingleResult() != null) {
                montoObtenidodb = String.valueOf(query.getSingleResult());
            } else {
                montoObtenidodb = "0";
            }
            Double monto = Double.parseDouble(String.valueOf(montoObtenidodb)) + montoI;
            System.out.println("monto:" + monto);
            if (monto <= Double.parseDouble(tb.getDato1())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            em.close();
            System.out.println("Error al validar permitido diario:" + e.getMessage());
        } finally {
            em.close();
        }
        return false;
    }

    public String dateToString(Date cadena) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String cadenaStr = sdf.format(cadena);
        return cadenaStr;
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

    public String validarTransferenciaSPEI(OrderWsSPEI orden) {
        EntityManager em = AbstractFacade.conexion();
        ogsDTO ogs_or = util.ogs(orden.getCIF());
        opaDTO opa_or = util.opa(orden.getClabeSolicitante());
        String mensaje = "";
        try {
            String busquedaSolicitante = "SELECT * FROM personas WHERE idorigen = " + ogs_or.getIdorigen()
                    + " AND idgrupo = " + ogs_or.getIdgrupo() + " AND idsocio = " + ogs_or.getIdsocio();
            Query queryBusquedaSolicitante = em.createNativeQuery(busquedaSolicitante, Persona.class);
            Persona p = (Persona) queryBusquedaSolicitante.getSingleResult();
            String cuentaOrigen = "SELECT * FROM auxiliares a WHERE idorigenp = " + opa_or.getIdorigenp()
                    + " AND idproducto = " + opa_or.getIdproducto() + " AND idauxiliar = " + opa_or.getIdauxiliar();
            if (p != null) {
                Query queryOrigen = em.createNativeQuery(cuentaOrigen, Auxiliares.class);
                Auxiliares a = (Auxiliares) queryOrigen.getSingleResult();
                if (a != null) {
                    //Validamos que pertenezca al socio
                    String opa = String.format("%06d", a.getIdorigen()) + String.format("%02d", a.getIdgrupo()) + String.format("%06d", a.getIdsocio());
                    if (opa.equals(orden.getClabeSolicitante())) {
                        //Validamos el estatus
                        if (a.getEstatus() == 2) {
                            //Validar el tipo de producto
                            Productos pr = em.find(Productos.class, a.getAuxiliaresPK().getIdproducto());
                            if (pr.getTipoproducto() != 2) {
                                //Solo validado para ahorro solo faltaria para inversion
                                if (pr.getTipoproducto() == 1) {

                                } else if (pr.getTipoproducto() == 0) {
                                    if (Double.parseDouble(a.getSaldo().toString()) >= orden.getMonto()) {
                                        //Validare el maximo para banca movil
                                        mensaje = "EXITOSO";
                                    } else {
                                        mensaje = "Fondos insuficientes";
                                    }
                                }
                            } else {
                                mensaje = "No se puede transferir de un prestamo";
                            }
                        } else {
                            mensaje = "La cuenta esta inactiva";
                        }
                    } else {
                        mensaje = "La cuenta no pertenece al socio";
                    }
                } else {
                    mensaje = "Cuenta no existe";
                }
            } else {
                mensaje = "Socio no existe";
            }
        } catch (Exception e) {
            mensaje = e.getMessage();
            System.out.println("Error en procesar la validacion:" + e.getMessage());
            return mensaje;
        }
        return mensaje;
    }

    //Solo aplica para cnmx
    public boolean validarSaldosMinimoProducto(AuxiliaresPK auxPK, Double monto) {
        EntityManager em = AbstractFacade.conexion();
        /*try {
        Auxiliares a=em.find(Auxiliares.class, auxPK);
        Double saldoValidar=Double.parseDouble(a.getSaldo().toString())-monto;
        if(a.getAuxiliaresPK().getIdproducto()==100){
            
        }
        if(saldovalidar>=100)
            
        } catch (Exception e) {
            
        }finally{
            em.close();
        }*/
        return false;
    }

    //validacion de saldo minimo y maximo
    public String limite_saldo_max_min(String num_socio, String num_opa, int cargo_abono, Double monto_transferencia) {
        EntityManager em = AbstractFacade.conexion();
        String mensaje = "";
        //Parseamos ogs y opa
        ogsDTO ogs = util.ogs(num_socio);
        opaDTO opa = util.opa(num_opa);

        try {
            AuxiliaresPK auxPK = new AuxiliaresPK(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar());
            Auxiliares auxi = em.find(Auxiliares.class, auxPK);
            TablasPK tbPK = new TablasPK("bankingly_banca_movil", "usuario_banca_movil");
            Tablas tbus = em.find(Tablas.class, tbPK);

            String sal_max_min = "SELECT sai_valida_limites_de_saldo_max_min(" + opa.getIdproducto() + ",'"
                    + "socio:" + ogs.getIdorigen() + "-" + ogs.getIdgrupo() + "-" + ogs.getIdsocio() + "|"
                    + "opa:" + opa.getIdorigenp() + "-" + opa.getIdproducto() + "-" + opa.getIdauxiliar() + "|"
                    + "cargoabono:" + cargo_abono + "|"
                    + "monto:" + monto_transferencia + "|"
                    + "saldo:" + auxi.getSaldo() + "|"
                    + "idproducto:" + opa.getIdproducto() + "|"
                    + "modulo:4|"
                    + "plazo:" + auxi.getPlazo() + "|"
                    + "montosolicitado:" + auxi.getMontosolicitado() + "|"
                    + "montoautorizado:" + auxi.getMontoautorizado() + "|"
                    + "montoprestado:" + auxi.getMontoprestado() + "|"
                    + "usuario:" + tbus.getDato1() + "')";
            System.out.println("QUERY SALDO MAXIMO Y MINIMO: " + sal_max_min);

            Query saldo_max_min = em.createNativeQuery(sal_max_min);
            String resultado_max_min = String.valueOf(saldo_max_min.getSingleResult());
            String resmaxmin = resultado_max_min;

            if (resmaxmin.equals("null")/*!resmaxmin.equals("")*/) {
                System.out.println("SI ENTRO");
            } else {
                mensaje = resmaxmin;
                System.out.println("NO ENTRO");
            }

        } catch (Exception e) {
            System.out.println("ERROR PRODUCIDO EN VALIDAR PERMITIDO: " + e.getMessage());
            em.close();
        } finally {
            em.close();
        }

        return mensaje;
    }

    //valida el monto para banca movil total de transferencias
    public String minMax(Double amount) {
        EntityManager em = AbstractFacade.conexion();
        String mensaje = "";
        try {
            TablasPK tbPk = new TablasPK("bankingly_banca_movil", "montomaximominimo");
            Tablas tb = em.find(Tablas.class,
                    tbPk);
            if (amount > Double.parseDouble(tb.getDato1())) {
                mensaje = "MAYOR";
            } else if (amount < Double.parseDouble(tb.getDato2())) {
                mensaje = "MENOR";
            } else {
                mensaje = "VALIDO";
            }
        } catch (Exception e) {
            em.close();
            System.out.println("Error al validar monto min-max:" + e.getMessage());
        } finally {
            em.close();
        }
        return mensaje;
    }

    public boolean actividad_horario() {
        EntityManager em = AbstractFacade.conexion();//emf.createEntityManager()EntityManager em = emf.createEntityManager();EntityManager em = emf.createEntityManager();
        boolean bandera_ = false;
        try {
            if (util.actividad(em)) {
                bandera_ = true;
            }
        } catch (Exception e) {
            System.out.println("Error al verificar el horario de actividad");
        } finally {
            em.close();
        }

        return bandera_;
    }

    /*public void cerrar() {
        emf.close();
    }*/
}
