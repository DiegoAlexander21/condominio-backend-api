package pe.edu.utp.condominio.api.dominios.unidades.dto.response;

public class UnidadResponse {

    private Long id;
    private String nombreCondominio;
    private String numeroUnidad;
    private String torre;
    private int piso;
    private double area;
    private String nombrePropietario;
    private String dniPropietario;
    private String telefonoPropietario;
    private String nombreResidente;
    private String emailResidente;
    private boolean residenteActivo;

    public UnidadResponse() {
    }

    public UnidadResponse(Long id, String nombreCondominio, String numeroUnidad, String torre,
            int piso, double area, String nombrePropietario, String dniPropietario,
            String telefonoPropietario, String nombreResidente, String emailResidente,
            boolean residenteActivo) {
        this.id = id;
        this.nombreCondominio = nombreCondominio;
        this.numeroUnidad = numeroUnidad;
        this.torre = torre;
        this.piso = piso;
        this.area = area;
        this.nombrePropietario = nombrePropietario;
        this.dniPropietario = dniPropietario;
        this.telefonoPropietario = telefonoPropietario;
        this.nombreResidente = nombreResidente;
        this.emailResidente = emailResidente;
        this.residenteActivo = residenteActivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCondominio() {
        return nombreCondominio;
    }

    public void setNombreCondominio(String nombreCondominio) {
        this.nombreCondominio = nombreCondominio;
    }

    public String getNumeroUnidad() {
        return numeroUnidad;
    }

    public void setNumeroUnidad(String numeroUnidad) {
        this.numeroUnidad = numeroUnidad;
    }

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getDniPropietario() {
        return dniPropietario;
    }

    public void setDniPropietario(String dniPropietario) {
        this.dniPropietario = dniPropietario;
    }

    public String getTelefonoPropietario() {
        return telefonoPropietario;
    }

    public void setTelefonoPropietario(String telefonoPropietario) {
        this.telefonoPropietario = telefonoPropietario;
    }

    public String getNombreResidente() {
        return nombreResidente;
    }

    public void setNombreResidente(String nombreResidente) {
        this.nombreResidente = nombreResidente;
    }

    public String getEmailResidente() {
        return emailResidente;
    }

    public void setEmailResidente(String emailResidente) {
        this.emailResidente = emailResidente;
    }

    public boolean isResidenteActivo() {
        return residenteActivo;
    }

    public void setResidenteActivo(boolean residenteActivo) {
        this.residenteActivo = residenteActivo;
    }
}
