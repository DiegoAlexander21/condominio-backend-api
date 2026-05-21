package pe.edu.utp.condominio.api.dominios.finanzas.dto.response;

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
    private LocalDateTime fechaRegistro;

    public GastoResponse() {
    }

    public GastoResponse(Long id, String descripcion, TipoGasto tipoGasto,
            MetodoDistribucion metodoDistribucion, double montoTotal,
            Long incidenciaId, LocalDateTime fechaRegistro) {
        this.id = id;
        this.descripcion = descripcion;
        this.tipoGasto = tipoGasto;
        this.metodoDistribucion = metodoDistribucion;
        this.montoTotal = montoTotal;
        this.incidenciaId = incidenciaId;
        this.fechaRegistro = fechaRegistro;
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

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
