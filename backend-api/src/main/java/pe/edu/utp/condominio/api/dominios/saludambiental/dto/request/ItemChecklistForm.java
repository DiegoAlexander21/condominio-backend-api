package pe.edu.utp.condominio.api.dominios.saludambiental.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ItemChecklistForm {

    @NotBlank(message = "La descripción del ítem es obligatoria.")
    @Size(max = 300, message = "La descripción no puede exceder los 300 caracteres.")
    private String descripcion;

    @NotNull(message = "El orden del ítem es obligatorio.")
    private Integer orden;

    public ItemChecklistForm() {}

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
}
