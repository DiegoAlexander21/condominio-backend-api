package pe.edu.utp.condominio.api.dominios.comunicacion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ComunicadoForm {

    @NotNull(message = "El condominio es obligatorio.")
    private Long condominioId;

    @NotBlank(message = "El titulo es obligatorio.")
    @Size(min = 3, max = 200, message = "El titulo debe tener entre 3 y 200 caracteres.")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio.")
    @Size(min = 10, max = 3000, message = "El contenido debe tener entre 10 y 3000 caracteres.")
    private String contenido;

    public ComunicadoForm() {
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

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
