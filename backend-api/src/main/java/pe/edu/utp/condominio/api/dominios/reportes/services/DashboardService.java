package pe.edu.utp.condominio.api.dominios.reportes.services;

import java.util.List;
import org.springframework.stereotype.Service;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.AreaGastoResponse;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.IncidenciaFrecuenteResponse;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.ReporteDashboardResponse;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.UnidadMorosaResponse;

@Service
public class DashboardService {

    private final ReporteIncidenciasService reporteIncidenciasService;
    private final ReporteFinanzasService reporteFinanzasService;

    public DashboardService(ReporteIncidenciasService reporteIncidenciasService,
            ReporteFinanzasService reporteFinanzasService) {
        this.reporteIncidenciasService = reporteIncidenciasService;
        this.reporteFinanzasService = reporteFinanzasService;
    }

    public ReporteDashboardResponse generarReporte(int limite) {
        int limiteSeguro = limite > 0 ? limite : 5;
        
        List<IncidenciaFrecuenteResponse> incidenciasFrecuentes = reporteIncidenciasService.obtenerIncidenciasFrecuentes(limiteSeguro);
        List<AreaGastoResponse> areasConMayorGasto = reporteFinanzasService.obtenerAreasConMayorGasto(limiteSeguro);
        List<UnidadMorosaResponse> unidadesMorosas = reporteFinanzasService.obtenerUnidadesMorosas(limiteSeguro);
        List<UnidadMorosaResponse> unidadesConMayorDeuda = reporteFinanzasService.obtenerUnidadesConMayorDeuda(limiteSeguro);

        return new ReporteDashboardResponse(incidenciasFrecuentes, areasConMayorGasto, unidadesMorosas, unidadesConMayorDeuda);
    }
}
