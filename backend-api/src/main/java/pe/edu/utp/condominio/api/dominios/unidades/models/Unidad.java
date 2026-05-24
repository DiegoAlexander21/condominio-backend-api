package pe.edu.utp.condominio.api.dominios.unidades.models;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import pe.edu.utp.condominio.api.dominios.calificaciones.models.CalificacionArea;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.VotoAsamblea;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.finanzas.models.DetalleGastoUnidad;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EstadoCuenta;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Pago;
import pe.edu.utp.condominio.api.dominios.historial.models.HistorialTitularidad;
import pe.edu.utp.condominio.api.dominios.incidencias.models.IncidenciaUnidad;
import pe.edu.utp.condominio.api.dominios.paqueteria.models.Paquete;
import pe.edu.utp.condominio.api.dominios.visitas.models.Visita;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.ReservaAreaComun;

@Entity
@Table(name = "unidades")
public class Unidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condominio_id", nullable = false)
    private Condominio condominio;

    @Column(nullable = false, length = 30)
    private String numeroUnidad;

    @Column(nullable = false, length = 20)
    private String torre;

    @Column(nullable = false)
    private int piso;

    @Column(nullable = false)
    private double area;

    @OneToOne(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Propietario propietario;

    @OneToOne(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Residente residente;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialTitularidad> historialTitularidad = new ArrayList<>();

    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncidenciaUnidad> incidencias = new ArrayList<>();

    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleGastoUnidad> detallesGasto = new ArrayList<>();

    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstadoCuenta> estadosCuenta = new ArrayList<>();

    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visita> visitas = new ArrayList<>();

    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paquete> paquetes = new ArrayList<>();

    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VotoAsamblea> votos = new ArrayList<>();

    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalificacionArea> calificacionesAreas = new ArrayList<>();

    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaAreaComun> reservasAreasComunes = new ArrayList<>();

    public Unidad() {
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

    public String getNumeroUnidad() {
        return numeroUnidad;
    }

    public void setNumeroUnidad(String numeroUnidad) {
        this.numeroUnidad = numeroUnidad;
    }

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
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

    public List<HistorialTitularidad> getHistorialTitularidad() {
        return historialTitularidad;
    }

    public void setHistorialTitularidad(List<HistorialTitularidad> historialTitularidad) {
        this.historialTitularidad = historialTitularidad;
    }

    public List<IncidenciaUnidad> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(List<IncidenciaUnidad> incidencias) {
        this.incidencias = incidencias;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Residente getResidente() {
        return residente;
    }

    public void setResidente(Residente residente) {
        this.residente = residente;
    }

    public List<DetalleGastoUnidad> getDetallesGasto() {
        return detallesGasto;
    }

    public void setDetallesGasto(List<DetalleGastoUnidad> detallesGasto) {
        this.detallesGasto = detallesGasto;
    }

    public List<EstadoCuenta> getEstadosCuenta() {
        return estadosCuenta;
    }

    public void setEstadosCuenta(List<EstadoCuenta> estadosCuenta) {
        this.estadosCuenta = estadosCuenta;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public List<Visita> getVisitas() {
        return visitas;
    }

    public void setVisitas(List<Visita> visitas) {
        this.visitas = visitas;
    }

    public List<Paquete> getPaquetes() {
        return paquetes;
    }

    public void setPaquetes(List<Paquete> paquetes) {
        this.paquetes = paquetes;
    }

    public List<VotoAsamblea> getVotos() {
        return votos;
    }

    public void setVotos(List<VotoAsamblea> votos) {
        this.votos = votos;
    }

    public List<CalificacionArea> getCalificacionesAreas() {
        return calificacionesAreas;
    }

    public void setCalificacionesAreas(List<CalificacionArea> calificacionesAreas) {
        this.calificacionesAreas = calificacionesAreas;
    }

    public List<ReservaAreaComun> getReservasAreasComunes() {
        return reservasAreasComunes;
    }

    public void setReservasAreasComunes(List<ReservaAreaComun> reservasAreasComunes) {
        this.reservasAreasComunes = reservasAreasComunes;
    }
}