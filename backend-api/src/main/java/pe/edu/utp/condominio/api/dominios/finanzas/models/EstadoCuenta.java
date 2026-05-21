package pe.edu.utp.condominio.api.dominios.finanzas.models;

import java.time.LocalDate;
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
@Table(name = "estados_cuenta", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "unidad_id", "periodo" })
})
public class EstadoCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_id", nullable = false)
    private Unidad unidad;

    @Column(nullable = false)
    private LocalDate periodo;

    @Column(nullable = false)
    private double totalCuotas;

    @Column(nullable = false)
    private double totalExtraordinarios;

    @Column(nullable = false)
    private double totalPagado;

    @Column(nullable = false)
    private double saldo;

    @Column(nullable = false)
    private LocalDateTime fechaGeneracion;

    public EstadoCuenta() {
    }

    @PrePersist
    public void prepararRegistro() {
        fechaGeneracion = LocalDateTime.now();
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
