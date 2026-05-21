package pe.edu.utp.condominio.api.dominios.saludambiental.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChecklistForm {

    @NotNull(message = "El ID del área común es obligatorio.")
    private Long areaComunId;

    @NotBlank(message = "El nombre del checklist es obligatorio.")
    @Size(max = 200, message = "El nombre no puede exceder los 200 caracteres.")
    private String nombre;

    @NotNull(message = "Debe incluir al menos un ítem en el checklist.")
    @Size(min = 1, message = "Debe incluir al menos un ítem.")
    private List<ItemChecklistForm> items;

    public ChecklistForm() {
    }

    public Long getAreaComunId() {
        return areaComunId;
    }

    public void setAreaComunId(Long areaComunId) {
        this.areaComunId = areaComunId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ItemChecklistForm> getItems() {
        return items;
    }

    public void setItems(List<ItemChecklistForm> items) {
        this.items = items;
    }
}
