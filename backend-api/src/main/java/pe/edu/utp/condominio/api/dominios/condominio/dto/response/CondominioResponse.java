package pe.edu.utp.condominio.api.dominios.condominio.dto.response;

public class CondominioResponse {

    private Long id;
    private String nombre;
    private int torres;
    private int pisosPorTorre;
    private java.time.LocalDateTime fechaRegistro;

    public CondominioResponse() {
    }

    public CondominioResponse(Long id, String nombre, int torres, int pisosPorTorre, java.time.LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.torres = torres;
        this.pisosPorTorre = pisosPorTorre;
        this.fechaRegistro = fechaRegistro;
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

    public java.time.LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(java.time.LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
