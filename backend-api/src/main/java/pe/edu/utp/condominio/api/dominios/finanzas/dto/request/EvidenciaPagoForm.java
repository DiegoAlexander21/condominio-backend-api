package pe.edu.utp.condominio.api.dominios.finanzas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EvidenciaPagoForm {

    @NotNull(message = "El ID del pago es obligatorio.")
    private Long pagoId;

    @NotBlank(message = "La URL del archivo es obligatoria.")
    private String urlArchivo;

    public EvidenciaPagoForm() {
    }

    public Long getPagoId() {
        return pagoId;
    }

    public void setPagoId(Long pagoId) {
        this.pagoId = pagoId;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }
}
