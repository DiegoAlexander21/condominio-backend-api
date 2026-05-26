package pe.edu.utp.condominio.api.dominios.finanzas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EstadoCuentaForm {

    @NotNull(message = "La unidad es obligatoria.")
    private Long unidadId;

    @NotBlank(message = "El periodo es obligatorio.")
    private String periodo;

    public EstadoCuentaForm() {
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
