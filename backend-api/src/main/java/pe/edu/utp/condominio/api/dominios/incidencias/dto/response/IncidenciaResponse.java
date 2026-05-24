package pe.edu.utp.condominio.api.dominios.incidencias.dto.response;

import java.time.LocalDateTime;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.CausaIncidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.EstadoIncidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.GravedadIncidencia;

public class IncidenciaResponse {

    private Long id;
    private Long areaComunId;
    private Long unidadId;
    private String descripcion;
    private GravedadIncidencia gravedad;
    private CausaIncidencia causa;
    private EstadoIncidencia estado;
    private String responsableAtencion;
    private LocalDateTime fechaReporte;
    private LocalDateTime fechaActualizacion;
    private String lugarAfectado;

    public IncidenciaResponse() {
    }

    public IncidenciaResponse(Long id, Long areaComunId, Long unidadId, String descripcion,
            GravedadIncidencia gravedad, CausaIncidencia causa, EstadoIncidencia estado,
            String responsableAtencion, LocalDateTime fechaReporte, LocalDateTime fechaActualizacion,
            String lugarAfectado) {
        this.id = id;
        this.areaComunId = areaComunId;
        this.unidadId = unidadId;
        this.descripcion = descripcion;
        this.gravedad = gravedad;
        this.causa = causa;
        this.estado = estado;
        this.responsableAtencion = responsableAtencion;
        this.fechaReporte = fechaReporte;
        this.fechaActualizacion = fechaActualizacion;
        this.lugarAfectado = lugarAfectado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getLugarAfectado() {
        return lugarAfectado;
    }

    public void setLugarAfectado(String lugarAfectado) {
        this.lugarAfectado = lugarAfectado;
    }

    public String getGravedadFormateada() {
        return gravedad != null ? formatearEnum(gravedad.name()) : "";
    }

    public String getCausaFormateada() {
        return causa != null ? formatearEnum(causa.name()) : "";
    }

    public String getEstadoFormateado() {
        return estado != null ? formatearEnum(estado.name()) : "";
    }

    private String formatearEnum(String valor) {
        if (valor == null || valor.isEmpty()) return "";
        String[] palabras = valor.toLowerCase().split("_");
        if (palabras.length == 0) return "";
        palabras[0] = palabras[0].substring(0, 1).toUpperCase() + palabras[0].substring(1);
        return String.join(" ", palabras);
    }
}
