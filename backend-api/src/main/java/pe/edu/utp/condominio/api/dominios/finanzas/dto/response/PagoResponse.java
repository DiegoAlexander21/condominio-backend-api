package pe.edu.utp.condominio.api.dominios.finanzas.dto.response;

import java.time.LocalDateTime;

public class PagoResponse {

    private Long id;
    private Long unidadId;
    private Long estadoCuentaId;
    private double monto;
    private LocalDateTime fechaPago;
    private String observacion;

    public PagoResponse() {
    }

    public PagoResponse(Long id, Long unidadId, Long estadoCuentaId, double monto,
            LocalDateTime fechaPago, String observacion) {
        this.id = id;
        this.unidadId = unidadId;
        this.estadoCuentaId = estadoCuentaId;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.observacion = observacion;
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

    public Long getEstadoCuentaId() {
        return estadoCuentaId;
    }

    public void setEstadoCuentaId(Long estadoCuentaId) {
        this.estadoCuentaId = estadoCuentaId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
