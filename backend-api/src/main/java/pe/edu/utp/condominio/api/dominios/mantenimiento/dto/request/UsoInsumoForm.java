package pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UsoInsumoForm {

    @NotNull(message = "El ID del insumo es obligatorio.")
    private Long insumoId;

    @NotNull(message = "La cantidad usada es obligatoria.")
    @Min(value = 0, message = "La cantidad no puede ser negativa.")
    private Double cantidadUsada;

    public UsoInsumoForm() {}

    public Long getInsumoId() { return insumoId; }
    public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
    public Double getCantidadUsada() { return cantidadUsada; }
    public void setCantidadUsada(Double cantidadUsada) { this.cantidadUsada = cantidadUsada; }
}
