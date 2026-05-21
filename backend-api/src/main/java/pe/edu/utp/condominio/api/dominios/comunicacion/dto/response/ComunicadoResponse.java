package pe.edu.utp.condominio.api.dominios.comunicacion.dto.response;

import java.time.LocalDateTime;

public class ComunicadoResponse {

    private Long id;
    private Long condominioId;
    private String titulo;
    private String contenido;
    private LocalDateTime fechaPublicacion;

    public ComunicadoResponse() {
    }

    public ComunicadoResponse(Long id, Long condominioId, String titulo, String contenido,
            LocalDateTime fechaPublicacion) {
        this.id = id;
        this.condominioId = condominioId;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
}
