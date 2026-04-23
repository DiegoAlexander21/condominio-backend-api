package pe.edu.utp.condominio.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_titularidad")
public class HistorialTitularidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "El ID del departamento debe ser un número positivo")
    private Long departamentoId;

    @NotBlank(message = "El nombre del dueño anterior es obligatorio")
    @Size(min = 3, max = 70)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")
    @Column(length = 70)
    private String propietarioAnterior;

    @NotBlank(message = "El nombre del dueño actual es obligatorio")
    @Size(min = 3, max = 70)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")
    @Column(length = 70)
    private String nuevoPropietario;

    private LocalDateTime fechaCambio;

    public HistorialTitularidad() {
    }

    public HistorialTitularidad(Long id, Long departamentoId, String propietarioAnterior, 
                                String nuevoPropietario, LocalDateTime fechaCambio) {
        this.id = id;
        this.departamentoId = departamentoId;
        this.propietarioAnterior = propietarioAnterior;
        this.nuevoPropietario = nuevoPropietario;
        this.fechaCambio = fechaCambio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getPropietarioAnterior() {
        return propietarioAnterior;
    }

    public void setPropietarioAnterior(String propietarioAnterior) {
        this.propietarioAnterior = propietarioAnterior;
    }

    public String getNuevoPropietario() {
        return nuevoPropietario;
    }

    public void setNuevoPropietario(String nuevoPropietario) {
        this.nuevoPropietario = nuevoPropietario;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}