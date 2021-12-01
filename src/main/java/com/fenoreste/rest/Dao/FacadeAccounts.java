/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.rest.Dao;

import DTO.AccountHoldersDTO;
import DTO.AccountHoldersValidateDTO;
import DTO.Auxiliares_dDTO;
import DTO.DetailsAccountDTO;
import DTO.HoldsDTO;
import DTO.opaDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.Entidades.Auxiliares;
import com.fenoreste.rest.Entidades.AuxiliaresD;
import com.fenoreste.rest.Entidades.AuxiliaresPK;
import com.fenoreste.rest.Entidades.Persona;
import com.fenoreste.rest.Entidades.PersonasPK;
import com.fenoreste.rest.Entidades.Productos;
import com.fenoreste.rest.Entidades.V_auxiliares;
import com.fenoreste.rest.Entidades.tipos_cuenta_siscoop;
import com.fenoreste.rest.Entidades.transferencias_completadas_siscoop;
import com.fenoreste.rest.Entidades.v_auxiliaresPK;
import com.fenoreste.rest.Util.Utilidades;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Elliot
 */
public abstract class FacadeAccounts<T> {

    public FacadeAccounts(Class<T> entityClass) {

    }

    Utilidades Util = new Utilidades();

    public List<AccountHoldersValidateDTO> validateInternalAccount(String accountId) {
        EntityManager em = AbstractFacade.conexion();
        opaDTO opa = Util.opa(accountId);

        System.out.println("AccountIDDDDDDDDDDDDD:" + accountId);
        List<AccountHoldersValidateDTO> holders = new ArrayList<AccountHoldersValidateDTO>();
        try {
            String consulta = "SELECT * FROM auxiliares a WHERE "
                    + " a.idorigenp = " + opa.getIdorigenp() + " AND a.idproducto = " + opa.getIdproducto() + " AND a.idauxiliar = " + opa.getIdauxiliar();
            System.out.println("consulta:" + consulta);
            Query query = em.createNativeQuery(consulta, Auxiliares.class);
            Auxiliares aa = (Auxiliares) query.getSingleResult();
            PersonasPK personaspk = new PersonasPK(aa.getIdorigen(), aa.getIdgrupo(), aa.getIdsocio());
            Persona person = em.find(Persona.class, personaspk);
            /*
        AuxiliaresPK auxpk = new AuxiliaresPK(o, p, a);
        Auxiliares aa = em.find(Auxiliares.class, auxpk);
             */
            String o = String.format("%06d", aa.getIdorigen());
            String g = String.format("%02d", aa.getIdgrupo());
            String s = String.format("%06d", aa.getIdsocio());

            String ogs = o + g + s;
            String persona = person.getNombre() + " " + person.getAppaterno() + " " + person.getApmaterno();

            //AccountHoldersDTO dto = new AccountHoldersDTO(persona, "SOW");
            AccountHoldersValidateDTO dto = new AccountHoldersValidateDTO(persona, "SOW", ogs);
            holders.add(dto);
        } catch (Exception e) {
            em.close();
            System.out.println("Error al crear lista:" + e.getMessage());
        } finally {
            em.close();
        }
        System.out.println("Holders:" + holders);
        return holders;
    }

    public List<String> validateBeneficiary(String accountId, String accountType) {
        EntityManager em = AbstractFacade.conexion();
        int o = Integer.parseInt(accountId.substring(0, 6));
        int p = Integer.parseInt(accountId.substring(6, 11));
        int a = Integer.parseInt(accountId.substring(11, 19));
        List<String> lista = new ArrayList<String>();
        try {
            AuxiliaresPK auxpk = new AuxiliaresPK(o, p, a);
            Auxiliares aa = em.find(Auxiliares.class, auxpk);
            PersonasPK personaspk = new PersonasPK(aa.getIdorigen(), aa.getIdgrupo(), aa.getIdsocio());
            Persona person = em.find(Persona.class, personaspk);
            String persona = person.getNombre() + " " + person.getAppaterno() + " " + person.getApmaterno();
            tipos_cuenta_siscoop tps = em.find(tipos_cuenta_siscoop.class, aa.getAuxiliaresPK().getIdproducto());
            if (tps.getProducttypename().trim().replace(" ", "").toUpperCase().contains(accountType.replace(" ", ""))) {
                lista.add(persona.toUpperCase());
                lista.add(tps.getProducttypename().trim().toUpperCase());
                lista.add(accountId);
            } else {
                System.out.println("no es");
            }

        } catch (Exception e) {
            System.out.println("Error al crear lista:" + e.getMessage());
        } finally {
            em.close();
        }
        return lista;
    }

    public String statements(String accountId, String initialDate, String finalDate, int pageSize, int pageStartIndex) {
        EntityManager em = AbstractFacade.conexion();
        File file = null;
        try {
            file = crear_llenar_txt(accountId, initialDate, finalDate);
            //file=new File(ruta()+"e_cuenta_ahorro_0101010011000010667_2.txt");
            System.out.println("fileNameTxt:" + file.getName());
            File fileTxt = new File(ruta() + file.getName());
            if (fileTxt.exists()) {
                File fileHTML = crear_llenar_html(fileTxt, fileTxt.getName().replace(".txt", ".html"));
                if (crearPDF(ruta(), fileHTML.getName())) {
                    System.out.println("si");
                    String pdf = ruta() + fileHTML.getName().replace(".html", "pdf");
                    fileTxt.delete();
                    fileHTML.delete();
                }
            }
        } catch (Exception e) {
            System.out.println("Error en conver:" + e.getMessage());
        }
        /*try{
        String consulta="SELECT * FROM auxiliares_d WHERE"
                + " replace(to_char(idorigenp,'099999')||to_char(idproducto,'09999')||to_char(idauxiliar,'09999999'),' ','')='"+accountId
                +"' AND date(fecha) BETWEEN '"+initialDate+"' AND '"+finalDate+"'";
        System.out.println("Consulta:"+consulta);
        Query query=em.createNativeQuery(consulta,AuxiliaresD.class);
        query.setFirstResult(pageStartIndex);
        query.setMaxResults(pageSize);
     listaa =query.getResultList();
        for(int i=0;i<listaa.size();i++){
            AuxiliaresD a=listaa.get(i);
            System.out.println("a:"+a);
            StatementsDTO dto=new StatementsDTO(
                    accountId,
                    initialDate,
                    finalDate,
                    String.valueOf(a.getAuxiliaresDPK().getIdorigenp()+a.getAuxiliaresDPK().getIdproducto()+a.getAuxiliaresDPK().getIdauxiliar()),
                    String.valueOf(a.getTransaccion()));    
        lista.add(dto);
        }
        System.out.println("lista:"+lista);
          
    }catch(Exception e){
        System.out.println("Error:"+e.getMessage());
                
    }*/
        return file.getName().replace(".txt", ".pdf");

    }

    public List<HoldsDTO> holds(String accountId) {
        EntityManager em = AbstractFacade.conexion();
        List<HoldsDTO> listaDTO = new ArrayList<HoldsDTO>();
        opaDTO opa = Util.opa(accountId);
        try {
            String consulta = "SELECT * FROM auxiliares a WHERE "
                    + " a.idorigenp = " + opa.getIdorigenp() + " AND a.idproducto = " + opa.getIdproducto() + " AND a.idauxiliar = " + opa.getIdauxiliar()
                    + " AND estatus = 2 AND garantia > 0";
            System.out.println("consulta:" + consulta);

            Query query = em.createNativeQuery(consulta, Auxiliares.class);

            List<Auxiliares> lista = query.getResultList();
            System.out.println("listaSize:" + lista.size());
            for (int i = 0; i < lista.size(); i++) {
                System.out.println("ne");
                Auxiliares a = lista.get(i);
                try {
                    String consulta2 = "SELECT * FROM referenciasp WHERE idorigenpr=" + a.getAuxiliaresPK().getIdorigenp() + " AND idproductor=" + a.getAuxiliaresPK().getIdproducto() + " AND idauxiliarr=" + a.getAuxiliaresPK().getIdauxiliar();
                    System.out.println("consulta2:" + consulta2);
                    Query query1 = em.createNativeQuery(consulta2);
                    List<Object[]> listarf = query1.getResultList();
                    String fbloqueo = "";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Productos pr = em.find(Productos.class, a.getAuxiliaresPK().getIdproducto());
                    System.out.println("io");
                    System.out.println("lista:" + listarf);
                    for (Object[] obj : listarf) {
                        v_auxiliaresPK auxpk = new v_auxiliaresPK(Integer.parseInt(obj[0].toString()), Integer.parseInt(obj[1].toString()), Integer.parseInt(obj[2].toString()));
                        V_auxiliares aa = em.find(V_auxiliares.class, auxpk);
                        System.out.println("aaa:" + aa.getAuxiliaresPK().getIdproducto());
                        if (aa.getEstatus() <= 2) {
                            System.out.println("entro");
                            if (aa.getEstatus() == 2) {
                                System.out.println("fecha:" + aa.getFechaactivacion());
                                String ff = "2021-03-30 00:00:00";
                                Timestamp tss = Timestamp.valueOf(ff);
                                System.out.println("cadena:" + tss);
                                fbloqueo = sdf.format(aa.getFechaactivacion());
                                //fbloqueo = String.valueOf(aa.getFechaactivacion());
                            } else if (aa.getEstatus() < 2) {
                                System.out.println("salio del if");
                                //fbloqueo = String.valueOf(aa.getFechaape());
                                fbloqueo = sdf.format(aa.getFechaape());
                            }
                            HoldsDTO dto = new HoldsDTO(accountId,
                                    Double.parseDouble(a.getGarantia().toString()),
                                    fbloqueo,
                                    pr.getNombre());
                            System.out.println("DTO:" + dto);
                            listaDTO.add(dto);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error:" + e.getMessage());
                }

            }
        } catch (Exception e) {
            System.out.println("Error en holds:" + e.getMessage());
        } finally {
            em.close();
        }
        return listaDTO;
    }

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

    public List<Auxiliares_dDTO> History(String accountId, List<String> fechas, List<String> montos, String transactionType, int count, int pageSize, int pageStartIndex) {
        EntityManager em = AbstractFacade.conexion();
        List<transferencias_completadas_siscoop> dtoFacade = new ArrayList<>();
        List<AuxiliaresD> aux = new ArrayList<>();
        List<Auxiliares_dDTO> listaDTO = new ArrayList<>();
        opaDTO opa = Util.opa(accountId);
        List<AuxiliaresD> listaContador = null;
        try {
            int inicio_busqueda = pageStartIndex * pageSize;
            String con = "SELECT * FROM auxiliares_d WHERE ";
            String sulta = "";
            String complemento_transaction_type = "";
            //Por si el socio es curioso y marco todos los filtros
            if (fechas.size() > 0 && montos.size() > 0 && !transactionType.equals("") && count > 0) {
                if (transactionType.equals("A")) {
                    complemento_transaction_type = " cargoabono in(0,1)";
                } else if (transactionType.equals("C")) {
                    complemento_transaction_type = " cargoabono=0";
                } else if (transactionType.equals("D")) {
                    complemento_transaction_type = " cargoabono=1";
                }
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND date(fecha) between '" + fechas.get(0).trim() + "' AND '" + fechas.get(1).trim() + "'"
                        + " AND monto between " + Double.parseDouble(montos.get(0)) + " AND " + Double.parseDouble(montos.get(1))
                        + " AND " + complemento_transaction_type
                        + " ORDER BY fecha DESC LIMIT " + count;
                //Si solo esta marcado fecha,monto,transactionType
            } else if (fechas.size() > 0 && montos.size() > 0 && !transactionType.equals("") && count == 0) {

                if (transactionType.equals("A")) {
                    complemento_transaction_type = " cargoabono in(0,1)";
                } else if (transactionType.equals("C")) {
                    complemento_transaction_type = " cargoabono=0";
                } else if (transactionType.equals("D")) {
                    complemento_transaction_type = " cargoabono=1";
                }
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND date(fecha) between '" + fechas.get(0).trim() + "' AND '" + fechas.get(1).trim() + "'"
                        + " AND monto between " + Double.parseDouble(montos.get(0)) + " AND " + Double.parseDouble(montos.get(1))
                        + " AND " + complemento_transaction_type
                        + " ORDER BY fecha DESC";
            } else if (fechas.size() > 0 && montos.size() > 0 && transactionType.equals("") && count == 0) {//Si solo esta marcado fechas y montos
                System.out.println("Fechas y montos");
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND date(fecha) between '" + fechas.get(0).trim() + "' AND '" + fechas.get(1).trim() + "'"
                        + " AND monto between " + Double.parseDouble(montos.get(0)) + " AND " + Double.parseDouble(montos.get(1))
                        + " ORDER BY fecha DESC";
            } else if (fechas.size() > 0 && montos.size() == 0 && transactionType.equals("") && count == 0) {//Si solo fechas
                System.out.println("entroooooooooooooo aqui");
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND date(fecha) between '" + fechas.get(0).trim() + "' AND '" + fechas.get(1).trim() + "'"
                        + " ORDER BY fecha DESC";
            } else if (fechas.size() > 0 && montos.size() == 0 && !transactionType.equals("") && count > 0) {//Si solo vienen fechas,tipo transaccion y contador
                System.out.println("Fechas,tipo transaccion y count");
                if (transactionType.equals("A")) {
                    complemento_transaction_type = " cargoabono in(0,1)";
                } else if (transactionType.equals("C")) {
                    complemento_transaction_type = " cargoabono=0";
                } else if (transactionType.equals("D")) {
                    complemento_transaction_type = " cargoabono=1";
                }
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND " + complemento_transaction_type
                        + " AND date(fecha) between '" + fechas.get(0).trim() + "' AND '" + fechas.get(1).trim() + "' ORDER BY fecha DESC LIMIT " + count;
            } else if (fechas.size() > 0 && montos.size() == 0 && !transactionType.equals("") && count == 0) {//Si solo viene fecha,tipos de transacccion
                if (transactionType.equals("A")) {
                    complemento_transaction_type = " cargoabono in(0,1)";
                } else if (transactionType.equals("C")) {
                    complemento_transaction_type = " cargoabono=0";
                } else if (transactionType.equals("D")) {
                    complemento_transaction_type = " cargoabono=1";
                }
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND " + complemento_transaction_type
                        + " AND date(fecha) between '" + fechas.get(0).trim() + "' AND '" + fechas.get(1).trim() + "'"
                        + " ORDER BY fecha DESC";

            } else if (fechas.size() > 0 && montos.size() == 0 && transactionType.equals("") && count > 0) {//Solo fecha y contador
                System.out.println("aquiiiiiiiiiiiiiiiiiiiiiiiii");
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND " + complemento_transaction_type
                        + " AND date(fecha) between '" + fechas.get(0).trim() + "' AND '" + fechas.get(1).trim() + "' ORDER BY fecha DESC LIMIT " + count;
            } else if (fechas.size() == 0 && montos.size() > 0 && !transactionType.equals("") && count > 0) {//Si solo montos,tipo de transaccion y contador de operacions
                System.out.println("aquiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
                if (transactionType.equals("A")) {
                    complemento_transaction_type = " cargoabono in(0,1)";
                } else if (transactionType.equals("C")) {
                    complemento_transaction_type = " cargoabono=0";
                } else if (transactionType.equals("D")) {
                    complemento_transaction_type = " cargoabono=1";
                }
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND " + complemento_transaction_type
                        + " AND monto between  " + Double.parseDouble(montos.get(0)) + " AND " + Double.parseDouble(montos.get(1)) + " ORDER BY fecha DESC LIMIT " + count;

            } else if (fechas.size() == 0 && montos.size() > 0 && transactionType.equals("") && count > 0) {//Si solo montos y tipo de transaccion
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND " + complemento_transaction_type
                        + " AND monto between " + Double.parseDouble(montos.get(0)) + " AND " + Double.parseDouble(montos.get(1))
                        + " ORDER BY fecha DESC";
            } else if (fechas.size() == 0 && montos.size() > 0 && transactionType.equals("") && count > 0) { //Si solo montos y contador de operaciones
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND monto between " + Double.parseDouble(montos.get(0)) + " AND " + Double.parseDouble(montos.get(1)) + " ORDER BY fecha DESC LIMIT " + count;

            } else if (fechas.size() == 0 && montos.size() == 0 && transactionType.equals("") && count == 0) {//solo monto
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND monto between " + Double.parseDouble(montos.get(0)) + " AND " + Double.parseDouble(montos.get(1))
                        + " ORDER BY fecha DESC";
            } else if (fechas.size() == 0 && montos.size() == 0 && !transactionType.equals("") && count > 0) {//Solo tipo de transaccion y contador
                if (transactionType.equals("A")) {
                    complemento_transaction_type = " cargoabono in(0,1)";
                } else if (transactionType.equals("C")) {
                    complemento_transaction_type = " cargoabono=0";
                } else if (transactionType.equals("D")) {
                    complemento_transaction_type = " cargoabono=1";
                }
                sulta = " idorigenp = " + opa.getIdorigenp()
                        + " AND idproducto = " + opa.getIdproducto()
                        + " AND idauxiliar = " + opa.getIdauxiliar()
                        + " AND " + complemento_transaction_type + " ORDER BY fecha DESC LIMIT  " + count;
            }

            System.out.println("CONSULTA: " + con + sulta);
            Query query = em.createNativeQuery(con + sulta, AuxiliaresD.class);
            listaContador = query.getResultList();
            System.out.println("TOTAL REGISTROS H: " + listaContador.size());

            query.setFirstResult(inicio_busqueda);
            query.setMaxResults(pageSize);
            aux = query.getResultList();

            for (int y = 0; y < aux.size(); y++) {
                Auxiliares_dDTO adto = new Auxiliares_dDTO();
                adto.setAuxiliaresDPK(aux.get(y).getAuxiliaresDPK());
                adto.setCargoabono(aux.get(y).getCargoabono());
                adto.setMonto(aux.get(y).getMonto());
                adto.setMontoio(aux.get(y).getMontoio());
                adto.setMontoim(aux.get(y).getMontoim());
                adto.setMontoiva(aux.get(y).getMontoiva());
                adto.setIdorigenc(aux.get(y).getIdorigenc());
                adto.setPeriodo(aux.get(y).getPeriodo());
                adto.setIdtipo(aux.get(y).getIdtipo());
                adto.setIdpoliza(aux.get(y).getIdpoliza());
                adto.setTipomov(aux.get(y).getTipomov());
                adto.setSaldoec(aux.get(y).getSaldoec());
                adto.setTransaccion(aux.get(y).getTransaccion());
                adto.setMontoivaim(aux.get(y).getMontoivaim());
                adto.setEfectivo(aux.get(y).getEfectivo());
                adto.setDiasvencidos(aux.get(y).getDiasvencidos());
                adto.setMontovencido(aux.get(y).getMontovencido());
                adto.setTicket(aux.get(y).getTicket());
                adto.setMontoidnc(aux.get(y).getMontoidnc());
                adto.setMontoieco(aux.get(y).getMontoieco());
                adto.setMontoidncm(aux.get(y).getMontoidncm());
                adto.setMontoiecom(aux.get(y).getMontoiecom());
                adto.setTotal_lista(listaContador.size());
                listaDTO.add(adto);
            }
            //lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error al leer transacciones:" + e.getMessage());
        } finally {
            em.close();
        }

        return listaDTO;
    }

    public List<AuxiliaresD> Histbbory_Size(String accountId, String initialDate, String finalDate) {
        EntityManager em = AbstractFacade.conexion();
        List<AuxiliaresD> aux = new ArrayList<>();
        opaDTO opa = Util.opa(accountId);
        try {
            String consulta = "SELECT * FROM auxiliares_d WHERE idorigenp = " + opa.getIdorigenp()
                    + " AND idproducto = " + opa.getIdproducto() + " AND idauxiliar = " + opa.getIdauxiliar()
                    + " AND date(fecha) between '" + initialDate.trim() + "' AND '" + finalDate.trim() + "' ORDER BY fecha desc";
            System.out.println("CONSULTA: " + consulta);
            Query query = em.createNativeQuery(consulta, AuxiliaresD.class
            );
            aux = query.getResultList();
            System.out.println("TOTAL REGISTROS H_S: " + aux.size());
        } catch (Exception e) {
            System.out.println("Error al leer transacciones:" + e.getMessage());
        } finally {
            em.close();
        }
        return aux;
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

    public DetailsAccountDTO detailsAccount(String accountId) {
        EntityManager em = AbstractFacade.conexion();
        DetailsAccountDTO dto = new DetailsAccountDTO();
        opaDTO opa = Util.opa(accountId);
        try {
            String consulta = "SELECT * FROM auxiliares a WHERE "
                    + " a.idorigenp = " + opa.getIdorigenp() + " AND a.idproducto = " + opa.getIdproducto() + " AND a.idauxiliar = " + opa.getIdauxiliar();
            System.out.println("Consulta:" + consulta);
            Query query = em.createNativeQuery(consulta, Auxiliares.class
            );
            Auxiliares a = (Auxiliares) query.getSingleResult();
            System.out.println("a:" + a);
            tipos_cuenta_siscoop tps = em.find(tipos_cuenta_siscoop.class,
                    a.getAuxiliaresPK().getIdproducto());
            System.out.println("tps:" + tps);
            String e = "";
            if (a.getEstatus() == 0) {
                e = "INACTIVE";
            } else if (a.getEstatus() == 1) {
                e = "DORMANT";
            } else if (a.getEstatus() == 2) {
                e = "OPEN";
            } else if (a.getEstatus() == 3) {
                e = "CLOSED";
            }
            Query query1 = em.createNativeQuery("SELECT nombre FROM origenes WHERE idorigen=" + a.getAuxiliaresPK().getIdorigenp());
            String sucursal = (String) query1.getSingleResult();
            System.out.println("Sucursal:" + sucursal);
            String aa = String.format("%08d", a.getAuxiliaresPK().getIdauxiliar());
            String cadenaa = aa.substring(4, 8);
            String cade = "******" + cadenaa;
            /*dto = new DetailsAccountDTO(
                    accountId,
                    accountId,//String.valueOf(a.getAuxiliaresPK().getIdorigenp()) + "" + String.valueOf(a.getAuxiliaresPK().getIdproducto()) + "" + String.valueOf(a.getAuxiliaresPK().getIdauxiliar()),
                    accountId,//String.valueOf(a.getAuxiliaresPK().getIdorigenp()) + "" + String.valueOf(a.getAuxiliaresPK().getIdproducto()) + "" + String.valueOf(a.getAuxiliaresPK().getIdauxiliar()),
                    tps.getProducttypename().toUpperCase(),
                    "MXN",
                    String.valueOf(a.getAuxiliaresPK().getIdproducto()),
                    e,
                    sucursal,
                    String.valueOf(a.getFechaactivacion()));*/
            Productos producto = em.find(Productos.class,
                    a.getAuxiliaresPK().getIdproducto());

            String fechaTrabajo = "SELECT date(fechatrabajo) FROM origenes limit 1";
            Query queryOrigenes = em.createNativeQuery(fechaTrabajo);
            String fechaTrabajoReal = String.valueOf(queryOrigenes.getSingleResult());
            String fecha[] = fechaTrabajoReal.split("-");
            LocalDate date = LocalDate.of(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[2]));
            System.out.println("FechaTrabajo: " + date);
            //date = date.plusDays(7);
            String date_semana = String.valueOf(date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (producto.getTipoproducto() == 1 || producto.getTipoproducto() == 8) {
                //Corremos sai_auxiliar para obetener datos  

                //Corro SAi para calculo de interes en una semana
                String sai_interes = "SELECT sai_auxiliar(" + a.getAuxiliaresPK().getIdorigenp() + "," + a.getAuxiliaresPK().getIdproducto() + "," + a.getAuxiliaresPK().getIdauxiliar() + ",'" + date_semana + "')";
                System.out.println("sasaasas: " + sai_interes);
                Query RsSai = em.createNativeQuery(sai_interes);
                String sai_aux = RsSai.getSingleResult().toString();
                String[] parts = sai_aux.split("\\|");
                List list = Arrays.asList(parts);
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaDate = formato.parse(list.get(2).toString());
                SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd");
                String realDate = formato2.format(fechaDate);
                dto.setProximoMontoInteres(Double.parseDouble(list.get(5).toString()));
                dto.setProximaFechaPago(date_semana);
                dto.setFechaVencimiento(realDate.replace("/", "-"));

            } else if (producto.getTipoproducto() == 2) {
                //Corro SAi para calculo de interes en una semana
                String sai_interes = "SELECT sai_auxiliar(" + a.getAuxiliaresPK().getIdorigenp() + "," + a.getAuxiliaresPK().getIdproducto() + "," + a.getAuxiliaresPK().getIdauxiliar() + ",'" + date_semana + "')";
                System.out.println("sasaasas: " + sai_interes);
                Query RsSai = em.createNativeQuery(sai_interes);
                String sai_aux = RsSai.getSingleResult().toString();
                String[] parts = sai_aux.split("\\|");
                List lista = Arrays.asList(parts);
                String sai_montototal = "SELECT sai_bankingly_prestamo_cuanto(" + a.getAuxiliaresPK().getIdorigenp() + "," + a.getAuxiliaresPK().getIdproducto() + "," + a.getAuxiliaresPK().getIdauxiliar() + "," + "'" + date_semana + "'" + "," + a.getTipoamortizacion() + "," + "'" + sai_aux + "'" + ")";
                System.out.println("MONTO TOTAL A CUBRIR: " + sai_montototal);
                Query sai_mont = em.createNativeQuery(sai_montototal);
                String sai_prest_cuanto = sai_mont.getSingleResult().toString();
                String[] sai_pres_parts = sai_prest_cuanto.split("\\|");
                List lista_saipres = Arrays.asList(sai_pres_parts);
                dto.setProximoMontoInteres(Double.parseDouble(lista_saipres.get(0).toString()));
                dto.setProximaFechaPago(lista.get(10).toString());
                dto.setFechaVencimiento(lista.get(8).toString());
                dto.setMontoDesembolso(a.getMontoprestado().doubleValue());
            }

            dto.setAccountId(accountId);
            dto.setAccountNumber(accountId);
            dto.setDisplayAccountNumber(accountId);
            dto.setAccountType(tps.getProducttypename().trim().toUpperCase());
            dto.setCurrencyCode("MXN");
            dto.setProductCode(String.valueOf(a.getAuxiliaresPK().getIdproducto()));
            dto.setStatus(e);
            dto.setSucursal(sucursal);
            String Fe_Ac = sdf.format(a.getFechaactivacion());
            dto.setOpenedDate(Fe_Ac);
            //dto.setOpenedDate(String.valueOf(String.valueOf(a.getFechaactivacion())));
            dto.setTasa(a.getTasaio().doubleValue());

        } catch (Exception e) {
            System.out.println("Error en buscar detalles de cuenta:" + e.getMessage());
        } finally {
            em.close();
        }
        return dto;
    }

    public String Holders(String accountId) {
        EntityManager em = AbstractFacade.conexion();
        String nombre = "";
        opaDTO opa = Util.opa(accountId);
        try {
            String consulta = "SELECT * FROM auxiliares a WHERE "
                    + " a.idorigenp = " + opa.getIdorigenp() + " AND a.idproducto = " + opa.getIdproducto() + " AND a.idauxiliar = " + opa.getIdauxiliar();
            Query query = em.createNativeQuery(consulta, Auxiliares.class
            );
            Auxiliares a = (Auxiliares) query.getSingleResult();
            PersonasPK pk = new PersonasPK(a.getIdorigen(), a.getIdgrupo(), a.getIdsocio());
            Persona p = em.find(Persona.class,
                    pk);
            System.out.println("p:" + p);
            nombre = p.getNombre() + " " + p.getAppaterno() + " " + p.getApmaterno();
        } catch (Exception e) {
            System.out.println("Error al buscar propietario de la cuenta:" + e.getMessage());
        }
        return nombre;
    }

    public String accountType(int idproducto) {
        EntityManager em = AbstractFacade.conexion();
        tipos_cuenta_siscoop tip = null;
        try {
            tip = em.find(tipos_cuenta_siscoop.class,
                    idproducto);
        } catch (Exception e) {
            System.out.println("Error al buscar tipo cuenta siscoop:" + e.getMessage());
        }
        return tip.getProducttypename().trim().toUpperCase();
    }


    /*Creando estados de cuenta*/
    public static String ruta() {
        String home = System.getProperty("user.home");
        String separador = System.getProperty("file.separator");
        return home + separador + "Banca" + separador;
    }

    public File crear_llenar_txt(String accountId, String initialDate, String finalDate) {
        opaDTO opa = Util.opa(accountId);
        int numeroAleatorio = (int) (Math.random() * 9 + 1);
        EntityManager em = AbstractFacade.conexion();
        File file = null;
        Productos pr = em.find(Productos.class,
                opa.getIdproducto());
        String nombre_formato = "";
        String nombre_archivo = "";
        if (pr.getTipoproducto() == 1) {
            nombre_formato = "estado_cuenta_dpfs_ind";
            nombre_archivo = "e_cuenta_dpfs_ind_";
        } else if (pr.getTipoproducto() == 0) {
            nombre_formato = "estado_cuenta_ahorros";
            nombre_archivo = "e_cuenta_ahorros_";
        } else if (pr.getTipoproducto() == 2) {
            nombre_formato = "estado_cuenta_prestamos";
            nombre_archivo = "e_cuenta_prestamos_";
        }
        String nombre_txt = nombre_archivo + opa.getIdorigenp() + "" + opa.getIdproducto() + "" + opa.getIdauxiliar() + "_" + String.valueOf(numeroAleatorio) + ".txt";
        System.out.println("nombreTxt:" + nombre_txt);
        try {
            String fichero_txt = ruta() + nombre_txt;
            String contenido;
            String consulta = "SELECT sai_" + nombre_formato + "(" + opa.getIdorigenp() + "," + opa.getIdproducto() + "," + opa.getIdauxiliar() + ",'" + initialDate + "','" + finalDate + "')";
            System.out.println("Consulta Statements:" + consulta);
            Query query = em.createNativeQuery(consulta);
            contenido = String.valueOf(query.getSingleResult());
            file = new File(fichero_txt);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(contenido);
            bw.close();

        } catch (Exception e) {
            em.close();
            System.out.println("Error:" + e.getMessage());
        } finally {
            em.close();
        }

        return file;
    }

    public File crear_llenar_html(File file, String nombre) throws FileNotFoundException {
        String nombre_html = nombre;//=nombre_txt.replace(".txt",".html");
        String html = ruta() + nombre_html;
        File fi = new File(html);
        FileOutputStream fs = new FileOutputStream(fi);
        OutputStreamWriter out = new OutputStreamWriter(fs);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            String linea_contenedor = "";

            while ((linea = br.readLine()) != null) {

                if (linea.contains("/usr/local/saicoop/img_estado_cuenta_ahorros/")) {
                    String cade = ruta();
                    System.out.println("Cade:" + cade.replace("\\", "/"));
                    linea = linea.replace("/usr/local/saicoop/img_estado_cuenta_ahorros/", cade.replace("\\", "/"));
                }
                if (linea.contains(" & ")) {
                    System.out.println("si tele");
                    linea = linea.replace(" & ", " y ");
                }
                ////cambio en el contenedor de html para ajustar al convetir a pdf
                if (linea.contains(".contenedor")) {
                    linea_contenedor = linea_contenedor + linea;
                }
                if (linea.contains("height:") && linea_contenedor.contains(".contenedor")) {
                    String[] lineas = linea.split(":");
                    String linea_despues_de_2puntos = lineas[1].replace(" ", "");
                    linea_despues_de_2puntos = ": auto;";
                    String linea_reconstruida = lineas[0] + linea_despues_de_2puntos;
                    linea = linea_reconstruida;
                }
                if (linea.contains(" width:") && linea_contenedor.contains(".contenedor")) {
                    String[] lineas = linea.split(":");
                    String linea_despues_de_2puntos = lineas[1].replace(" ", "");
                    linea_despues_de_2puntos = ": auto;";
                    String linea_reconstruida = lineas[0] + linea_despues_de_2puntos;
                    linea = linea_reconstruida;
                }

                out.write(linea);
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Excepcion leyendo txt" + ": " + e.getMessage());
        }
        return fi;
    }

    public boolean crearPDF(String ruta, String nombreDelHTMLAConvertir) {
        try {
            //ruta donde esta el html a convertir
            String ficheroHTML = ruta + nombreDelHTMLAConvertir;

            String url = new File(ficheroHTML).toURI().toURL().toString();
            //ruta donde se almacenara el pdf y que nombre se le data
            String ficheroPDF = ruta + nombreDelHTMLAConvertir.replace("T", "").replace("-", "").replace(":", "").replace(".html", ".pdf");
            /* OutputStream os = new FileOutputStream(ficheroPDF);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(url);
            renderer.layout();
            renderer.createPDF(os);
            os.close();*/
            File htmlSource = new File(ficheroHTML);
            File pdfDest = new File(ficheroPDF);
            // pdfHTML specific code
            ConverterProperties converterProperties = new ConverterProperties();

            HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
            return true;
        } catch (Exception e) {
            System.out.println("Error al crear PDF:" + e.getMessage());
            return false;
        }

    }

    public boolean validarCuenta(String accountId) {
        EntityManager em = AbstractFacade.conexion();
        opaDTO opa = Util.opa(accountId);
        try {
            String consulta = "SELECT count(*) FROM auxiliares a WHERE "
                    + " a.idorigenp = " + opa.getIdorigenp() + " AND a.idproducto = " + opa.getIdproducto() + " AND a.idauxiliar = " + opa.getIdauxiliar();
            Query query = em.createNativeQuery(consulta);
            int count = Integer.parseInt(String.valueOf(query.getSingleResult()));
            if (count > 0) {
                return true;
            }

        } catch (Exception e) {
            em.clear();
            em.close();
            System.out.println("Error al validar cuenta:" + e.getMessage());
        }
        em.close();
        return false;

    }

    public boolean actividad_horario() {
        EntityManager em = AbstractFacade.conexion();
        boolean bandera_ = false;
        try {
            if (Util.actividad(em)) {
                bandera_ = true;
            }
        } catch (Exception e) {
            System.out.println("ERROR AL VERIFICAR EL HORARIO DE ACTIVIDAD");
        } finally {
            em.close();
        }

        return bandera_;
    }

    /*public void cerrar() {
        emf.close();
    }*/
}
