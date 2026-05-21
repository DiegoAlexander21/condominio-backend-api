package pe.edu.utp.condominio.api.dominios.calificaciones.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CalificacionForm {

    @NotNull(message = "El ID del área común es obligatorio.")
    private Long areaComunId;

    @NotNull(message = "El ID de la unidad es obligatorio.")
    private Long unidadId;

    @Min(value = 1, message = "El puntaje mínimo es 1.")
    @Max(value = 5, message = "El puntaje máximo es 5.")
    private int puntaje;

    @Size(max = 500, message = "El comentario no puede exceder los 500 caracteres.")
    private String comentario;

    public CalificacionForm() {}

    public Long getAreaComunId() { return areaComunId; }
    public void setAreaComunId(Long areaComunId) { this.areaComunId = areaComunId; }
    public Long getUnidadId() { return unidadId; }
    public void setUnidadId(Long unidadId) { this.unidadId = unidadId; }
    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
