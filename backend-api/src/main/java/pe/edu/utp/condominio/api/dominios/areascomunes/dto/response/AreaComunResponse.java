package pe.edu.utp.condominio.api.dominios.areascomunes.dto.response;

import java.time.LocalTime;

public class AreaComunResponse {

    private Long id;
    private Long condominioId;
    private String nombre;
    private int capacidad;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String normasUso;

    public AreaComunResponse() {
    }

    public AreaComunResponse(Long id, Long condominioId, String nombre, int capacidad,
            LocalTime horaInicio, LocalTime horaFin, String normasUso) {
        this.id = id;
        this.condominioId = condominioId;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.normasUso = normasUso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCondominioId() {
        return condominioId;
    }

    public void setCondominioId(Long condominioId) {
        this.condominioId = condominioId;
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
}
