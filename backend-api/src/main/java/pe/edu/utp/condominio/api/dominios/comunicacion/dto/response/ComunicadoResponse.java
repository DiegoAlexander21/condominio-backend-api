package pe.edu.utp.condominio.api.dominios.comunicacion.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import pe.edu.utp.condominio.api.dominios.comunicacion.dto.ComunicadoTorreDto;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.AlcanceComunicado;

public class ComunicadoResponse {

    private Long id;
    private AlcanceComunicado alcance;
    private List<Long> condominioIds;
    private List<ComunicadoTorreDto> torres;
    private List<Long> unidadIds;
    private String titulo;
    private String contenido;
    private LocalDateTime fechaPublicacion;

    public ComunicadoResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlcanceComunicado getAlcance() {
        return alcance;
    }

    public void setAlcance(AlcanceComunicado alcance) {
        this.alcance = alcance;
    }

    public List<Long> getCondominioIds() {
        return condominioIds;
    }

    public void setCondominioIds(List<Long> condominioIds) {
        this.condominioIds = condominioIds;
    }

    public List<ComunicadoTorreDto> getTorres() {
        return torres;
    }

    public void setTorres(List<ComunicadoTorreDto> torres) {
        this.torres = torres;
    }

    public List<Long> getUnidadIds() {
        return unidadIds;
    }

    public void setUnidadIds(List<Long> unidadIds) {
        this.unidadIds = unidadIds;
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
