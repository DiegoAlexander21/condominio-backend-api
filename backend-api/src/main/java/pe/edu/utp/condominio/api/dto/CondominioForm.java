package pe.edu.utp.condominio.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CondominioForm {

    @NotBlank(message = "El nombre del condominio es obligatorio.")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres.")
    private String nombre;

    @Min(value = 1, message = "El numero de torres debe ser mayor a cero.")
    @Max(value = 100, message = "El numero de torres no puede ser mayor a 100.")
    private Integer torres;

    @Min(value = 1, message = "El numero de pisos por torre debe ser mayor a cero.")
    @Max(value = 200, message = "El numero de pisos por torre no puede ser mayor a 200.")
    private Integer pisosPorTorre;

    public CondominioForm() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTorres() {
        return torres;
    }

    public void setTorres(Integer torres) {
        this.torres = torres;
    }

    public Integer getPisosPorTorre() {
        return pisosPorTorre;
    }

    public void setPisosPorTorre(Integer pisosPorTorre) {
        this.pisosPorTorre = pisosPorTorre;
    }
}
