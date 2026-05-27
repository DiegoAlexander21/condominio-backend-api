package pe.edu.utp.condominio.api.dominios.reportes.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pe.edu.utp.condominio.api.dominios.reportes.services.ReportesDashboardService;

@Controller
@RequestMapping("/reportes")
public class GestionReportesController {

    private final ReportesDashboardService reportesDashboardService;

    public GestionReportesController(ReportesDashboardService reportesDashboardService) {
        this.reportesDashboardService = reportesDashboardService;
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(
            @RequestParam(name = "limite", required = false, defaultValue = "5") int limite,
            Model model) {
        model.addAttribute("dashboard", reportesDashboardService.generarReporte(limite));
        return "dominios/reportes/dashboard";
    }
}
