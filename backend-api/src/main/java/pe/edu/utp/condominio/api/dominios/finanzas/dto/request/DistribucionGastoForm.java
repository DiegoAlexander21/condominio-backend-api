package pe.edu.utp.condominio.api.dominios.finanzas.dto.request;

import jakarta.validation.constraints.NotNull;

public class DistribucionGastoForm {

    @NotNull(message = "El gasto es obligatorio.")
    private Long gastoId;

    private Long unidadId;

    public DistribucionGastoForm() {
    }

    public Long getGastoId() {
        return gastoId;
    }

    public void setGastoId(Long gastoId) {
        this.gastoId = gastoId;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }
}
