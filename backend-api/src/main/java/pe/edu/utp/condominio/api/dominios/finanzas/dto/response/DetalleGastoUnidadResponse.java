package pe.edu.utp.condominio.api.dominios.finanzas.dto.response;

import java.time.LocalDateTime;

public class DetalleGastoUnidadResponse {

    private Long id;
    private Long gastoId;
    private Long unidadId;
    private double montoAsignado;
    private LocalDateTime fechaRegistro;

    public DetalleGastoUnidadResponse() {
    }

    public DetalleGastoUnidadResponse(Long id, Long gastoId, Long unidadId,
            double montoAsignado, LocalDateTime fechaRegistro) {
        this.id = id;
        this.gastoId = gastoId;
        this.unidadId = unidadId;
        this.montoAsignado = montoAsignado;
        this.fechaRegistro = fechaRegistro;
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
}
