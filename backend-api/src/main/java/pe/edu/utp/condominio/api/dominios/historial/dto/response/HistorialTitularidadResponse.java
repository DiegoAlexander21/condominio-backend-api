package pe.edu.utp.condominio.api.dominios.historial.dto.response;

import java.time.LocalDateTime;

public class HistorialTitularidadResponse {

    private Long id;
    private Long unidadId;
    private String numeroUnidad;
    private String propietarioAnterior;
    private String nuevoPropietario;
    private LocalDateTime fechaCambio;

    public HistorialTitularidadResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public String getNumeroUnidad() {
        return numeroUnidad;
    }

    public void setNumeroUnidad(String numeroUnidad) {
        this.numeroUnidad = numeroUnidad;
    }

    public String getPropietarioAnterior() {
        return propietarioAnterior;
    }

    public void setPropietarioAnterior(String propietarioAnterior) {
        this.propietarioAnterior = propietarioAnterior;
    }

    public String getNuevoPropietario() {
        return nuevoPropietario;
    }

    public void setNuevoPropietario(String nuevoPropietario) {
        this.nuevoPropietario = nuevoPropietario;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}
