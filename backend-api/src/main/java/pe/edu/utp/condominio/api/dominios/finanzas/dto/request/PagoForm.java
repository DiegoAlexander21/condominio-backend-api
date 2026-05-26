package pe.edu.utp.condominio.api.dominios.finanzas.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PagoForm {

    @NotNull(message = "La unidad es obligatoria.")
    private Long unidadId;

    private Long estadoCuentaId;

    @Min(value = 1, message = "El monto debe ser mayor a cero.")
    private double monto;

    @Size(max = 300, message = "La observacion no puede superar 300 caracteres.")
    private String observacion;

    @Size(max = 500, message = "La URL del comprobante no puede superar 500 caracteres.")
    private String evidenciaUrl;

    public PagoForm() {
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEvidenciaUrl() {
        return evidenciaUrl;
    }

    public void setEvidenciaUrl(String evidenciaUrl) {
        this.evidenciaUrl = evidenciaUrl;
    }
}
