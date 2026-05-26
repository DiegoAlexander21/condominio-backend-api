package pe.edu.utp.condominio.api.dominios.reportes.dto.response;

import java.util.List;

public class ReporteDashboardResponse {

    private List<IncidenciaFrecuenteResponse> incidenciasFrecuentes;
    private List<AreaGastoResponse> areasConMayorGasto;
    private List<UnidadMorosaResponse> unidadesMorosas;
    private List<UnidadMorosaResponse> unidadesConMayorDeuda;

    public ReporteDashboardResponse() {
    }

    public ReporteDashboardResponse(List<IncidenciaFrecuenteResponse> incidenciasFrecuentes,
            List<AreaGastoResponse> areasConMayorGasto,
            List<UnidadMorosaResponse> unidadesMorosas,
            List<UnidadMorosaResponse> unidadesConMayorDeuda) {
        this.incidenciasFrecuentes = incidenciasFrecuentes;
        this.areasConMayorGasto = areasConMayorGasto;
        this.unidadesMorosas = unidadesMorosas;
        this.unidadesConMayorDeuda = unidadesConMayorDeuda;
    }

    public List<IncidenciaFrecuenteResponse> getIncidenciasFrecuentes() {
        return incidenciasFrecuentes;
    }

    public void setIncidenciasFrecuentes(List<IncidenciaFrecuenteResponse> incidenciasFrecuentes) {
        this.incidenciasFrecuentes = incidenciasFrecuentes;
    }

    public List<AreaGastoResponse> getAreasConMayorGasto() {
        return areasConMayorGasto;
    }

    public void setAreasConMayorGasto(List<AreaGastoResponse> areasConMayorGasto) {
        this.areasConMayorGasto = areasConMayorGasto;
    }

    public List<UnidadMorosaResponse> getUnidadesMorosas() {
        return unidadesMorosas;
    }

    public void setUnidadesMorosas(List<UnidadMorosaResponse> unidadesMorosas) {
        this.unidadesMorosas = unidadesMorosas;
    }

    public List<UnidadMorosaResponse> getUnidadesConMayorDeuda() {
        return unidadesConMayorDeuda;
    }

    public void setUnidadesConMayorDeuda(List<UnidadMorosaResponse> unidadesConMayorDeuda) {
        this.unidadesConMayorDeuda = unidadesConMayorDeuda;
    }
}
