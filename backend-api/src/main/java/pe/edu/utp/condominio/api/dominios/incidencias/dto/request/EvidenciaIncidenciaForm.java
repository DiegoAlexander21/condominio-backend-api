package pe.edu.utp.condominio.api.dominios.incidencias.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EvidenciaIncidenciaForm {

    @NotNull(message = "La incidencia es obligatoria.")
    private Long incidenciaId;

    @NotBlank(message = "La URL del archivo es obligatoria.")
    @Size(max = 500, message = "La URL no puede superar 500 caracteres.")
    private String urlArchivo;

    public EvidenciaIncidenciaForm() {
    }

    public Long getIncidenciaId() {
        return incidenciaId;
    }

    public void setIncidenciaId(Long incidenciaId) {
        this.incidenciaId = incidenciaId;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }
}
