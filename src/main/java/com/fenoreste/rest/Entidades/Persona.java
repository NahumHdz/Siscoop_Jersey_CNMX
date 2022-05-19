package com.fenoreste.rest.Entidades;

import com.fenoreste.rest.Entidades.PersonasPK;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Cacheable(false)
@Entity
@Table(name = "personas")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected PersonasPK personasPK;

    @Column(name = "calle")
    private String calle;
    @Column(name = "numeroext")
    private String numeroext;
    @Column(name = "numeroint")
    private String numeroint;
    @Column(name = "entrecalles")
    private String entrecalles;
    @Column(name = "fechanacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechanacimiento;
    @Column(name = "lugarnacimiento")
    private String lugarnacimiento;
    @Column(name = "efnacimiento")
    private Integer efnacimiento;
    @Column(name = "sexo")
    private Short sexo;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "telefonorecados")
    private String telefonorecados;
    @Column(name = "listanegra")
    private Boolean listanegra;
    @Column(name = "estadocivil")
    private Short estadocivil;
    @Column(name = "idcoop")
    private String idcoop;
    @Column(name = "idsector")
    private Integer idsector;
    @Column(name = "estatus")
    private Boolean estatus;
    @Column(name = "aceptado")
    private Boolean aceptado;
    @Column(name = "fechaingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaingreso;
    @Column(name = "fecharetiro")
    @Temporal(TemporalType.DATE)
    private Date fecharetiro;
    @Column(name = "fechaciudad")
    @Temporal(TemporalType.DATE)
    private Date fechaciudad;
    @Column(name = "regimen_mat")
    private Short regimenMat;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "medio_inf")
    private Short medioInf;
    @Column(name = "requisitos")
    private Integer requisitos;
    @Column(name = "appaterno")
    private String appaterno;
    @Column(name = "apmaterno")
    private String apmaterno;
    @Column(name = "nacionalidad")
    private Short nacionalidad;
    @Column(name = "grado_estudios")
    private Short gradoEstudios;
    @Column(name = "categoria")
    private Short categoria;
    @Column(name = "rfc")
    private String rfc;
    @Column(name = "curp")
    private String curp;
    @Column(name = "email")
    private String email;
    @Column(name = "razon_social")
    private String razonSocial;
    @Column(name = "causa_baja")
    private Integer causaBaja;
    @Column(name = "nivel_riesgo")
    private Short nivelRiesgo;
    @Column(name = "celular")
    private String celular;
    @Column(name = "rfc_valido")
    private Boolean rfcValido;
    @Column(name = "curp_valido")
    private Boolean curpValido;
    @Column(name = "idcolonia")
    private Integer idcolonia;

    public Persona() {
    }

    public Persona(PersonasPK personasPK) {
        this.personasPK = personasPK;
    }

    public Persona(int idorigen, int idgrupo, int idsocio) {
        this.personasPK = new PersonasPK(idorigen, idgrupo, idsocio);
    }

    public PersonasPK getPersonasPK() {
        return this.personasPK;
    }

    public void setPersonasPK(PersonasPK personasPK) {
        this.personasPK = personasPK;
    }

    public String getCalle() {
        return this.calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroext() {
        return this.numeroext;
    }

    public void setNumeroext(String numeroext) {
        this.numeroext = numeroext;
    }

    public String getNumeroint() {
        return this.numeroint;
    }

    public void setNumeroint(String numeroint) {
        this.numeroint = numeroint;
    }

    public String getEntrecalles() {
        return this.entrecalles;
    }

    public void setEntrecalles(String entrecalles) {
        this.entrecalles = entrecalles;
    }

    public Date getFechanacimiento() {
        return this.fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getLugarnacimiento() {
        return this.lugarnacimiento;
    }

    public void setLugarnacimiento(String lugarnacimiento) {
        this.lugarnacimiento = lugarnacimiento;
    }

    public Integer getEfnacimiento() {
        return this.efnacimiento;
    }

    public void setEfnacimiento(Integer efnacimiento) {
        this.efnacimiento = efnacimiento;
    }

    public Short getSexo() {
        return this.sexo;
    }

    public void setSexo(Short sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefonorecados() {
        return this.telefonorecados;
    }

    public void setTelefonorecados(String telefonorecados) {
        this.telefonorecados = telefonorecados;
    }

    public Boolean getListanegra() {
        return this.listanegra;
    }

    public void setListanegra(Boolean listanegra) {
        this.listanegra = listanegra;
    }

    public Short getEstadocivil() {
        return this.estadocivil;
    }

    public void setEstadocivil(Short estadocivil) {
        this.estadocivil = estadocivil;
    }

    public String getIdcoop() {
        return this.idcoop;
    }

    public void setIdcoop(String idcoop) {
        this.idcoop = idcoop;
    }

    public Integer getIdsector() {
        return this.idsector;
    }

    public void setIdsector(Integer idsector) {
        this.idsector = idsector;
    }

    public Boolean getEstatus() {
        return this.estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }

    public Boolean getAceptado() {
        return this.aceptado;
    }

    public void setAceptado(Boolean aceptado) {
        this.aceptado = aceptado;
    }

    public Date getFechaingreso() {
        return this.fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Date getFecharetiro() {
        return this.fecharetiro;
    }

    public void setFecharetiro(Date fecharetiro) {
        this.fecharetiro = fecharetiro;
    }

    public Date getFechaciudad() {
        return this.fechaciudad;
    }

    public void setFechaciudad(Date fechaciudad) {
        this.fechaciudad = fechaciudad;
    }

    public Short getRegimenMat() {
        return this.regimenMat;
    }

    public void setRegimenMat(Short regimenMat) {
        this.regimenMat = regimenMat;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Short getMedioInf() {
        return this.medioInf;
    }

    public void setMedioInf(Short medioInf) {
        this.medioInf = medioInf;
    }

    public Integer getRequisitos() {
        return this.requisitos;
    }

    public void setRequisitos(Integer requisitos) {
        this.requisitos = requisitos;
    }

    public String getAppaterno() {
        return this.appaterno;
    }

    public void setAppaterno(String appaterno) {
        this.appaterno = appaterno;
    }

    public String getApmaterno() {
        return this.apmaterno;
    }

    public void setApmaterno(String apmaterno) {
        this.apmaterno = apmaterno;
    }

    public Short getNacionalidad() {
        return this.nacionalidad;
    }

    public void setNacionalidad(Short nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Short getGradoEstudios() {
        return this.gradoEstudios;
    }

    public void setGradoEstudios(Short gradoEstudios) {
        this.gradoEstudios = gradoEstudios;
    }

    public Short getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Short categoria) {
        this.categoria = categoria;
    }

    public String getRfc() {
        return this.rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getCurp() {
        return this.curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRazonSocial() {
        return this.razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Integer getCausaBaja() {
        return this.causaBaja;
    }

    public void setCausaBaja(Integer causaBaja) {
        this.causaBaja = causaBaja;
    }

    public Short getNivelRiesgo() {
        return this.nivelRiesgo;
    }

    public void setNivelRiesgo(Short nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public String getCelular() {
        return this.celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Boolean getRfcValido() {
        return this.rfcValido;
    }

    public void setRfcValido(Boolean rfcValido) {
        this.rfcValido = rfcValido;
    }

    public Boolean getCurpValido() {
        return this.curpValido;
    }

    public void setCurpValido(Boolean curpValido) {
        this.curpValido = curpValido;
    }

    public Integer getIdcolonia() {
        return this.idcolonia;
    }

    public void setIdcolonia(Integer idcolonia) {
        this.idcolonia = idcolonia;
    }

    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.personasPK);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        com.fenoreste.rest.Entidades.Persona other = (com.fenoreste.rest.Entidades.Persona) obj;
        return Objects.equals(this.personasPK, other.personasPK);
    }

    public String toString() {
        /* 461 */ return "Personas{personasPK=" + this.personasPK + ", calle=" + this.calle + ", numeroext=" + this.numeroext + ", numeroint=" + this.numeroint + ", entrecalles=" + this.entrecalles + ", fechanacimiento=" + this.fechanacimiento + ", lugarnacimiento=" + this.lugarnacimiento + ", efnacimiento=" + this.efnacimiento + ", sexo=" + this.sexo + ", telefono=" + this.telefono + ", telefonorecados=" + this.telefonorecados + ", listanegra=" + this.listanegra + ", estadocivil=" + this.estadocivil + ", idcoop=" + this.idcoop + ", idsector=" + this.idsector + ", estatus=" + this.estatus + ", aceptado=" + this.aceptado + ", fechaingreso=" + this.fechaingreso + ", fecharetiro=" + this.fecharetiro + ", fechaciudad=" + this.fechaciudad + ", regimenMat=" + this.regimenMat + ", nombre=" + this.nombre + ", medioInf=" + this.medioInf + ", requisitos=" + this.requisitos + ", appaterno=" + this.appaterno + ", apmaterno=" + this.apmaterno + ", nacionalidad=" + this.nacionalidad + ", gradoEstudios=" + this.gradoEstudios + ", categoria=" + this.categoria + ", rfc=" + this.rfc + ", curp=" + this.curp + ", email=" + this.email + ", razonSocial=" + this.razonSocial + ", causaBaja=" + this.causaBaja + ", nivelRiesgo=" + this.nivelRiesgo + ", celular=" + this.celular + ", rfcValido=" + this.rfcValido + ", curpValido=" + this.curpValido + ", idcolonia=" + this.idcolonia + '}';
        /*     */    }
    /*     */ }
