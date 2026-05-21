package pe.edu.utp.condominio.api.dominios.visitas.dto.response;

import java.time.LocalDateTime;
import pe.edu.utp.condominio.api.dominios.visitas.enums.EstadoVisita;

public class VisitaResponse {

    private Long id;
    private Long unidadId;
    private String nombreVisitante;
    private String documentoVisitante;
    private LocalDateTime fechaVisitaProgramada;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaSalida;
    private EstadoVisita estado;
    private LocalDateTime fechaRegistro;

    public VisitaResponse() {
    }

    public VisitaResponse(Long id, Long unidadId, String nombreVisitante, String documentoVisitante,
            LocalDateTime fechaVisitaProgramada, LocalDateTime fechaIngreso, LocalDateTime fechaSalida,
            EstadoVisita estado, LocalDateTime fechaRegistro) {
        this.id = id;
        this.unidadId = unidadId;
        this.nombreVisitante = nombreVisitante;
        this.documentoVisitante = documentoVisitante;
        this.fechaVisitaProgramada = fechaVisitaProgramada;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public String getNombreVisitante() {
        return nombreVisitante;
    }

    public void setNombreVisitante(String nombreVisitante) {
        this.nombreVisitante = nombreVisitante;
    }

    public String getDocumentoVisitante() {
        return documentoVisitante;
    }

    public void setDocumentoVisitante(String documentoVisitante) {
        this.documentoVisitante = documentoVisitante;
    }

    public LocalDateTime getFechaVisitaProgramada() {
        return fechaVisitaProgramada;
    }

    public void setFechaVisitaProgramada(LocalDateTime fechaVisitaProgramada) {
        this.fechaVisitaProgramada = fechaVisitaProgramada;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public EstadoVisita getEstado() {
        return estado;
    }

    public void setEstado(EstadoVisita estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
