package pe.edu.utp.condominio.api.dominios.visitas.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import pe.edu.utp.condominio.api.dominios.visitas.enums.EstadoVisita;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;

@Entity
@Table(name = "visitas")
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_id", nullable = false)
    private Unidad unidad;

    @Column(nullable = false, length = 150)
    private String nombreVisitante;

    @Column(nullable = false, length = 30)
    private String documentoVisitante;

    @Column(nullable = false)
    private LocalDateTime fechaVisitaProgramada;

    @Column
    private LocalDateTime fechaIngreso;

    @Column
    private LocalDateTime fechaSalida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private EstadoVisita estado;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    public Visita() {
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

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public String getNombreVisitante() {
        return nombreVisitante;
    }

    public void setNombreVisitante(String nombreVisitante) {
        this.nombreVisitante = nombreVisitante;
    }

    public String getDocumentoVisitante() {
        return documentoVisitante;
    }

    public void setDocumentoVisitante(String documentoVisitante) {
        this.documentoVisitante = documentoVisitante;
    }

    public LocalDateTime getFechaVisitaProgramada() {
        return fechaVisitaProgramada;
    }

    public void setFechaVisitaProgramada(LocalDateTime fechaVisitaProgramada) {
        this.fechaVisitaProgramada = fechaVisitaProgramada;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public EstadoVisita getEstado() {
        return estado;
    }

    public void setEstado(EstadoVisita estado) {
        this.estado = estado;
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
}
