package pe.edu.utp.condominio.api.dominios.incidencias.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.EstadoIncidencia;

public class ActualizacionIncidenciaForm {

    @NotNull(message = "El identificador de la incidencia es obligatorio.")
    private Long incidenciaId;

    @NotNull(message = "El estado es obligatorio.")
    private EstadoIncidencia estado;

    @Size(max = 150, message = "El responsable no puede superar 150 caracteres.")
    private String responsableAtencion;

    public ActualizacionIncidenciaForm() {
    }

    public Long getIncidenciaId() {
        return incidenciaId;
    }

    public void setIncidenciaId(Long incidenciaId) {
        this.incidenciaId = incidenciaId;
    }

    public EstadoIncidencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidencia estado) {
        this.estado = estado;
    }

    public String getResponsableAtencion() {
        return responsableAtencion;
    }

    public void setResponsableAtencion(String responsableAtencion) {
        this.responsableAtencion = responsableAtencion;
    }
}
