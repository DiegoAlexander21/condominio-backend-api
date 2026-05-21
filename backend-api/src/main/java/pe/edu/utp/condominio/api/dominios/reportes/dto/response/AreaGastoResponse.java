package pe.edu.utp.condominio.api.dominios.reportes.dto.response;

public class AreaGastoResponse {

    private Long areaComunId;
    private String areaComunNombre;
    private double montoTotal;

    public AreaGastoResponse() {
    }

    public AreaGastoResponse(Long areaComunId, String areaComunNombre, double montoTotal) {
        this.areaComunId = areaComunId;
        this.areaComunNombre = areaComunNombre;
        this.montoTotal = montoTotal;
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

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
}
