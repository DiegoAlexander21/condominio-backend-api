package pe.edu.utp.condominio.api.dominios.finanzas.dto.response;

import java.time.LocalDateTime;

public class EvidenciaPagoResponse {

    private Long id;
    private Long pagoId;
    private String urlArchivo;
    private LocalDateTime fechaRegistro;

    public EvidenciaPagoResponse() {
    }

    public EvidenciaPagoResponse(Long id, Long pagoId, String urlArchivo, LocalDateTime fechaRegistro) {
        this.id = id;
        this.pagoId = pagoId;
        this.urlArchivo = urlArchivo;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPagoId() {
        return pagoId;
    }

    public void setPagoId(Long pagoId) {
        this.pagoId = pagoId;
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
