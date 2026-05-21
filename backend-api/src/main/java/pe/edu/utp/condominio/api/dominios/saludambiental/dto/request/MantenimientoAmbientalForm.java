package pe.edu.utp.condominio.api.dominios.saludambiental.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MantenimientoAmbientalForm {

    @NotNull(message = "El ID del área común es obligatorio.")
    private Long areaComunId;

    @NotBlank(message = "La descripción del mantenimiento es obligatoria.")
    @Size(max = 300, message = "La descripción no puede exceder los 300 caracteres.")
    private String descripcion;

    @NotBlank(message = "El nombre del responsable es obligatorio.")
    @Size(max = 150, message = "El nombre del responsable no puede exceder los 150 caracteres.")
    private String responsable;

    public MantenimientoAmbientalForm() {}

    public Long getAreaComunId() { return areaComunId; }
    public void setAreaComunId(Long areaComunId) { this.areaComunId = areaComunId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
}
