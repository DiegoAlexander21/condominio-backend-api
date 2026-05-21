package pe.edu.utp.condominio.api.dominios.comunicacion.models;

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
import jakarta.persistence.UniqueConstraint;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;

@Entity
@Table(name = "votos_asamblea", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "asamblea_id", "unidad_id" })
})
public class VotoAsamblea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asamblea_id", nullable = false)
    private Asamblea asamblea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcion_id", nullable = false)
    private OpcionVotacion opcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_id", nullable = false)
    private Unidad unidad;

    @Column(nullable = false)
    private LocalDateTime fechaVoto;

    public VotoAsamblea() {
    }

    @PrePersist
    public void prepararRegistro() {
        fechaVoto = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Asamblea getAsamblea() {
        return asamblea;
    }

    public void setAsamblea(Asamblea asamblea) {
        this.asamblea = asamblea;
    }

    public OpcionVotacion getOpcion() {
        return opcion;
    }

    public void setOpcion(OpcionVotacion opcion) {
        this.opcion = opcion;
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    public LocalDateTime getFechaVoto() {
        return fechaVoto;
    }

    public void setFechaVoto(LocalDateTime fechaVoto) {
        this.fechaVoto = fechaVoto;
    }
}
