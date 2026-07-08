package pe.edu.utp.condominio.api.dominios.comunicacion.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.AlcanceComunicado;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.ComunicadoTorreDto;

public class AsambleaForm {

    @NotNull(message = "El/los condominios son obligatorios.")
    private List<Long> condominioIds;

    @NotNull(message = "El alcance es obligatorio.")
    private AlcanceComunicado alcance;

    private List<ComunicadoTorreDto> torres;

    private List<Long> unidadIds;

    @NotBlank(message = "El titulo es obligatorio.")
    @Size(min = 3, max = 200, message = "El titulo debe tener entre 3 y 200 caracteres.")
    private String titulo;

    @NotBlank(message = "La descripcion es obligatoria.")
    @Size(min = 10, max = 1500, message = "La descripcion debe tener entre 10 y 1500 caracteres.")
    private String descripcion;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria.")
    private LocalDateTime fechaFin;

    @NotNull(message = "Debe registrar opciones de votacion.")
    private List<@NotBlank @Size(min = 1, max = 200, message = "La opcion debe tener entre 1 y 200 caracteres.") String> opciones;

    public AsambleaForm() {
    }

    public List<Long> getCondominioIds() {
        return condominioIds;
    }

    public void setCondominioIds(List<Long> condominioIds) {
        this.condominioIds = condominioIds;
    }

    public AlcanceComunicado getAlcance() {
        return alcance;
    }

    public void setAlcance(AlcanceComunicado alcance) {
        this.alcance = alcance;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }
}
