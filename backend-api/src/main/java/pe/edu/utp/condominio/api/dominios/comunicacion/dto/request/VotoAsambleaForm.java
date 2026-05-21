package pe.edu.utp.condominio.api.dominios.comunicacion.dto.request;

import jakarta.validation.constraints.NotNull;

public class VotoAsambleaForm {

    @NotNull(message = "La asamblea es obligatoria.")
    private Long asambleaId;

    @NotNull(message = "La opcion es obligatoria.")
    private Long opcionId;

    @NotNull(message = "La unidad es obligatoria.")
    private Long unidadId;

    public VotoAsambleaForm() {
    }

    public Long getAsambleaId() {
        return asambleaId;
    }

    public void setAsambleaId(Long asambleaId) {
        this.asambleaId = asambleaId;
    }

    public Long getOpcionId() {
        return opcionId;
    }

    public void setOpcionId(Long opcionId) {
        this.opcionId = opcionId;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }
}
