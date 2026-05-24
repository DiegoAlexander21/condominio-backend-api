package pe.edu.utp.condominio.api.dominios.areascomunes.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
import pe.edu.utp.condominio.api.dominios.calificaciones.models.CalificacionArea;
import pe.edu.utp.condominio.api.dominios.calificaciones.models.EstadoArea;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.incidencias.models.IncidenciaAreaComun;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.TareaMantenimiento;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.ChecklistSaludAmbiente;

import pe.edu.utp.condominio.api.dominios.saludambiental.models.RegistroMantenimientoAmbiental;

@Entity
@Table(name = "areas_comunes")
public class AreaComun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condominio_id", nullable = false)
    private Condominio condominio;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private int capacidad;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Column(length = 1000)
    private String normasUso;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "areaComun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaAreaComun> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "areaComun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncidenciaAreaComun> incidencias = new ArrayList<>();

    @OneToMany(mappedBy = "areaComun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistSaludAmbiente> checklists = new ArrayList<>();


    @OneToMany(mappedBy = "areaComun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroMantenimientoAmbiental> mantenimientos = new ArrayList<>();

    @OneToMany(mappedBy = "areaComun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TareaMantenimiento> tareasMantenimiento = new ArrayList<>();

    @OneToMany(mappedBy = "areaComun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalificacionArea> calificaciones = new ArrayList<>();

    @OneToMany(mappedBy = "areaComun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstadoArea> estados = new ArrayList<>();

    public AreaComun() {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getNormasUso() {
        return normasUso;
    }

    public void setNormasUso(String normasUso) {
        this.normasUso = normasUso;
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

    public List<ReservaAreaComun> getReservas() {
        return reservas;
    }

    public void setReservas(List<ReservaAreaComun> reservas) {
        this.reservas = reservas;
    }

    public List<IncidenciaAreaComun> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(List<IncidenciaAreaComun> incidencias) {
        this.incidencias = incidencias;
    }

    public List<ChecklistSaludAmbiente> getChecklists() {
        return checklists;
    }

    public void setChecklists(List<ChecklistSaludAmbiente> checklists) {
        this.checklists = checklists;
    }


    public List<RegistroMantenimientoAmbiental> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(List<RegistroMantenimientoAmbiental> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    public List<TareaMantenimiento> getTareasMantenimiento() {
        return tareasMantenimiento;
    }

    public void setTareasMantenimiento(List<TareaMantenimiento> tareasMantenimiento) {
        this.tareasMantenimiento = tareasMantenimiento;
    }

    public List<CalificacionArea> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<CalificacionArea> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public List<EstadoArea> getEstados() {
        return estados;
    }

    public void setEstados(List<EstadoArea> estados) {
        this.estados = estados;
    }
}
