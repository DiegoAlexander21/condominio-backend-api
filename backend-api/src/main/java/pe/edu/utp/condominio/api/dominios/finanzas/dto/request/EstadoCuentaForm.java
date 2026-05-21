package pe.edu.utp.condominio.api.dominios.finanzas.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

public class EstadoCuentaForm {

    @NotNull(message = "La unidad es obligatoria.")
    private Long unidadId;

    @NotNull(message = "El periodo es obligatorio.")
    private LocalDate periodo;

    public EstadoCuentaForm() {
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public LocalDate getPeriodo() {
        return periodo;
    }

    public void setPeriodo(LocalDate periodo) {
        this.periodo = periodo;
    }
}
