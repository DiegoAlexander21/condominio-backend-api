package pe.edu.utp.condominio.api.dominios.visitas.dto.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

public class RegistroIngresoVisitaForm {

    @NotNull(message = "La visita es obligatoria.")
    private Long visitaId;

    private LocalDateTime fechaIngreso;

    public RegistroIngresoVisitaForm() {
    }

    public Long getVisitaId() {
        return visitaId;
    }

    public void setVisitaId(Long visitaId) {
        this.visitaId = visitaId;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}
