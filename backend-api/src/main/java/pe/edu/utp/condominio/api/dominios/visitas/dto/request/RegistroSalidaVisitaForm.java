package pe.edu.utp.condominio.api.dominios.visitas.dto.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

public class RegistroSalidaVisitaForm {

    @NotNull(message = "La visita es obligatoria.")
    private Long visitaId;

    private LocalDateTime fechaSalida;

    public RegistroSalidaVisitaForm() {
    }

    public Long getVisitaId() {
        return visitaId;
    }

    public void setVisitaId(Long visitaId) {
        this.visitaId = visitaId;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
}
