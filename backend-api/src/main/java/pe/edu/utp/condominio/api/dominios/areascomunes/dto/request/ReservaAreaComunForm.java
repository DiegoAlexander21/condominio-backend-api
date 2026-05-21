package pe.edu.utp.condominio.api.dominios.areascomunes.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReservaAreaComunForm {

    @NotNull(message = "Debe seleccionar un area comun.")
    private Long areaComunId;

    @NotNull(message = "Debe seleccionar una unidad.")
    private Long unidadId;

    @NotNull(message = "La fecha de reserva es obligatoria.")
    private LocalDate fechaReserva;

    @NotNull(message = "La hora de inicio es obligatoria.")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria.")
    private LocalTime horaFin;

    @NotBlank(message = "El nombre del responsable es obligatorio.")
    @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres.")
    private String responsableNombre;

    public ReservaAreaComunForm() {
    }

    public Long getAreaComunId() {
        return areaComunId;
    }

    public void setAreaComunId(Long areaComunId) {
        this.areaComunId = areaComunId;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
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

    public String getResponsableNombre() {
        return responsableNombre;
    }

    public void setResponsableNombre(String responsableNombre) {
        this.responsableNombre = responsableNombre;
    }
}
