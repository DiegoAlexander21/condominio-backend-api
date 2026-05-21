package pe.edu.utp.condominio.api.dominios.comunicacion.dto.response;

public class OpcionResultadoResponse {

    private Long opcionId;
    private String texto;
    private long votos;

    public OpcionResultadoResponse() {
    }

    public OpcionResultadoResponse(Long opcionId, String texto, long votos) {
        this.opcionId = opcionId;
        this.texto = texto;
        this.votos = votos;
    }

    public Long getOpcionId() {
        return opcionId;
    }

    public void setOpcionId(Long opcionId) {
        this.opcionId = opcionId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public long getVotos() {
        return votos;
    }

    public void setVotos(long votos) {
        this.votos = votos;
    }
}
