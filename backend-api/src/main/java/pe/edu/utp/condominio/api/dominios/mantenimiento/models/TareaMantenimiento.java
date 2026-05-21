package pe.edu.utp.condominio.api.dominios.mantenimiento.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;

@Entity
@Table(name = "tareas_mantenimiento")
public class TareaMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_comun_id", nullable = false)
    private AreaComun areaComun;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaProgramada;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsoInsumo> usosInsumos = new ArrayList<>();

    public TareaMantenimiento() {
    }

    @PrePersist
    public void prepararRegistro() {
        LocalDateTime ahora = LocalDateTime.now();
        fechaRegistro = ahora;
        fechaActualizacion = ahora;
    }

    @PreUpdate
    public void prepararActualizacion() {
        fechaActualizacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AreaComun getAreaComun() {
        return areaComun;
    }

    public void setAreaComun(AreaComun areaComun) {
        this.areaComun = areaComun;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<UsoInsumo> getUsosInsumos() {
        return usosInsumos;
    }

    public void setUsosInsumos(List<UsoInsumo> usosInsumos) {
        this.usosInsumos = usosInsumos;
    }
}
