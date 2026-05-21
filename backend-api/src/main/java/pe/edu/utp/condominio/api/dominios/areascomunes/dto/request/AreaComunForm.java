package pe.edu.utp.condominio.api.dominios.areascomunes.dto.request;

import java.time.LocalTime;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AreaComunForm {

    private Long id;

    @NotNull(message = "Debe seleccionar un condominio.")
    private Long condominioId;

    @NotBlank(message = "El nombre del area comun es obligatorio.")
    @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres.")
    private String nombre;

    @NotNull(message = "La capacidad es obligatoria.")
    @Min(value = 1, message = "La capacidad debe ser mayor a cero.")
    @Max(value = 1000, message = "La capacidad no puede ser mayor a 1000.")
    private Integer capacidad;

    @NotNull(message = "La hora de inicio es obligatoria.")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria.")
    private LocalTime horaFin;

    @Size(max = 1000, message = "Las normas de uso no pueden superar los 1000 caracteres.")
    private String normasUso;

    public AreaComunForm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCondominioId() {
        return condominioId;
    }

    public void setCondominioId(Long condominioId) {
        this.condominioId = condominioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getNormasUso() {
        return normasUso;
    }

    public void setNormasUso(String normasUso) {
        this.normasUso = normasUso;
    }
}
