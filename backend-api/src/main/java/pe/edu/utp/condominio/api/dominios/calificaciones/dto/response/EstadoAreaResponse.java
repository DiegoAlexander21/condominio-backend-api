package pe.edu.utp.condominio.api.dominios.calificaciones.dto.response;

import java.time.LocalDateTime;

public class EstadoAreaResponse {

    private Long id;
    private String nombreArea;
    private double calificacionPromedio;
    private int totalIncidencias;
    private int totalChecklistsNoAprobados;
    private LocalDateTime fechaCalculo;

    public EstadoAreaResponse() {}

    public EstadoAreaResponse(Long id, String nombreArea, double calificacionPromedio, int totalIncidencias, int totalChecklistsNoAprobados, LocalDateTime fechaCalculo) {
        this.id = id;
        this.nombreArea = nombreArea;
        this.calificacionPromedio = calificacionPromedio;
        this.totalIncidencias = totalIncidencias;
        this.totalChecklistsNoAprobados = totalChecklistsNoAprobados;
        this.fechaCalculo = fechaCalculo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String nombreArea) { this.nombreArea = nombreArea; }
    public double getCalificacionPromedio() { return calificacionPromedio; }
    public void setCalificacionPromedio(double calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }
    public int getTotalIncidencias() { return totalIncidencias; }
    public void setTotalIncidencias(int totalIncidencias) { this.totalIncidencias = totalIncidencias; }
    public int getTotalChecklistsNoAprobados() { return totalChecklistsNoAprobados; }
    public void setTotalChecklistsNoAprobados(int totalChecklistsNoAprobados) { this.totalChecklistsNoAprobados = totalChecklistsNoAprobados; }
    public LocalDateTime getFechaCalculo() { return fechaCalculo; }
    public void setFechaCalculo(LocalDateTime fechaCalculo) { this.fechaCalculo = fechaCalculo; }
}
