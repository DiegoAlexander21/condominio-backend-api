package pe.edu.utp.condominio.api.dominios.comunicacion.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.AlcanceComunicado;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;

@Entity
@Table(name = "comunicados")
public class Comunicado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) default 'GLOBAL'")
    private AlcanceComunicado alcance;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "comunicado_condominios",
        joinColumns = @JoinColumn(name = "comunicado_id"),
        inverseJoinColumns = @JoinColumn(name = "condominio_id")
    )
    private List<Condominio> condominiosDestino = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "comunicado_torres",
        joinColumns = @JoinColumn(name = "comunicado_id")
    )
    private List<ComunicadoTorre> torresDestino = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "comunicado_unidades",
        joinColumns = @JoinColumn(name = "comunicado_id"),
        inverseJoinColumns = @JoinColumn(name = "unidad_id")
    )
    private List<Unidad> unidadesDestino = new ArrayList<>();

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 3000)
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaPublicacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    public Comunicado() {
    }

    @PrePersist
    public void prepararRegistro() {
        LocalDateTime ahora = LocalDateTime.now();
        fechaPublicacion = ahora;
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

    public AlcanceComunicado getAlcance() {
        return alcance;
    }

    public void setAlcance(AlcanceComunicado alcance) {
        this.alcance = alcance;
    }

    public List<Condominio> getCondominiosDestino() {
        return condominiosDestino;
    }

    public void setCondominiosDestino(List<Condominio> condominiosDestino) {
        this.condominiosDestino = condominiosDestino;
    }

    public List<ComunicadoTorre> getTorresDestino() {
        return torresDestino;
    }

    public void setTorresDestino(List<ComunicadoTorre> torresDestino) {
        this.torresDestino = torresDestino;
    }

    public List<Unidad> getUnidadesDestino() {
        return unidadesDestino;
    }

    public void setUnidadesDestino(List<Unidad> unidadesDestino) {
        this.unidadesDestino = unidadesDestino;
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

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
