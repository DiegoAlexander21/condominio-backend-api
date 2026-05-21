package pe.edu.utp.condominio.api.dominios.comunicacion.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.EstadoAsamblea;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;

@Entity
@Table(name = "asambleas")
public class Asamblea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condominio_id", nullable = false)
    private Condominio condominio;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 1500)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoAsamblea estado;

    @OneToMany(mappedBy = "asamblea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpcionVotacion> opciones = new ArrayList<>();

    @OneToMany(mappedBy = "asamblea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VotoAsamblea> votos = new ArrayList<>();

    public Asamblea() {
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

    public Condominio getCondominio() {
        return condominio;
    }

    public void setCondominio(Condominio condominio) {
        this.condominio = condominio;
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

    public EstadoAsamblea getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsamblea estado) {
        this.estado = estado;
    }

    public List<OpcionVotacion> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionVotacion> opciones) {
        this.opciones = opciones;
    }

    public List<VotoAsamblea> getVotos() {
        return votos;
    }

    public void setVotos(List<VotoAsamblea> votos) {
        this.votos = votos;
    }
}
