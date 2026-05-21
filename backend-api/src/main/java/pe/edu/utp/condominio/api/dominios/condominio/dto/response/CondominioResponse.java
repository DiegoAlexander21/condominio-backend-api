package pe.edu.utp.condominio.api.dominios.condominio.dto.response;

public class CondominioResponse {

    private Long id;
    private String nombre;
    private int torres;
    private int pisosPorTorre;

    public CondominioResponse() {
    }

    public CondominioResponse(Long id, String nombre, int torres, int pisosPorTorre) {
        this.id = id;
        this.nombre = nombre;
        this.torres = torres;
        this.pisosPorTorre = pisosPorTorre;
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
}
