package pe.edu.utp.condominio.api.dominios.finanzas.models;

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
import java.time.LocalDateTime;

@Entity
@Table(name = "evidencias_pagos")
public class EvidenciaPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id", nullable = false)
    private Pago pago;

    @Column(nullable = false, length = 500)
    private String urlArchivo;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    public EvidenciaPago() {
    }

    @PrePersist
    public void prepararRegistro() {
        fechaRegistro = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
