package pe.edu.utp.condominio.api.dominios.unidades.dto.request;

import jakarta.validation.constraints.Size;

public class AsignarOcupantesForm {

    private Long id;

    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres.")
    private String nombrePropietario;

    @Size(max = 8, message = "El DNI no debe exceder los 8 digitos.")
    private String dniPropietario;

    private String emailPropietario;

    private String telefonoPropietario;

    private String nombreResidente;
    private String emailResidente;
    private String dniResidente;
    private String parentesco;
    private boolean residenteActivo;

    public AsignarOcupantesForm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getDniPropietario() {
        return dniPropietario;
    }

    public void setDniPropietario(String dniPropietario) {
        this.dniPropietario = dniPropietario;
    }

    public String getEmailPropietario() {
        return emailPropietario;
    }

    public void setEmailPropietario(String emailPropietario) {
        this.emailPropietario = emailPropietario;
    }

    public String getTelefonoPropietario() {
        return telefonoPropietario;
    }

    public void setTelefonoPropietario(String telefonoPropietario) {
        this.telefonoPropietario = telefonoPropietario;
    }

    public String getNombreResidente() {
        return nombreResidente;
    }

    public void setNombreResidente(String nombreResidente) {
        this.nombreResidente = nombreResidente;
    }

    public String getEmailResidente() {
        return emailResidente;
    }

    public void setEmailResidente(String emailResidente) {
        this.emailResidente = emailResidente;
    }

    public String getDniResidente() {
        return dniResidente;
    }

    public void setDniResidente(String dniResidente) {
        this.dniResidente = dniResidente;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public boolean isResidenteActivo() {
        return residenteActivo;
    }

    public void setResidenteActivo(boolean residenteActivo) {
        this.residenteActivo = residenteActivo;
    }
}
