package pe.edu.utp.condominio.api.dominios.comunicacion.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.EstadoAsamblea;

public class AsambleaResponse {

    private Long id;
    private Long condominioId;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private EstadoAsamblea estado;
    private List<OpcionVotacionResponse> opciones;

    public AsambleaResponse() {
    }

    public AsambleaResponse(Long id, Long condominioId, String titulo, String descripcion,
            LocalDateTime fechaInicio, LocalDateTime fechaFin, EstadoAsamblea estado,
            List<OpcionVotacionResponse> opciones) {
        this.id = id;
        this.condominioId = condominioId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.opciones = opciones;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoAsamblea getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsamblea estado) {
        this.estado = estado;
    }

    public List<OpcionVotacionResponse> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionVotacionResponse> opciones) {
        this.opciones = opciones;
    }
}
