package pe.edu.utp.condominio.api.dominios.condominio.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CondominioForm {

    private Long id;

    @NotBlank(message = "El nombre del condominio es obligatorio.")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres.")
    private String nombre;

    @Min(value = 1, message = "El numero de torres debe ser mayor a cero.")
    private Integer torres;

    @Min(value = 1, message = "El numero de pisos por torre debe ser mayor a cero.")
    private Integer pisosPorTorre;

    public CondominioForm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
