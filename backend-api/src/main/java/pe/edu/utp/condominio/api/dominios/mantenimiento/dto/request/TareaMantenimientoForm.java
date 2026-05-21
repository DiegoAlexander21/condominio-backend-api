package pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TareaMantenimientoForm {

    @NotNull(message = "El ID del área común es obligatorio.")
    private Long areaComunId;

    @NotBlank(message = "La descripción de la tarea es obligatoria.")
    @Size(max = 200, message = "La descripción no puede exceder los 200 caracteres.")
    private String descripcion;

    @NotNull(message = "La fecha programada es obligatoria.")
    private LocalDateTime fechaProgramada;

    private List<UsoInsumoForm> usosInsumos;

    public TareaMantenimientoForm() {}

    public Long getAreaComunId() { return areaComunId; }
    public void setAreaComunId(Long areaComunId) { this.areaComunId = areaComunId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaProgramada() { return fechaProgramada; }
    public void setFechaProgramada(LocalDateTime fechaProgramada) { this.fechaProgramada = fechaProgramada; }
    public List<UsoInsumoForm> getUsosInsumos() { return usosInsumos; }
    public void setUsosInsumos(List<UsoInsumoForm> usosInsumos) { this.usosInsumos = usosInsumos; }
}
