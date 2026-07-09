package pe.edu.utp.condominio.api.dominios.reportes.services;

import java.util.List;
import org.springframework.stereotype.Service;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.AreaGastoResponse;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.IncidenciaFrecuenteResponse;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.ReporteDashboardResponse;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.UnidadMorosaResponse;
import pe.edu.utp.condominio.api.dominios.calificaciones.services.EstadoAreaService;
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.response.EstadoAreaResponse;

@Service
public class DashboardService {

    private final ReporteIncidenciasService reporteIncidenciasService;
    private final ReporteFinanzasService reporteFinanzasService;
    private final EstadoAreaService estadoAreaService;

    public DashboardService(ReporteIncidenciasService reporteIncidenciasService,
            ReporteFinanzasService reporteFinanzasService,
            EstadoAreaService estadoAreaService) {
        this.reporteIncidenciasService = reporteIncidenciasService;
        this.reporteFinanzasService = reporteFinanzasService;
        this.estadoAreaService = estadoAreaService;
    }

    public ReporteDashboardResponse generarReporte(int limite) {
        int limiteSeguro = limite > 0 ? limite : 5;
        
        List<IncidenciaFrecuenteResponse> incidenciasFrecuentes = reporteIncidenciasService.obtenerIncidenciasFrecuentes(limiteSeguro);
        List<AreaGastoResponse> areasConMayorGasto = reporteFinanzasService.obtenerAreasConMayorGasto(limiteSeguro);
        List<UnidadMorosaResponse> unidadesMorosas = reporteFinanzasService.obtenerUnidadesMorosas(limiteSeguro);
        List<UnidadMorosaResponse> unidadesConMayorDeuda = reporteFinanzasService.obtenerUnidadesConMayorDeuda(limiteSeguro);
        
        List<EstadoAreaResponse> rankingAreas = 
                estadoAreaService.obtenerRankingAreas().stream().limit(limiteSeguro).toList();

        return new ReporteDashboardResponse(incidenciasFrecuentes, areasConMayorGasto, unidadesMorosas, unidadesConMayorDeuda, rankingAreas);
    }
}
