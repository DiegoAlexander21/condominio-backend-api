package pe.edu.utp.condominio.api.dto;

public class CondominioForm {

    private String nombre;
    private int torres;
    private int pisosPorTorre;

    public CondominioForm() {
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
