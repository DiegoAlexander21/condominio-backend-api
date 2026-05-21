package pe.edu.utp.condominio.api.dominios.saludambiental.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ChecklistResponse {

    private Long id;
    private Long areaComunId;
    private String nombreArea;
    private String nombre;
    private boolean activo;
    private LocalDateTime fechaRegistro;
    private List<ItemChecklistResponse> items;

    public ChecklistResponse() {}

    public ChecklistResponse(Long id, Long areaComunId, String nombreArea, String nombre, boolean activo, LocalDateTime fechaRegistro, List<ItemChecklistResponse> items) {
        this.id = id;
        this.areaComunId = areaComunId;
        this.nombreArea = nombreArea;
        this.nombre = nombre;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
        this.items = items;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAreaComunId() { return areaComunId; }
    public void setAreaComunId(Long areaComunId) { this.areaComunId = areaComunId; }
    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String nombreArea) { this.nombreArea = nombreArea; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public List<ItemChecklistResponse> getItems() { return items; }
    public void setItems(List<ItemChecklistResponse> items) { this.items = items; }
}
