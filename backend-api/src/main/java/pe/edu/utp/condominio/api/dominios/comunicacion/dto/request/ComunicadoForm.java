package pe.edu.utp.condominio.api.dominios.comunicacion.dto.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.ComunicadoTorreDto;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.AlcanceComunicado;

public class ComunicadoForm {

    @NotNull(message = "El alcance es obligatorio.")
    private AlcanceComunicado alcance;

    @NotNull(message = "Debe seleccionar al menos un condominio.")
    private List<Long> condominioIds = new ArrayList<>();

    private List<ComunicadoTorreDto> torres = new ArrayList<>();

    private List<Long> unidadIds = new ArrayList<>();

    @NotBlank(message = "El titulo es obligatorio.")
    @Size(min = 3, max = 200, message = "El titulo debe tener entre 3 y 200 caracteres.")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio.")
    @Size(min = 10, max = 3000, message = "El contenido debe tener entre 10 y 3000 caracteres.")
    private String contenido;

    public ComunicadoForm() {
    }

    public AlcanceComunicado getAlcance() {
        return alcance;
    }

    public void setAlcance(AlcanceComunicado alcance) {
        this.alcance = alcance;
    }

    public List<Long> getCondominioIds() {
        return condominioIds;
    }

    public void setCondominioIds(List<Long> condominioIds) {
        this.condominioIds = condominioIds;
    }

    public List<ComunicadoTorreDto> getTorres() {
        return torres;
    }

    public void setTorres(List<ComunicadoTorreDto> torres) {
        this.torres = torres;
    }

    public List<Long> getUnidadIds() {
        return unidadIds;
    }

    public void setUnidadIds(List<Long> unidadIds) {
        this.unidadIds = unidadIds;
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
