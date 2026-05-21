package pe.edu.utp.condominio.api.dominios.paqueteria.dto.response;

import java.time.LocalDateTime;
import pe.edu.utp.condominio.api.dominios.paqueteria.enums.EstadoPaquete;

public class PaqueteResponse {

    private Long id;
    private Long unidadId;
    private String remitente;
    private String destinatario;
    private EstadoPaquete estado;
    private LocalDateTime fechaRecepcion;
    private LocalDateTime fechaEntrega;
    private String observacion;

    public PaqueteResponse() {
    }

    public PaqueteResponse(Long id, Long unidadId, String remitente, String destinatario,
            EstadoPaquete estado, LocalDateTime fechaRecepcion, LocalDateTime fechaEntrega,
            String observacion) {
        this.id = id;
        this.unidadId = unidadId;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.estado = estado;
        this.fechaRecepcion = fechaRecepcion;
        this.fechaEntrega = fechaEntrega;
        this.observacion = observacion;
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

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public EstadoPaquete getEstado() {
        return estado;
    }

    public void setEstado(EstadoPaquete estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
