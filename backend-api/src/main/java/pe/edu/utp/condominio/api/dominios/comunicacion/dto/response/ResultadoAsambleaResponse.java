package pe.edu.utp.condominio.api.dominios.comunicacion.dto.response;

import java.util.List;

public class ResultadoAsambleaResponse {

    private Long asambleaId;
    private long totalVotos;
    private List<OpcionResultadoResponse> resultados;

    public ResultadoAsambleaResponse() {
    }

    public ResultadoAsambleaResponse(Long asambleaId, long totalVotos,
            List<OpcionResultadoResponse> resultados) {
        this.asambleaId = asambleaId;
        this.totalVotos = totalVotos;
        this.resultados = resultados;
    }

    public Long getAsambleaId() {
        return asambleaId;
    }

    public void setAsambleaId(Long asambleaId) {
        this.asambleaId = asambleaId;
    }

    public long getTotalVotos() {
        return totalVotos;
    }

    public void setTotalVotos(long totalVotos) {
        this.totalVotos = totalVotos;
    }

    public List<OpcionResultadoResponse> getResultados() {
        return resultados;
    }

    public void setResultados(List<OpcionResultadoResponse> resultados) {
        this.resultados = resultados;
    }
}
