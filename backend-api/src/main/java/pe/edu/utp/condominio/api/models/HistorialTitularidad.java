package pe.edu.utp.condominio.api.models;

import java.time.LocalDateTime;

public class HistorialTitularidad {

    private Long id;
    private Long departamentoId;
    private String propietarioAnterior;
    private String nuevoPropietario;
    private LocalDateTime fechaCambio;

    public HistorialTitularidad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
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