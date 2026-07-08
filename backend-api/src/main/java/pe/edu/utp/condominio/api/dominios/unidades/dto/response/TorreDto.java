package pe.edu.utp.condominio.api.dominios.unidades.dto.response;

public class TorreDto {
    private Long condominioId;
    private String torre;

    public TorreDto() {
    }

    public TorreDto(Long condominioId, String torre) {
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
