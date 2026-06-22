package pe.edu.utp.condominio.api.dominios.seguridad.dto.response;

public class UsuarioPerfilResponse {
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String correo;
    private String rol;
    private Long unidadId;
    private String nombreCondominio;
    private String torre;
    private Integer piso;
    private String numeroUnidad;

    public UsuarioPerfilResponse() {
    }

    public UsuarioPerfilResponse(String nombres, String apellidos, String numeroDocumento, String correo, String rol,
            Long unidadId, String nombreCondominio, String torre, Integer piso, String numeroUnidad) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.numeroDocumento = numeroDocumento;
        this.correo = correo;
        this.rol = rol;
        this.unidadId = unidadId;
        this.nombreCondominio = nombreCondominio;
        this.torre = torre;
        this.piso = piso;
        this.numeroUnidad = numeroUnidad;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public String getNombreCondominio() {
        return nombreCondominio;
    }

    public void setNombreCondominio(String nombreCondominio) {
        this.nombreCondominio = nombreCondominio;
    }

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public String getNumeroUnidad() {
        return numeroUnidad;
    }

    public void setNumeroUnidad(String numeroUnidad) {
        this.numeroUnidad = numeroUnidad;
    }
}
