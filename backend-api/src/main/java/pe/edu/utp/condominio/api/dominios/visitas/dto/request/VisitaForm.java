package pe.edu.utp.condominio.api.dominios.visitas.dto.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VisitaForm {

    @NotNull(message = "La unidad es obligatoria.")
    private Long unidadId;

    @NotBlank(message = "El nombre del visitante es obligatorio.")
    @Size(min = 2, max = 150, message = "El nombre del visitante debe tener entre 2 y 150 caracteres.")
    private String nombreVisitante;

    @NotBlank(message = "El documento del visitante es obligatorio.")
    @Size(min = 4, max = 30, message = "El documento debe tener entre 4 y 30 caracteres.")
    private String documentoVisitante;

    @NotNull(message = "La fecha programada es obligatoria.")
    private LocalDateTime fechaVisitaProgramada;

    public VisitaForm() {
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public String getNombreVisitante() {
        return nombreVisitante;
    }

    public void setNombreVisitante(String nombreVisitante) {
        this.nombreVisitante = nombreVisitante;
    }

    public String getDocumentoVisitante() {
        return documentoVisitante;
    }

    public void setDocumentoVisitante(String documentoVisitante) {
        this.documentoVisitante = documentoVisitante;
    }

    public LocalDateTime getFechaVisitaProgramada() {
        return fechaVisitaProgramada;
    }

    public void setFechaVisitaProgramada(LocalDateTime fechaVisitaProgramada) {
        this.fechaVisitaProgramada = fechaVisitaProgramada;
    }
}
