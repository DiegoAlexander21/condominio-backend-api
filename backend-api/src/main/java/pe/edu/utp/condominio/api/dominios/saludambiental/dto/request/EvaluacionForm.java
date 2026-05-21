package pe.edu.utp.condominio.api.dominios.saludambiental.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.edu.utp.condominio.api.dominios.saludambiental.enums.ResultadoChecklist;

public class EvaluacionForm {

    @NotNull(message = "El ID del checklist es obligatorio.")
    private Long checklistId;

    @NotNull(message = "El resultado de la evaluación es obligatorio.")
    private ResultadoChecklist resultado;

    @Size(max = 500, message = "La observación no puede exceder los 500 caracteres.")
    private String observacion;

    public EvaluacionForm() {}

    public Long getChecklistId() { return checklistId; }
    public void setChecklistId(Long checklistId) { this.checklistId = checklistId; }
    public ResultadoChecklist getResultado() { return resultado; }
    public void setResultado(ResultadoChecklist resultado) { this.resultado = resultado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
