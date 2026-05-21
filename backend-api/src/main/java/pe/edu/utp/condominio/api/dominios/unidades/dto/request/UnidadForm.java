package pe.edu.utp.condominio.api.dominios.unidades.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UnidadForm {

    private Long id;

    @NotBlank(message = "Debe seleccionar un condominio.")
    private String nombreCondominio;

    @NotBlank(message = "El numero de unidad es obligatorio.")
    @Size(min = 1, max = 20, message = "El numero de unidad debe tener entre 1 y 20 caracteres.")
    private String numeroUnidad;

    @NotBlank(message = "La torre no puede estar vacia.")
    @Size(min = 1, max = 10, message = "La torre debe tener entre 1 y 10 caracteres.")
    private String torre;

    @NotNull(message = "El piso es obligatorio.")
    @Min(value = 1, message = "El piso debe ser mayor a cero.")
    @Max(value = 200, message = "El piso no puede ser mayor a 200.")
    private Integer piso;

    @NotNull(message = "El area es obligatoria.")
    @DecimalMin(value = "1.0", message = "El area debe ser mayor a cero.")
    private Double area;

    @NotBlank(message = "El nombre del propietario es obligatorio.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    private String nombrePropietario;

    @NotBlank(message = "El DNI del propietario es obligatorio.")
    @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 digitos.")
    private String dniPropietario;

    private String emailPropietario;
    private String telefonoPropietario;
    private String nombreResidente;
    private String emailResidente;
    private String dniResidente;
    private String parentesco;
    private boolean residenteActivo;

    public UnidadForm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCondominio() {
        return nombreCondominio;
    }

    public void setNombreCondominio(String nombreCondominio) {
        this.nombreCondominio = nombreCondominio;
    }

    public String getNumeroUnidad() {
        return numeroUnidad;
    }

    public void setNumeroUnidad(String numeroUnidad) {
        this.numeroUnidad = numeroUnidad;
    }

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
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
