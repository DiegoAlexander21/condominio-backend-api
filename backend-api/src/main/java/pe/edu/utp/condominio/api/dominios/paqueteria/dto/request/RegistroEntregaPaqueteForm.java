package pe.edu.utp.condominio.api.dominios.paqueteria.dto.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegistroEntregaPaqueteForm {

    @NotNull(message = "El paquete es obligatorio.")
    private Long paqueteId;

    private LocalDateTime fechaEntrega;

    @Size(max = 300, message = "La observacion no puede superar 300 caracteres.")
    private String observacion;

    public RegistroEntregaPaqueteForm() {
    }

    public Long getPaqueteId() {
        return paqueteId;
    }

    public void setPaqueteId(Long paqueteId) {
        this.paqueteId = paqueteId;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
