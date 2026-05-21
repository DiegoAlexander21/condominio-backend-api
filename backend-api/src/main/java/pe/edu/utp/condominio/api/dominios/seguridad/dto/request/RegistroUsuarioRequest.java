package pe.edu.utp.condominio.api.dominios.seguridad.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.utp.condominio.api.dominios.seguridad.enums.NombreRol;
import pe.edu.utp.condominio.api.dominios.seguridad.enums.TipoDocumento;

public class RegistroUsuarioRequest {

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotNull
    private TipoDocumento tipoDocumento;

    @NotBlank
    private String numeroDocumento;

    @NotBlank
    private String telefono;

    @NotBlank
    @jakarta.validation.constraints.Email
    private String correo;

    @NotBlank
    private String contrasena;

    @NotNull
    private NombreRol rol;

    public RegistroUsuarioRequest() {
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

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public NombreRol getRol() {
        return rol;
    }

    public void setRol(NombreRol rol) {
        this.rol = rol;
    }
}
