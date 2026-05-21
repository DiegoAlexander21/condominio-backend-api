package pe.edu.utp.condominio.api.dominios.reportes.dto.response;

public class IncidenciaFrecuenteResponse {

    private Long areaComunId;
    private String areaComunNombre;
    private long totalIncidencias;

    public IncidenciaFrecuenteResponse() {
    }

    public IncidenciaFrecuenteResponse(Long areaComunId, String areaComunNombre, long totalIncidencias) {
        this.areaComunId = areaComunId;
        this.areaComunNombre = areaComunNombre;
        this.totalIncidencias = totalIncidencias;
    }

    public Long getAreaComunId() {
        return areaComunId;
    }

    public void setAreaComunId(Long areaComunId) {
        this.areaComunId = areaComunId;
    }

    public String getAreaComunNombre() {
        return areaComunNombre;
    }

    public void setAreaComunNombre(String areaComunNombre) {
        this.areaComunNombre = areaComunNombre;
    }

    public long getTotalIncidencias() {
        return totalIncidencias;
    }

    public void setTotalIncidencias(long totalIncidencias) {
        this.totalIncidencias = totalIncidencias;
    }
}
