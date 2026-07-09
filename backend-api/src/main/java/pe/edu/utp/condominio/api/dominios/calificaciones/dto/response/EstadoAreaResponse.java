package pe.edu.utp.condominio.api.dominios.calificaciones.dto.response;

import java.time.LocalDateTime;

public class EstadoAreaResponse {

    private Long id;
    private Long areaId;
    private Long condominioId;
    private String condominioNombre;
    private String nombreArea;
    private double calificacionPromedio;
    private int totalIncidencias;
    private int totalChecklistsNoAprobados;
    private LocalDateTime fechaCalculo;

    public EstadoAreaResponse() {}

    public EstadoAreaResponse(Long id, Long areaId, Long condominioId, String condominioNombre, String nombreArea, double calificacionPromedio, int totalIncidencias, int totalChecklistsNoAprobados, LocalDateTime fechaCalculo) {
        this.id = id;
        this.areaId = areaId;
        this.condominioId = condominioId;
        this.condominioNombre = condominioNombre;
        this.nombreArea = nombreArea;
        this.calificacionPromedio = calificacionPromedio;
        this.totalIncidencias = totalIncidencias;
        this.totalChecklistsNoAprobados = totalChecklistsNoAprobados;
        this.fechaCalculo = fechaCalculo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }
    public Long getCondominioId() { return condominioId; }
    public void setCondominioId(Long condominioId) { this.condominioId = condominioId; }
    public String getCondominioNombre() { return condominioNombre; }
    public void setCondominioNombre(String condominioNombre) { this.condominioNombre = condominioNombre; }
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
