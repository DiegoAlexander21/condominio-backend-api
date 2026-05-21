package pe.edu.utp.condominio.api.dominios.mantenimiento.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class TareaMantenimientoResponse {

    private Long id;
    private String nombreArea;
    private String descripcion;
    private LocalDateTime fechaProgramada;
    private List<UsoInsumoResponse> usosInsumos;
    private double costoTotal;

    public TareaMantenimientoResponse() {}

    public TareaMantenimientoResponse(Long id, String nombreArea, String descripcion, LocalDateTime fechaProgramada, List<UsoInsumoResponse> usosInsumos, double costoTotal) {
        this.id = id;
        this.nombreArea = nombreArea;
        this.descripcion = descripcion;
        this.fechaProgramada = fechaProgramada;
        this.usosInsumos = usosInsumos;
        this.costoTotal = costoTotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String nombreArea) { this.nombreArea = nombreArea; }
    public String getShortDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaProgramada() { return fechaProgramada; }
    public void setFechaProgramada(LocalDateTime fechaProgramada) { this.fechaProgramada = fechaProgramada; }
    public List<UsoInsumoResponse> getUsosInsumos() { return usosInsumos; }
    public void setUsosInsumos(List<UsoInsumoResponse> usosInsumos) { this.usosInsumos = usosInsumos; }
    public double getCostoTotal() { return costoTotal; }
    public void setCostoTotal(double costoTotal) { this.costoTotal = costoTotal; }

    public String getDescripcion() {
        return descripcion;
    }
}
