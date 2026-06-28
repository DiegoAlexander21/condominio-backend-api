package pe.edu.utp.condominio.api.dominios.finanzas.dto.request;

import jakarta.validation.constraints.NotNull;

public class AprobacionPagoForm {

    @NotNull(message = "El campo aprobar es obligatorio.")
    private Boolean aprobar;

    private String observacionAdmin;

    public AprobacionPagoForm() {
    }

    public Boolean getAprobar() {
        return aprobar;
    }

    public void setAprobar(Boolean aprobar) {
        this.aprobar = aprobar;
    }

    public String getObservacionAdmin() {
        return observacionAdmin;
    }

    public void setObservacionAdmin(String observacionAdmin) {
        this.observacionAdmin = observacionAdmin;
    }
}
