package pe.edu.utp.condominio.api.dominios.reportes.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.ReporteDashboardResponse;
import pe.edu.utp.condominio.api.dominios.reportes.services.DashboardService;

@RestController
@RequestMapping("/api/reportes")
public class ReporteRestController {

    private final DashboardService dashboardService;

    public ReporteRestController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ReporteDashboardResponse> obtenerDashboard(
            @RequestParam(name = "limite", required = false, defaultValue = "5") int limite) {
        return ResponseEntity.ok(dashboardService.generarReporte(limite));
    }
}
