package pe.edu.utp.condominio.api.dominios.finanzas.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagoResponse {

    private Long id;
    private Long unidadId;
    private String unidadDetalles;
    private Long estadoCuentaId;
    private Double monto;
    private LocalDateTime fechaPago;
    private String observacion;
    private List<EvidenciaPagoResponse> evidencias = new ArrayList<>();

    public PagoResponse() {
    }

    public PagoResponse(Long id, Long unidadId, String unidadDetalles, Long estadoCuentaId, Double monto,
            LocalDateTime fechaPago, String observacion, List<EvidenciaPagoResponse> evidencias) {
        this.id = id;
        this.unidadId = unidadId;
        this.unidadDetalles = unidadDetalles;
        this.estadoCuentaId = estadoCuentaId;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.observacion = observacion;
        this.evidencias = evidencias;
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

    public String getUnidadDetalles() {
        return unidadDetalles;
    }

    public void setUnidadDetalles(String unidadDetalles) {
        this.unidadDetalles = unidadDetalles;
    }

    public Long getEstadoCuentaId() {
        return estadoCuentaId;
    }

    public void setEstadoCuentaId(Long estadoCuentaId) {
        this.estadoCuentaId = estadoCuentaId;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
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

    public List<EvidenciaPagoResponse> getEvidencias() {
        return evidencias;
    }

    public void setEvidencias(List<EvidenciaPagoResponse> evidencias) {
        this.evidencias = evidencias;
    }
}
