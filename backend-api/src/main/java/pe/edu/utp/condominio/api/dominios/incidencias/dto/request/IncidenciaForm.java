package pe.edu.utp.condominio.api.dominios.incidencias.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.CausaIncidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.GravedadIncidencia;

public class IncidenciaForm {

    private Long areaComunId;
    private Long unidadId;

    @NotBlank(message = "La descripcion es obligatoria.")
    @Size(min = 10, max = 1000, message = "La descripcion debe tener entre 10 y 1000 caracteres.")
    private String descripcion;

    @NotNull(message = "La gravedad es obligatoria.")
    private GravedadIncidencia gravedad;

    @NotNull(message = "La causa es obligatoria.")
    private CausaIncidencia causa;

    public IncidenciaForm() {
    }

    public Long getAreaComunId() {
        return areaComunId;
    }

    public void setAreaComunId(Long areaComunId) {
        this.areaComunId = areaComunId;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public GravedadIncidencia getGravedad() {
        return gravedad;
    }

    public void setGravedad(GravedadIncidencia gravedad) {
        this.gravedad = gravedad;
    }

    public CausaIncidencia getCausa() {
        return causa;
    }

    public void setCausa(CausaIncidencia causa) {
        this.causa = causa;
    }
}
