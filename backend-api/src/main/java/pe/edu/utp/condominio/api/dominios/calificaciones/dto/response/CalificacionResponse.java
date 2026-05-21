package pe.edu.utp.condominio.api.dominios.calificaciones.dto.response;

import java.time.LocalDateTime;

public class CalificacionResponse {

    private Long id;
    private String nombreArea;
    private String unidadIdentificador;
    private int puntaje;
    private String comentario;
    private LocalDateTime fechaRegistro;

    public CalificacionResponse() {}

    public CalificacionResponse(Long id, String nombreArea, String unidadIdentificador, int puntaje, String comentario, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombreArea = nombreArea;
        this.unidadIdentificador = unidadIdentificador;
        this.puntaje = puntaje;
        this.comentario = comentario;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String nombreArea) { this.nombreArea = nombreArea; }
    public String getUnidadIdentificador() { return unidadIdentificador; }
    public void setUnidadIdentificador(String unidadIdentificador) { this.unidadIdentificador = unidadIdentificador; }
    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
