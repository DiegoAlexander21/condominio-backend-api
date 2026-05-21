package pe.edu.utp.condominio.api.dominios.paqueteria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PaqueteForm {

    @NotNull(message = "La unidad es obligatoria.")
    private Long unidadId;

    @NotBlank(message = "El remitente es obligatorio.")
    @Size(min = 2, max = 150, message = "El remitente debe tener entre 2 y 150 caracteres.")
    private String remitente;

    @NotBlank(message = "El destinatario es obligatorio.")
    @Size(min = 2, max = 150, message = "El destinatario debe tener entre 2 y 150 caracteres.")
    private String destinatario;

    @Size(max = 300, message = "La observacion no puede superar 300 caracteres.")
    private String observacion;

    public PaqueteForm() {
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
