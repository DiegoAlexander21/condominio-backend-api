package pe.edu.utp.condominio.api.dominios.comunicacion.dto;

public class ComunicadoTorreDto {
    private Long condominioId;
    private String torre;

    public ComunicadoTorreDto() {
    }

    public ComunicadoTorreDto(Long condominioId, String torre) {
        this.condominioId = condominioId;
        this.torre = torre;
    }

    public Long getCondominioId() {
        return condominioId;
    }

    public void setCondominioId(Long condominioId) {
        this.condominioId = condominioId;
    }

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }
}
