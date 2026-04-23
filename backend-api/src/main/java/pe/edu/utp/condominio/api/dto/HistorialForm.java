package pe.edu.utp.condominio.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class HistorialForm {

    @Min(value = 1, message = "El ID del departamento debe ser un número positivo")
    private Long departamentoId;

    @NotBlank(message = "El nombre del dueño anterior es obligatorio")
    @Size(min = 3, max = 70, message = "El nombre del dueño anterior debe tener entre 3 y 70 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre del dueño anterior solo puede contener letras y espacios")
    private String propietarioAnterior;


    @NotBlank(message = "El nombre del dueño actual es obligatorio")
    @Size(min = 3, max = 70, message = "El nombre del dueño actual debe tener entre 3 y 70 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre del dueño actual solo puede contener letras y espacios")
    private String nuevoPropietario;

    public HistorialForm() {
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }
    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getPropietarioAnterior() {
        return propietarioAnterior;
    }

    public void setPropietarioAnterior(String propietarioAnterior) {
        this.propietarioAnterior = propietarioAnterior;
    }

    public String getNuevoPropietario() {
        return nuevoPropietario;
    }
    public void setNuevoPropietario(String nuevoPropietario) {
        this.nuevoPropietario = nuevoPropietario;
    }
}