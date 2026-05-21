package pe.edu.utp.condominio.api.dominios.mantenimiento.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "usos_insumos")
public class UsoInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarea_id", nullable = false)
    private TareaMantenimiento tarea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insumo_id", nullable = false)
    private InsumoMantenimiento insumo;

    @Column(nullable = false)
    private double cantidadUsada;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    public UsoInsumo() {
    }

    @PrePersist
    public void prepararRegistro() {
        fechaRegistro = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TareaMantenimiento getTarea() {
        return tarea;
    }

    public void setTarea(TareaMantenimiento tarea) {
        this.tarea = tarea;
    }

    public InsumoMantenimiento getInsumo() {
        return insumo;
    }

    public void setInsumo(InsumoMantenimiento insumo) {
        this.insumo = insumo;
    }

    public double getCantidadUsada() {
        return cantidadUsada;
    }

    public void setCantidadUsada(double cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
