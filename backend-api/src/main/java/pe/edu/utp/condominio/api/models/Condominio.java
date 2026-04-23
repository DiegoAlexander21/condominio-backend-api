package pe.edu.utp.condominio.api.models;

import java.time.LocalDateTime;

public class Condominio {

    private Long id;

    private String nombre;
    private int torres;
    private int pisosPorTorre;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;

    public Condominio() {
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
}
