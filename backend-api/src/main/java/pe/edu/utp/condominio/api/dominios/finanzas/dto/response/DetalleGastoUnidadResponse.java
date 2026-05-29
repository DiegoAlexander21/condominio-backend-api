package pe.edu.utp.condominio.api.dominios.finanzas.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DetalleGastoUnidadResponse {

    private Long id;
    private Long gastoId;
    private Long unidadId;
    private String descripcionGasto;
    private String tipoGasto;
    private double montoAsignado;
    private LocalDateTime fechaRegistro;
    private LocalDate fechaLimite;

    public DetalleGastoUnidadResponse() {
    }

    public DetalleGastoUnidadResponse(Long id, Long gastoId, Long unidadId, String descripcionGasto, String tipoGasto,
            double montoAsignado, LocalDateTime fechaRegistro) {
        this.id = id;
        this.gastoId = gastoId;
        this.unidadId = unidadId;
        this.descripcionGasto = descripcionGasto;
        this.tipoGasto = tipoGasto;
        this.montoAsignado = montoAsignado;
        this.fechaRegistro = fechaRegistro;
    }

    public DetalleGastoUnidadResponse(Long id, Long gastoId, Long unidadId, String descripcionGasto, String tipoGasto,
            double montoAsignado, LocalDateTime fechaRegistro, LocalDate fechaLimite) {
        this.id = id;
        this.gastoId = gastoId;
        this.unidadId = unidadId;
        this.descripcionGasto = descripcionGasto;
        this.tipoGasto = tipoGasto;
        this.montoAsignado = montoAsignado;
        this.fechaRegistro = fechaRegistro;
        this.fechaLimite = fechaLimite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGastoId() {
        return gastoId;
    }

    public void setGastoId(Long gastoId) {
        this.gastoId = gastoId;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public String getDescripcionGasto() {
        return descripcionGasto;
    }

    public void setDescripcionGasto(String descripcionGasto) {
        this.descripcionGasto = descripcionGasto;
    }

    public String getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(String tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public double getMontoAsignado() {
        return montoAsignado;
    }

    public void setMontoAsignado(double montoAsignado) {
        this.montoAsignado = montoAsignado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
}
