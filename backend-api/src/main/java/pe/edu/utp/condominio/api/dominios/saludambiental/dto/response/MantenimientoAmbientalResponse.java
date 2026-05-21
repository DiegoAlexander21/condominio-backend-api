package pe.edu.utp.condominio.api.dominios.saludambiental.dto.response;

import java.time.LocalDateTime;

public class MantenimientoAmbientalResponse {

    private Long id;
    private String nombreArea;
    private String descripcion;
    private LocalDateTime fechaRegistro;
    private String responsable;

    public MantenimientoAmbientalResponse() {}

    public MantenimientoAmbientalResponse(Long id, String nombreArea, String descripcion, LocalDateTime fechaRegistro, String responsable) {
        this.id = id;
        this.nombreArea = nombreArea;
        this.descripcion = descripcion;
        this.fechaRegistro = fechaRegistro;
        this.responsable = responsable;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String nombreArea) { this.nombreArea = nombreArea; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
}
