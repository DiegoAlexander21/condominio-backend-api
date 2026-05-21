package pe.edu.utp.condominio.api.dominios.calificaciones.models;

import java.time.LocalDateTime;

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
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;

@Entity
@Table(name = "estados_areas")
public class EstadoArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_comun_id", nullable = false)
    private AreaComun areaComun;

    @Column(nullable = false)
    private double calificacionPromedio;

    @Column(nullable = false)
    private int totalIncidencias;

    @Column(nullable = false)
    private int totalChecklistsNoAprobados;

    @Column(nullable = false)
    private LocalDateTime fechaCalculo;

    public EstadoArea() {
    }

    @PrePersist
    public void prepararRegistro() {
        fechaCalculo = LocalDateTime.now();
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

    public double getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }

    public int getTotalIncidencias() {
        return totalIncidencias;
    }

    public void setTotalIncidencias(int totalIncidencias) {
        this.totalIncidencias = totalIncidencias;
    }

    public int getTotalChecklistsNoAprobados() {
        return totalChecklistsNoAprobados;
    }

    public void setTotalChecklistsNoAprobados(int totalChecklistsNoAprobados) {
        this.totalChecklistsNoAprobados = totalChecklistsNoAprobados;
    }

    public LocalDateTime getFechaCalculo() {
        return fechaCalculo;
    }

    public void setFechaCalculo(LocalDateTime fechaCalculo) {
        this.fechaCalculo = fechaCalculo;
    }
}
