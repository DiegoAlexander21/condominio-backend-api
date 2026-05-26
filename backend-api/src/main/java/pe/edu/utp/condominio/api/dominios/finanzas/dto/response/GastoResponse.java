package pe.edu.utp.condominio.api.dominios.finanzas.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import pe.edu.utp.condominio.api.dominios.finanzas.enums.MetodoDistribucion;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;

public class GastoResponse {

    private Long id;
    private String descripcion;
    private TipoGasto tipoGasto;
    private MetodoDistribucion metodoDistribucion;
    private double montoTotal;
    private Long incidenciaId;
    private Long unidadIdCausante;
    private LocalDateTime fechaRegistro;
    private boolean distribuido;
    private LocalDate fechaLimite;
    private Long condominioId;
    private String condominioNombre;
    private String torre;

    public GastoResponse() {
    }

    public GastoResponse(Long id, String descripcion,
            TipoGasto tipoGasto,
            MetodoDistribucion metodoDistribucion,
            Long incidenciaId,
            double montoTotal,
            LocalDateTime fechaRegistro,
            LocalDate fechaLimite,
            Long condominioId,
            String condominioNombre,
            String torre) {
        this.id = id;
        this.descripcion = descripcion;
        this.tipoGasto = tipoGasto;
        this.metodoDistribucion = metodoDistribucion;
        this.incidenciaId = incidenciaId;
        this.montoTotal = montoTotal;
        this.fechaRegistro = fechaRegistro;
        this.fechaLimite = fechaLimite;
        this.condominioId = condominioId;
        this.condominioNombre = condominioNombre;
        this.torre = torre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoGasto getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(TipoGasto tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public MetodoDistribucion getMetodoDistribucion() {
        return metodoDistribucion;
    }

    public void setMetodoDistribucion(MetodoDistribucion metodoDistribucion) {
        this.metodoDistribucion = metodoDistribucion;
    }

    public String getMetodoDistribucionFormateado() {
        if (metodoDistribucion == null)
            return "";
        if (metodoDistribucion == MetodoDistribucion.PARTES_IGUALES)
            return "Partes Iguales";
        if (metodoDistribucion == MetodoDistribucion.COEFICIENTE_TAMANO)
            return "Coeficiente Tamaño";
        if (metodoDistribucion == MetodoDistribucion.COBRO_DIRECTO)
            return "Cobro Directo";
        return metodoDistribucion.name();
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Long getIncidenciaId() {
        return incidenciaId;
    }

    public void setIncidenciaId(Long incidenciaId) {
        this.incidenciaId = incidenciaId;
    }

    public Long getUnidadIdCausante() {
        return unidadIdCausante;
    }

    public void setUnidadIdCausante(Long unidadIdCausante) {
        this.unidadIdCausante = unidadIdCausante;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isDistribuido() {
        return distribuido;
    }

    public void setDistribuido(boolean distribuido) {
        this.distribuido = distribuido;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Long getCondominioId() {
        return condominioId;
    }

    public void setCondominioId(Long condominioId) {
        this.condominioId = condominioId;
    }

    public String getCondominioNombre() {
        return condominioNombre;
    }

    public void setCondominioNombre(String condominioNombre) {
        this.condominioNombre = condominioNombre;
    }

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }
}
