package pe.edu.utp.condominio.api.dominios.areascomunes.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservaAreaComunResponse {

    private Long id;
    private Long areaComunId;
    private Long unidadId;
    private String unidadNumero;
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String responsableNombre;
    private LocalDateTime fechaRegistro;

    public ReservaAreaComunResponse() {
    }

    public ReservaAreaComunResponse(Long id, Long areaComunId, Long unidadId, LocalDate fechaReserva,
            LocalTime horaInicio, LocalTime horaFin, String responsableNombre,
            LocalDateTime fechaRegistro) {
        this.id = id;
        this.areaComunId = areaComunId;
        this.unidadId = unidadId;
        this.fechaReserva = fechaReserva;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.responsableNombre = responsableNombre;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getUnidadNumero() {
        return unidadNumero;
    }

    public void setUnidadNumero(String unidadNumero) {
        this.unidadNumero = unidadNumero;
    }
}
