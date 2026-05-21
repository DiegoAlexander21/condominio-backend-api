package pe.edu.utp.condominio.api.dominios.condominio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.Asamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.Comunicado;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;

@Entity
@Table(name = "condominios")
public class Condominio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false)
    private int torres;

    @Column(nullable = false)
    private int pisosPorTorre;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Unidad> unidades = new ArrayList<>();

    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AreaComun> areasComunes = new ArrayList<>();

    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comunicado> comunicados = new ArrayList<>();

    @OneToMany(mappedBy = "condominio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asamblea> asambleas = new ArrayList<>();

    public Condominio() {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTorres() {
        return torres;
    }

    public void setTorres(int torres) {
        this.torres = torres;
    }

    public int getPisosPorTorre() {
        return pisosPorTorre;
    }

    public void setPisosPorTorre(int pisosPorTorre) {
        this.pisosPorTorre = pisosPorTorre;
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

    public List<Unidad> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<Unidad> unidades) {
        this.unidades = unidades;
    }

    public List<AreaComun> getAreasComunes() {
        return areasComunes;
    }

    public void setAreasComunes(List<AreaComun> areasComunes) {
        this.areasComunes = areasComunes;
    }

    public List<Comunicado> getComunicados() {
        return comunicados;
    }

    public void setComunicados(List<Comunicado> comunicados) {
        this.comunicados = comunicados;
    }

    public List<Asamblea> getAsambleas() {
        return asambleas;
    }

    public void setAsambleas(List<Asamblea> asambleas) {
        this.asambleas = asambleas;
    }
}
