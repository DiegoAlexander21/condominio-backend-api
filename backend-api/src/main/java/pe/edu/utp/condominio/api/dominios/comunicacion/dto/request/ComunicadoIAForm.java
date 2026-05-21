package pe.edu.utp.condominio.api.dominios.comunicacion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ComunicadoIAForm {

    @NotNull(message = "El condominio es obligatorio.")
    private Long condominioId;

    @NotBlank(message = "El titulo es obligatorio.")
    @Size(min = 3, max = 200, message = "El titulo debe tener entre 3 y 200 caracteres.")
    private String titulo;

    @NotBlank(message = "El borrador es obligatorio.")
    @Size(min = 10, max = 2000, message = "El borrador debe tener entre 10 y 2000 caracteres.")
    private String borrador;

    public ComunicadoIAForm() {
    }

    public Long getCondominioId() {
        return condominioId;
    }

    public void setCondominioId(Long condominioId) {
        this.condominioId = condominioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getBorrador() {
        return borrador;
    }

    public void setBorrador(String borrador) {
        this.borrador = borrador;
    }
}
