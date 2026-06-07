package pe.edu.utp.condominio.api.dominios.seguridad.dto.response;

public class LoginResponse {

    private String tokenAcceso;

    public LoginResponse(String tokenAcceso) {
        this.tokenAcceso = tokenAcceso;
    }

    public String getTokenAcceso() {
        return tokenAcceso;
    }

    public void setTokenAcceso(String tokenAcceso) {
        this.tokenAcceso = tokenAcceso;
    }
}
