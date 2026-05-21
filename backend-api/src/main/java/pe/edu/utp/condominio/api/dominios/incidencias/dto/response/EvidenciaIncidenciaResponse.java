package pe.edu.utp.condominio.api.dominios.incidencias.dto.response;

import java.time.LocalDateTime;

public class EvidenciaIncidenciaResponse {

    private Long id;
    private Long incidenciaId;
    private String urlArchivo;
    private LocalDateTime fechaRegistro;

    public EvidenciaIncidenciaResponse() {
    }

    public EvidenciaIncidenciaResponse(Long id, Long incidenciaId, String urlArchivo,
            LocalDateTime fechaRegistro) {
        this.id = id;
        this.incidenciaId = incidenciaId;
        this.urlArchivo = urlArchivo;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIncidenciaId() {
        return incidenciaId;
    }

    public void setIncidenciaId(Long incidenciaId) {
        this.incidenciaId = incidenciaId;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
