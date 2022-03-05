package com.fenoreste.rest.Dao;

import DTO.CustomerAccountDTO;
import DTO.CustomerContactDetailsDTO;
import DTO.CustomerDetailsDTO;
import DTO.CustomerSearchDTO;
import DTO.ogsDTO;
import DTO.opaDTO;
import com.fenoreste.rest.Util.AbstractFacade;
import com.fenoreste.rest.Entidades.Auxiliares;
import com.fenoreste.rest.Entidades.AuxiliaresD;
import com.fenoreste.rest.Entidades.CuentasSiscoop;
import com.fenoreste.rest.Entidades.Persona;
import com.fenoreste.rest.Entidades.PersonasPK;
import com.fenoreste.rest.Entidades.Productos;
import com.fenoreste.rest.Entidades.validaciones_telefono_siscoop;
import com.fenoreste.rest.Util.Utilidades;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public abstract class FacadeCustomer<T> {

    public FacadeCustomer(Class<T> entityClass) {

    }

    List<Object[]> lista = null;

    Utilidades Util = new Utilidades();

    public List<CustomerSearchDTO> search(String ogs, String nombre, String appaterno) {
        EntityManager em = AbstractFacade.conexion();
        List<CustomerSearchDTO> listaC = new ArrayList<CustomerSearchDTO>();
        CustomerSearchDTO client = null;
        try {
            int o = 0, g = 0, s = 0;
            List<Persona> listaPersonas = new ArrayList<>();
            PersonasPK pk = null;
            Persona p = null;
            String customerId = "";

            String name = "", curp = "", taxId = "", customerType = "";
            Date birthDate = null;
            String sql = "";
            if (!ogs.equals("")) {
                ogsDTO id_ogs = Util.ogs(ogs);
                sql = "SELECT * FROM personas WHERE "
                        + " idorigen = " + id_ogs.getIdorigen() + " AND idgrupo = " + id_ogs.getIdgrupo() + " AND idsocio = " + id_ogs.getIdsocio()
                        + " AND idgrupo = 10";
                /*o = Integer.parseInt(ogs.substring(0, 6));
                g = Integer.parseInt(ogs.substring(6, 8));
                s = Integer.parseInt(ogs.substring(8, 14));
                pk = new PersonasPK(o, g, s);*/

            } else {
                sql = "SELECT * FROM personas WHERE UPPER(replace(nombre,' ','')) LIKE '%" + nombre + "%' AND UPPER(appaterno||apmaterno) LIKE '%" + appaterno + "%' AND idgrupo=10";

            }
            System.out.println("SQL:" + sql);
            Query queryPersonas = em.createNativeQuery(sql, Persona.class);
            listaPersonas = queryPersonas.getResultList();
            System.out.println("lista:" + listaPersonas);

            for (int i = 0; i < listaPersonas.size(); i++) {
                p = listaPersonas.get(i);
                customerId = String.format("%06d", p.getPersonasPK().getIdorigen()) + String.format("%02d", p.getPersonasPK().getIdgrupo()) + String.format("%06d", p.getPersonasPK().getIdsocio());
                name = p.getNombre() + " " + p.getAppaterno() + " " + p.getApmaterno();
                taxId = p.getCurp();
                birthDate = p.getFechanacimiento();
                if (p.getRazonSocial() == null) {
                    customerType = "individual";
                } else {
                    customerType = "grupal";
                }

                client = new CustomerSearchDTO(
                        customerId,
                        name,
                        taxId,
                        dateToString(birthDate).replace("/", "-"),
                        "individual");
                listaC.add(client);
            }

            return listaC;
        } catch (Exception e) {
            em.close();
            System.out.println("Error al buscar cliente:" + e.getMessage());

        } finally {
            em.close();
        }

        return null;
    }

    public CustomerDetailsDTO details(String ogs) {
        EntityManager em = AbstractFacade.conexion();
        List<CustomerDetailsDTO> listaC = new ArrayList<CustomerDetailsDTO>();
        CustomerDetailsDTO client = new CustomerDetailsDTO();
        try {
            int o = Integer.parseInt(ogs.substring(0, 6));
            int g = Integer.parseInt(ogs.substring(6, 8));
            int s = Integer.parseInt(ogs.substring(8, 14));
            PersonasPK pk = new PersonasPK(o, g, s);
            Persona p = em.find(Persona.class, pk);
            String name = "", customerType = "";
            name = p.getNombre() + " " + p.getAppaterno() + " " + p.getApmaterno();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String birthDate=sdf.format(p.getFechanacimiento());
            if (p.getRazonSocial() == null) {
                customerType = "individual";
            } else {
                customerType = "grupal";
            }
            client.setNationalId(p.getCurp());
            client.setBirthDate(birthDate.replace("/","-"));
            client.setCustomerId(ogs);
            client.setName(name);
            client.setCustomerType("individual");
            client.setTaxId(p.getCurp());
            return client;
        } catch (Exception e) {
            System.out.println("Error al buscar cliente:" + e.getMessage());
            em.close();
        }
        em.close();

        return null;
    }

    public List<CustomerContactDetailsDTO> ContactDetails(String ogs) {
        EntityManager em = AbstractFacade.conexion();
        Query query = null;
        List<Object[]> ListaObjetos = null;
        ogsDTO id_ogs = Util.ogs(ogs);
        String consulta = "SELECT CASE WHEN p.telefono != '' THEN p.telefono ELSE '0' END as phone,"
                + " CASE WHEN p.celular != '' THEN p.celular ELSE '0000000000' END as cellphone,"
                + " CASE WHEN p.email != '' THEN  p.email ELSE '0' END as email"
                + " FROM personas p WHERE "
                + " p.idorigen = " + id_ogs.getIdorigen() + " AND p.idgrupo = " + id_ogs.getIdgrupo() + " AND p.idsocio = " + id_ogs.getIdsocio();
        CustomerContactDetailsDTO contactsPhone = new CustomerContactDetailsDTO();
        CustomerContactDetailsDTO contactsCellphone = new CustomerContactDetailsDTO();
        CustomerContactDetailsDTO contactsEmail = new CustomerContactDetailsDTO();
        List<CustomerContactDetailsDTO> ListaContactos = new ArrayList<CustomerContactDetailsDTO>();

        try {
            int o = Integer.parseInt(ogs.substring(0, 6));
            int g = Integer.parseInt(ogs.substring(6, 8));
            int s = Integer.parseInt(ogs.substring(8, 14));
            PersonasPK pk = new PersonasPK(o, g, s);
            Persona p = em.find(Persona.class, pk);
            if (p.getTelefono() != null) {
                contactsPhone.setCustomerContactId(ogs);
                contactsPhone.setCustomerContactType("phone");
                contactsPhone.setPhoneNumber("521" + p.getTelefono());
                ListaContactos.add(contactsPhone);

            }
            if (p.getCelular() != null) {
                contactsCellphone.setCustomerContactId(ogs);
                contactsCellphone.setCustomerContactType("phone");
                contactsCellphone.setCellphoneNumber("521" + p.getCelular());
                ListaContactos.add(contactsCellphone);
            }
            if (p.getEmail() != null) {
                contactsEmail.setCustomerContactId(ogs);
                contactsEmail.setCustomerContactType("email");
                contactsEmail.setEmail(p.getEmail());
                ListaContactos.add(contactsEmail);
            }
        } catch (Exception e) {
            em.close();
            System.out.println("Error al obtener detalles del socio:" + e.getMessage());

        }
        em.close();

        return ListaContactos;
    }

    public List<CustomerAccountDTO> Accounts(String customerId) {
        EntityManager em = AbstractFacade.conexion();
        ogsDTO ogs = Util.ogs(customerId);
        Query query = null;
        String consulta = "SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_siscoop tp USING(idproducto) WHERE "
                + " idorigen = " + ogs.getIdorigen() + " AND idgrupo = " + ogs.getIdgrupo() + " AND idsocio = " + ogs.getIdsocio() + " AND estatus = 2";
        CustomerAccountDTO producto = new CustomerAccountDTO();
        try {
            query = em.createNativeQuery(consulta, Auxiliares.class);
            List<Auxiliares> ListaProd = query.getResultList();
            String status = "";
            String accountType = "";
            Object[] arr = {};
            Object[] arr1 = {"relationCode", "SOW"};
            List<CustomerAccountDTO> listaDeCuentas = new ArrayList<CustomerAccountDTO>();

            for (int k = 0; k < 1; k++) {
                for (int i = 0; i < ListaProd.size(); i++) {
                    Auxiliares a = ListaProd.get(i);
                    System.out.println("IdproductoA:" + a.getAuxiliaresPK().getIdproducto());
                    try {
                        CuentasSiscoop tp = em.find(CuentasSiscoop.class, a.getAuxiliaresPK().getIdproducto());
                        accountType = String.valueOf(tp.getProducttypename().trim().toUpperCase());
                        if (accountType.contains("TIME")) {
                            accountType = "TIME";
                        }
                    } catch (Exception e) {
                        System.out.println("Error producido:" + e.getMessage());
                    }
                    if (a.getEstatus() == 2) {
                        status = "OPEN";
                    } else if (a.getEstatus() == 3) {
                        status = "CLOSED";
                    } else {
                        status = "INACTIVE";
                    }

                    String og = String.format("%06d", a.getIdorigen()) + String.format("%02d", a.getIdgrupo());
                    String s = String.format("%06d", a.getIdsocio());

                    String op = String.format("%06d", a.getAuxiliaresPK().getIdorigenp()) + String.format("%05d", a.getAuxiliaresPK().getIdproducto());
                    String aa = String.format("%08d", a.getAuxiliaresPK().getIdauxiliar());
                    System.out.println("opa:" + op + "," + aa);
                    String cadenaa = aa.substring(4, 8);
                    String cade = "******" + cadenaa;

                    producto = new CustomerAccountDTO(
                            op + aa,
                            op + aa,
                            cade,
                            accountType,
                            "MXN",
                            String.valueOf(a.getAuxiliaresPK().getIdproducto().toString()),
                            status,
                            arr,
                            arr1);
                    listaDeCuentas.add(producto);
                    accountType = "";
                }
            }
            return listaDeCuentas;

        } catch (Exception e) {
            em.close();
            System.out.println("Error al obtener cuentas:" + e.getMessage());
        } finally {
            em.close();
        }

        return null;
    }

    public boolean findCustomer(String ogs) {
        boolean bandera = false;
        EntityManager em = AbstractFacade.conexion();
        ogsDTO id_ogs = Util.ogs(ogs);
        try {
            Query query = em.createNativeQuery("SELECT * FROM personas WHERE "
                    + " p.idorigen = " + id_ogs.getIdorigen() + " AND p.idgrupo = " + id_ogs.getIdgrupo() + " AND p.idsocio = " + id_ogs.getIdsocio());
            if (query != null) {
                bandera = true;
            }
        } catch (Exception e) {
            //cerrar();
            em.close();
        } finally {
            em.close();
        }
        return bandera;
    }

    public String validateSetContactDetails(String customerId, String phone1, String email) {
        EntityManager em = AbstractFacade.conexion();
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        ogsDTO ogs = Util.ogs(customerId);

        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();
        char rndChar = 0;
        String cadena = "";
        for (int i = 0; i < 12; i++) {
            // 0-62 (exclusivo), retorno aleatorio 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            String c = String.valueOf(rndChar);
            cadena = cadena + c;
        }
        String mensaje = "";

        try {

            String validarDatos = "SELECT CASE WHEN telefono IS NOT NULL THEN telefono ELSE 'NO DATA' END "
                    + " CASE WHEN celular IS NOT NULL THEN celular ELSE 'NO DATA' END "
                    + " CASE WHEN email IS NOT NULL THEN email ELSE 'NO DATA' END"
                    + " WHERE idorigenp=" + ogs.getIdorigen()
                    + "       idgrupo=" + ogs.getIdgrupo()
                    + "       idsocio=" + ogs.getIdsocio();
            PersonasPK pk = new PersonasPK(ogs.getIdorigen(), ogs.getIdgrupo(), ogs.getIdsocio());
            Persona p = em.find(Persona.class, pk);

            if (p.getCelular().equals("")) {
                mensaje = mensaje + "SIN CELULAR";
            }
            if (p.getEmail().equals("")) {
                mensaje = mensaje + ",SIN EMAIL";
            }

            System.out.println("mensaje:" + mensaje);
            validaciones_telefono_siscoop validar_datos_contacto = new validaciones_telefono_siscoop();

            System.out.println("telefono:" + email.substring(3, 13));
            if (mensaje.equals("")) {
                validar_datos_contacto = em.find(validaciones_telefono_siscoop.class, customerId);
                if (validar_datos_contacto == null) {
                    if (p.getCelular().equals(phone1.substring(3, 13)) && p.getEmail().equals(email)) {
                        em.getTransaction().begin();
                        int registrosInsertados = em.createNativeQuery("INSERT INTO validaciones_telefonos_siscoop VALUES (?,?,?,?,?)")
                                .setParameter(1, cadena.toUpperCase())
                                .setParameter(2, customerId)
                                .setParameter(3, "521" + p.getCelular())
                                .setParameter(4, "521" + p.getCelular())
                                .setParameter(5, p.getEmail()).executeUpdate();
                        em.getTransaction().commit();
                        System.out.println("Registros insertados:" + registrosInsertados);
                        if (registrosInsertados > 0) {
                            mensaje = cadena;
                        }
                    } else {
                        mensaje = "Los datos no concuerdan con los de la base de datos";
                    }
                } else {
                    if (validar_datos_contacto.getCelular().equals(phone1) && validar_datos_contacto.getEmail().equals(email)) {
                        mensaje = validar_datos_contacto.getValidacion();
                    } else {
                        mensaje = "Ya existe un registro validado para:" + customerId + " pero no son los datos que se esta validando.";
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error en validar datos:" + e.getMessage());
            em.close();
        } finally {
            em.close();
        }

        return mensaje;
    }

    public String executeSetContactDetails(String validationId) {
        EntityManager em = AbstractFacade.conexion();
        String estatus = "";
        try {
            String consulta = "SELECT * FROM validaciones_telefonos_siscoop WHERE validacion='" + validationId + "'";
            System.out.println("consulta:" + consulta);
            Query query = em.createNativeQuery(consulta, validaciones_telefono_siscoop.class);
            validaciones_telefono_siscoop dto = (validaciones_telefono_siscoop) query.getSingleResult();
            if (dto != null) {
                estatus = "completed";
            }
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        } finally {
            em.close();
        }

        return estatus;
    }

    public Double[] position(String customerId) {
        EntityManager em = AbstractFacade.conexion();
        ogsDTO ogs = Util.ogs(customerId);
        double saldo_congelado = 0.0;
        double saldo_disponible = 0.0;
        double saldo_disponible_actual = 0.0;
        try {

            String consulta_productos = "SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_siscoop tp USING(idproducto) INNER JOIN productos p USING (idproducto)"
                    + " WHERE a.idorigen=" + ogs.getIdorigen()
                    + " AND idgrupo=" + ogs.getIdgrupo()
                    + " AND idsocio=" + ogs.getIdsocio()
                    + " AND a.estatus=2 AND tipoproducto in (0,1)";
            System.out.println("Consulta:" + consulta_productos);
            Query query = em.createNativeQuery(consulta_productos, Auxiliares.class);

            List<Auxiliares> lista_productos = query.getResultList();
            boolean bandera = false;
            for (int i = 0; i < lista_productos.size(); i++) {

                Auxiliares a = lista_productos.get(i);
                Productos pr = em.find(Productos.class, a.getAuxiliaresPK().getIdproducto());

                bandera = true;

                //Si es una inversion
                System.out.println("idproducto:" + a.getAuxiliaresPK().getIdproducto() + ",i:" + i + ",Saldo disponible:" + saldo_disponible + ",saldoCongelado:" + saldo_congelado);
                Query query_fecha_servidor = em.createNativeQuery("SELECT date(fechatrabajo) FROM origenes limit 1");
                String fecha_servidor = String.valueOf(query_fecha_servidor.getSingleResult());
                Date fecha_obtenida_servidor_db = stringToDate(fecha_servidor.replace("-", "/"));//fecha obtenida_servidor
                if (pr.getTipoproducto() == 1) {
                    saldo_disponible_actual = saldo_disponible_actual + a.getSaldo().doubleValue();
                    //Se suma fechaactivacion mas plazos para determinar si el producto ya se puede cobrar o aun no
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String fecha_auxiliar = dateFormat.format(a.getFechaactivacion());
                    String calcular_disponibilidad_saldo = "SELECT fechaactivacion + " + Integer.parseInt(String.valueOf(a.getPlazo())) + " FROM auxiliares a WHERE a.idorigenp="
                            + a.getAuxiliaresPK().getIdorigenp()
                            + " AND a.idproducto=" + a.getAuxiliaresPK().getIdproducto()
                            + " AND a.idauxiliar=" + a.getAuxiliaresPK().getIdauxiliar();
                    System.out.println("Calcular disponibilidad:" + calcular_disponibilidad_saldo);
                    Query fecha_disponibilidad_inversion = em.createNativeQuery(calcular_disponibilidad_saldo);
                    String fecha = String.valueOf(fecha_disponibilidad_inversion.getSingleResult()).replace("-", "/");

                    Date fecha_vencimiento_folio = stringToDate(fecha);//fecha vencimiento_folio_auxiliar

                    //si la fecha obtenida es igual al dia actual(hoy) o esta antes: El saldo de la inversion se puede retirar siempre y cuando no este amparando credito
                    //saldoLedgerDPF = saldoLedgerDPF + Double.parseDouble(a.getSaldo().toString());
                    System.out.println("fechaVencimientoFolio:" + fecha_vencimiento_folio);
                    System.out.println("FechaTrabajo:" + fecha_obtenida_servidor_db.toString());

                    if (fecha_vencimiento_folio.equals(fecha_obtenida_servidor_db) || fecha_vencimiento_folio.before(fecha_obtenida_servidor_db)) {
                        //Si ya esta disponible pero esta en garantia

                        if (a.getGarantia().intValue() > 0) {
                            saldo_congelado = saldo_congelado + Double.parseDouble(a.getGarantia().toString());
                            saldo_disponible = saldo_disponible + (Double.parseDouble(a.getSaldo().toString()) - Double.parseDouble(a.getGarantia().toString()));
                        } else {//Si ya se puede retirar la inversion por la fecha y no esta en garantia entonces el saldo ya esta disponible
                            saldo_disponible = saldo_disponible + Double.parseDouble(a.getSaldo().toString());
                        }
                        //Si el dpf aun no se puede retirar
                    } else {
                        saldo_congelado = saldo_congelado + Double.parseDouble(a.getSaldo().toString());
                    }

                } else if (pr.getTipoproducto() == 0) {
                    saldo_disponible_actual = saldo_disponible_actual + a.getSaldo().doubleValue();
                    if (pr.getNombre().toUpperCase().contains("NAVI")) {
                        String fecha = dateToString(fecha_obtenida_servidor_db);
                        if (fecha.substring(5, 7).contains("12")) {
                            if (Double.parseDouble(a.getGarantia().toString()) > 0) {
                                saldo_congelado = saldo_congelado + Double.parseDouble(a.getGarantia().toString());
                                saldo_disponible = saldo_disponible + (Double.parseDouble(a.getSaldo().toString()) - Double.parseDouble(a.getGarantia().toString()));

                            } else {
                                saldo_disponible = saldo_disponible + Double.parseDouble(a.getSaldo().toString());
                            }
                        } else {
                            saldo_congelado = saldo_congelado + a.getSaldo().doubleValue();
                        }
                    } else {
                        if (Double.parseDouble(a.getGarantia().toString()) > 0) {
                            saldo_congelado = saldo_congelado + Double.parseDouble(a.getGarantia().toString());
                            saldo_disponible = saldo_disponible + (Double.parseDouble(a.getSaldo().toString()) - Double.parseDouble(a.getGarantia().toString()));

                        } else {

                            saldo_disponible = saldo_disponible + Double.parseDouble(a.getSaldo().toString());
                        }
                    }
                }
                System.out.println("i:" + i + " ,saldo:" + a.getSaldo() + ",disponible:" + saldo_disponible + ", congelado:" + saldo_congelado);
            }

            System.out.println("El saldo disponible=" + saldo_disponible);
            System.out.println("El saldo congelado=" + saldo_congelado);
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println("Error:" + e.getMessage());
            em.close();
        } finally {
            em.close();
        }
        Double saldos[] = new Double[2];
        saldos[0] = saldo_disponible;
        saldos[1] = saldo_disponible_actual;
        return saldos;
    }

    public List<String[]> positionHistory2(String customerId, String fecha1, String fecha2) {
        EntityManager em = AbstractFacade.conexion();
        ogsDTO ogs = Util.ogs(customerId);
        List<String[]> lista_d = new ArrayList();
        try {
            //Buscamos la lista de movimientos del socio en las fechas que esta ingresando
            String saldo_diario = "SELECT * FROM saldos_diarios(" + ogs.getIdorigen() + "," + ogs.getIdgrupo() + "," + ogs.getIdsocio() + ",'" + fecha1 + "','" + fecha2 + "')";
            System.out.println("Consulta: " + saldo_diario);
            Query sal_dia = em.createNativeQuery(saldo_diario);
            
            List<Object[]>objetos = sal_dia.getResultList();
            for (Object[]ob:objetos) {
                System.out.println("EN LA FECHA " + ob[0] + " EL SALDO DISPONIBLE FUE DE: " + ob[2] + " EL SALDO TOTAL: " + ob[1]);
                String arr[] = new String[3];
                arr[0] = String.valueOf(ob[2]);
                arr[1] = String.valueOf(ob[1]);
                arr[2] = String.valueOf(ob[0]);
                lista_d.add(arr);
            }
        } catch (Exception e) {
            System.out.println("Error en postionHistory:" + e.getMessage());
        }
        return lista_d;
    }

    public List<String[]> positionHistory0(String customerId, String fecha1, String fecha2) {
        EntityManager em = AbstractFacade.conexion();
        ogsDTO ogs = Util.ogs(customerId);
        double ec_saldo_anterior = 0.0, v1 = 0.0, v2 = 0.0, v3 = 0.0, v4 = 0.0, v5 = 0.0, v6 = 0.0, v7 = 0.0, v8 = 0.8;
        int c0 = 00, c01 = 0, c2 = 0, c3 = 0;
        //Estas variables se usan para obtener un arreglo entre 2 fechas
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        List<String[]> lista_d = new ArrayList();
        try {
            //Buscamos la lista lista de auxiliares para el socio que esta ingresando
            String consulta_lista_auxiliares = "SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_siscoop tps USING(idproducto) INNER JOIN productos pr USING(idproducto)"
                    + " WHERE a.idorigen=" + ogs.getIdorigen()
                    + " AND a.idgrupo=" + ogs.getIdgrupo()
                    + " AND a.idsocio=" + ogs.getIdsocio()
                    + " AND a.estatus=2 AND pr.tipoproducto IN (0,1)";
            System.out.println("Consulta: " + consulta_lista_auxiliares);
            Query queryA = em.createNativeQuery(consulta_lista_auxiliares, Auxiliares.class);
            List<Auxiliares> listaAuxiliares = queryA.getResultList();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date fechaInicio = sdf.parse(fecha1);
            Date fechaFinal = sdf.parse(fecha2);

            List<Date> listaFechas = getListaEntreFechas(fechaInicio, fechaFinal);
            List<String> listaOpas = new ArrayList<>();

            //corremos la lista de todos los productos
            int x = 0;
            for (x = 0; x < listaAuxiliares.size(); x++) {
                Auxiliares a = listaAuxiliares.get(x);
                String opa = String.format("%06d", a.getAuxiliaresPK().getIdorigenp()) + "" + String.format("%05d", a.getAuxiliaresPK().getIdproducto()) + "" + String.format("%08d", a.getAuxiliaresPK().getIdauxiliar());
                listaOpas.add(opa);
            }

            //Corremos el arrelgo de fechas
            for (int i = 0; i < listaFechas.size(); i++) {

                String arr[] = new String[3];
                //Obtenemos la fecha en cierta posicion del arreglo
                Date fecha_intermedio = listaFechas.get(i);
                String fechaString = sdf.format(fecha_intermedio);

                String suma_saldo_aux = "SELECT case when sum(saldo)>0 then sum(saldo) else " + v6 + " end FROM auxiliares a inner join auxiliares_d ad using(idorigenp,idproducto,idauxiliar)  inner join tipos_cuenta_siscoop tp "
                        + " using(idproducto) inner join productos p using(idproducto) WHERE "
                        + " a.idorigen=" + ogs.getIdorigen()
                        + " AND idgrupo=" + ogs.getIdgrupo()
                        + " AND idsocio=" + ogs.getIdsocio()
                        + " AND p.tipoproducto in(0) and date(ad.fecha)='" + fechaString + "'";

                String suma_saldo_aux1 = "SELECT sum(saldo) FROM auxiliares a inner join tipos_cuenta_siscoop tp "
                        + " using(idproducto) inner join productos p using(idproducto) WHERE "
                        + " a.idorigen=" + ogs.getIdorigen()
                        + " AND idgrupo=" + ogs.getIdgrupo()
                        + " AND idsocio=" + ogs.getIdsocio()
                        + " AND p.tipoproducto in(0,1) ";

                Query query_suma_saldo = em.createNativeQuery(suma_saldo_aux1);
                double saldo_al_dia = Double.parseDouble(String.valueOf(query_suma_saldo.getSingleResult()));
                v6 = saldo_al_dia;

                //opaDTO opa = null;
                //System.out.println("idorigenp:" + opa.getIdorigenp() + ",idproducto:" + opa.getIdproducto() + ",idauxiliar:" + opa.getIdauxiliar());
                //Corro la lista de todos los productos que el socio
                for (int y = 0; y < listaOpas.size(); y++) {
                    //Deserealizo el opda
                    opaDTO opa = Util.opa(listaOpas.get(y));
                    String fecha_mov = fechaString;
                    String fecha_string_mov = fecha_mov.substring(0, 10).replace("-", "/");

                    //convierto la fecha del movimiento en date
                    Date date_mov = sdf.parse(fecha_string_mov);
                    //Date now = c.getTime();
                    c.setTime(date_mov);
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    c1.setTime(date_mov);
                    c1.add(Calendar.MONTH, -2);
                    Date fecha_mant = c1.getTime();
                    Date fecha_ant = c.getTime();
                    String fecha_mov_anterior = dateToString(fecha_ant);

                    //Obtengo el saldo del movimiento anterior 
                    String busqueda = "SELECT * FROM auxiliares_d WHERE idorigenp=" + opa.getIdorigenp()
                            + " AND idproducto=" + opa.getIdproducto()
                            + " AND idauxiliar=" + opa.getIdauxiliar()
                            + " AND date(fecha) BETWEEN '" + dateToString(fecha_mant) + "' AND '" + fecha_mov_anterior + "' ORDER BY fecha DESC LIMIT 1";
                    boolean b = false;
                    Query monto_ad = null;
                    try {
                        Query querycv = em.createNativeQuery(busqueda, AuxiliaresD.class);
                        AuxiliaresD add = (AuxiliaresD) querycv.getSingleResult();
                        //Busco todo los movimientos en auxiliareS_d para la fecha en el arreglo de fechas
                        String busqueda_movimientos = "SELECT case when sum(case when cargoabono=0 then -monto else monto end)!=0 then sum(case when cargoabono=0 then -monto else monto end) else 0 end  as monto FROM auxiliares_d"
                                + " WHERE idorigenp=" + opa.getIdorigenp()
                                + " AND idproducto=" + opa.getIdproducto()
                                + " AND idauxiliar=" + opa.getIdauxiliar()
                                + " AND date(fecha)='" + fechaString + "'";

                        monto_ad = em.createNativeQuery(busqueda_movimientos);
                        v7 = Double.parseDouble(String.valueOf(monto_ad.getSingleResult()));
                        ec_saldo_anterior = add.getSaldoec().doubleValue();
                        b = true;
                    } catch (Exception e) {
                    }

                    // System.out.println("el saldo ec anteriror del producto:" + add.getAuxiliaresDPK().getIdproducto() + " es:" + ec_saldo_anterior + " la fecha es:" + add.getAuxiliaresDPK().getFecha());
                    if (c0 > 2) {
                        if (b) {
                            v4 = v4 + Double.parseDouble(String.valueOf(monto_ad.getSingleResult()));
                        }

                    } else {
                        if (b) {
                            v1 = v1 + ec_saldo_anterior + Double.parseDouble(String.valueOf(monto_ad.getSingleResult()));
                            v2 = v2 + v1;
                            v1 = 0.0;
                            c0 = c0 + 1;
                        }

                    }

                }//Termina el recorrido de los opas
                if (c0 <= 2) {
                    v4 = v2;
                }

                System.out.println("entonces en la fecha " + fecha_intermedio + " el saldo disponible fue de:" + v4 + " el saldo actual:" + saldo_al_dia);
                arr[0] = String.valueOf(v4);
                arr[1] = String.valueOf(saldo_al_dia);
                arr[2] = fechaString;
                lista_d.add(arr);
            }//termina el recorrido de fechas
        } catch (Exception e) {
            System.out.println("Error en postionHistory:" + e.getMessage());
        }
        return lista_d;
    }

    public List<Double[]> positionHistory1(String customerId, String fecha1, String fecha2) {
        EntityManager em = AbstractFacade.conexion();
        ogsDTO ogs = Util.ogs(customerId);
        Double ledGer = 0.0, avalaible = 0.0, saldo_congelado = 0.0, saldo_disponible = 0.0;
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        try {
            //Buscamos la lista lista de auxiliares para el socio que esta ingresando
            String consulta_lista_auxiliares = "SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_siscoop tps USING(idproducto) INNER JOIN productos pr USING(idproducto)"
                    + " WHERE a.idorigen=" + ogs.getIdorigen()
                    + " AND a.idgrupo=" + ogs.getIdgrupo()
                    + " AND a.idsocio=" + ogs.getIdsocio()
                    + " AND a.estatus=2 AND pr.tipoproducto IN (0)";
            System.out.println("Consulta: " + consulta_lista_auxiliares);
            Query queryA = em.createNativeQuery(consulta_lista_auxiliares, Auxiliares.class);
            List<Auxiliares> listaAuxiliares = queryA.getResultList();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date fechaInicio = sdf.parse(fecha1);
            Date fechaFinal = sdf.parse(fecha2);
            List<Date> listaFechas = getListaEntreFechas(fechaInicio, fechaFinal);
            List<String> listaOpas = new ArrayList<>();
            double saldo_ec_anterior = 0.0;
            double saldo_al_dia_actual = 0.0;
            double saldo_sin_mov = 0.0;
            //corremos la lista de todos los productos
            int x = 0;
            for (x = 0; x < listaAuxiliares.size(); x++) {
                Auxiliares a = listaAuxiliares.get(x);
                String opa = String.format("%06d", a.getAuxiliaresPK().getIdorigenp()) + "" + String.format("%05d", a.getAuxiliaresPK().getIdproducto()) + "" + String.format("%08d", a.getAuxiliaresPK().getIdauxiliar());
                listaOpas.add(opa);
            }
            boolean b = false;
            for (int i = 0; i < listaFechas.size(); i++) {
                double saldo_actual = 0.0;
                Date fe = listaFechas.get(i);
                String fechaString = sdf.format(fe);
                //opaDTO opa = null;
                //System.out.println("idorigenp:" + opa.getIdorigenp() + ",idproducto:" + opa.getIdproducto() + ",idauxiliar:" + opa.getIdauxiliar());
                for (int y = 0; y < listaOpas.size(); y++) {

                    opaDTO opa = Util.opa(listaOpas.get(y));
                    String fecha_mov = fechaString;
                    String fecha_string_mov = fecha_mov.substring(0, 10).replace("-", "/");

                    Date date_mov = sdf.parse(fecha_string_mov);
                    //Date now = c.getTime();
                    c.setTime(date_mov);
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    c1.setTime(date_mov);
                    c1.add(Calendar.MONTH, -2);
                    Date fecha_mant = c1.getTime();
                    Date fecha_ant = c.getTime();
                    String fecha_mov_anterior = dateToString(fecha_ant);

                    //Obtengo el saldo del movimiento anterior 
                    String busqueda = "SELECT * FROM auxiliares_d WHERE idorigenp=" + opa.getIdorigenp()
                            + " AND idproducto=" + opa.getIdproducto()
                            + " AND idauxiliar=" + opa.getIdauxiliar()
                            + " AND date(fecha) BETWEEN '" + dateToString(fecha_mant) + "' AND '" + fecha_mov_anterior + "' ORDER BY fecha DESC LIMIT 1";
                    System.out.println("BUSQUEDA: " + busqueda);

                    Query querycv = em.createNativeQuery(busqueda, AuxiliaresD.class);
                    AuxiliaresD add = (AuxiliaresD) querycv.getSingleResult();

                    //Busco todo los movimientos en auxiliareS_d
                    String busqueda_movimientos = "SELECT (CASE WHEN sum(monto)>0 THEN sum(monto) ELSE 0 END) as monto FROM auxiliares_d"
                            + " WHERE idorigenp=" + opa.getIdorigenp()
                            + " AND idproducto=" + opa.getIdproducto()
                            + " AND idauxiliar=" + opa.getIdauxiliar()
                            + " AND date(fecha)='" + fechaString + "'";
                    System.out.println("BUSQUEDA MOVIMIENTO: " + busqueda_movimientos);
                    Query queryAuxiliares_d = em.createNativeQuery(busqueda_movimientos);

                    saldo_ec_anterior = add.getSaldoec().doubleValue();
                    System.out.println("SALDO COMIENZO: " + saldo_ec_anterior);

                    double saldoTotal = Double.parseDouble(String.valueOf(queryAuxiliares_d.getSingleResult()));
                    if (saldoTotal > 0) {
                        b = true;
                        System.out.println("entro el saldo disponible es:" + saldo_disponible);
                        //System.out.println("busqueda:"+busqueda_movimientos);
                        System.out.println("el saldo ec del movimiento anterior es:" + add.getSaldoec());

                        saldo_actual = saldo_disponible + saldo_ec_anterior + saldoTotal;
                        //saldo_congelado=saldo_congelado+saldo_disponible;
                        System.out.println("si hubo movimientos en la fecha y se incremento el saldo anterior era:" + saldo_ec_anterior + " ahora el nuevo saldo es:" + saldo_actual);
                    } else {
                        System.out.println("ledger entro con: " + ledGer);
                        ledGer = saldo_ec_anterior;
                    }

                    if (b) {
                        saldo_disponible = saldo_actual;
                        saldo_actual = 0.0;
                    }

                }

                if (b) {
                    saldo_congelado = saldo_congelado + saldo_disponible;
                    System.out.println(" el dia " + fechaString + " el saldo disponible fue de:" + saldo_congelado);
                } else {
                    saldo_sin_mov = saldo_sin_mov + ledGer;
                    System.out.println(" el dia " + fechaString + " el saldo disponible fue de:" + saldo_sin_mov);
                    ledGer = 0.0;
                }
                b = false;

            }

            System.out.println("FechaInicio:" + fechaInicio + ",fechaFinal:" + fechaFinal);
        } catch (Exception e) {

        }
        return null;
    }

    public List<Date> getListaEntreFechas(Date fechaInicio, Date fechaFin) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(fechaInicio);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaFin);
        List<Date> listaFechas = new ArrayList<Date>();
        while (!c1.after(c2)) {
            listaFechas.add(c1.getTime());
            c1.add(Calendar.DAY_OF_MONTH, 1);
        }
        return listaFechas;
    }

    public String dateToString(Date cadena) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String cadenaStr = sdf.format(cadena);
        return cadenaStr;
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
