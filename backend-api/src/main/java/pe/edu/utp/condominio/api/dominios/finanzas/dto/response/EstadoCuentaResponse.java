package pe.edu.utp.condominio.api.dominios.finanzas.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EstadoCuentaResponse {

    private Long id;
    private Long unidadId;
    private LocalDate periodo;
    private double totalCuotas;
    private double totalExtraordinarios;
    private double totalPagado;
    private double saldo;
    private LocalDateTime fechaGeneracion;

    public EstadoCuentaResponse() {
    }

    public EstadoCuentaResponse(Long id, Long unidadId, LocalDate periodo, double totalCuotas,
            double totalExtraordinarios, double totalPagado, double saldo,
            LocalDateTime fechaGeneracion) {
        this.id = id;
        this.unidadId = unidadId;
        this.periodo = periodo;
        this.totalCuotas = totalCuotas;
        this.totalExtraordinarios = totalExtraordinarios;
        this.totalPagado = totalPagado;
        this.saldo = saldo;
        this.fechaGeneracion = fechaGeneracion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public LocalDate getPeriodo() {
        return periodo;
    }

    public void setPeriodo(LocalDate periodo) {
        this.periodo = periodo;
    }

    public double getTotalCuotas() {
        return totalCuotas;
    }

    public void setTotalCuotas(double totalCuotas) {
        this.totalCuotas = totalCuotas;
    }

    public double getTotalExtraordinarios() {
        return totalExtraordinarios;
    }

    public void setTotalExtraordinarios(double totalExtraordinarios) {
        this.totalExtraordinarios = totalExtraordinarios;
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
}
