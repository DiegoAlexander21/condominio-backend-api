package pe.edu.utp.condominio.api.dominios.historial.controllers;

import pe.edu.utp.condominio.api.dominios.historial.services.HistorialTitularidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistorialTitularidadController {

    private final HistorialTitularidadService historialService;

    public HistorialTitularidadController(HistorialTitularidadService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/historial-titularidad")
    public String mostrarHistorial(Model modelo) {
        modelo.addAttribute("listaHistorial", historialService.obtenerTodoElHistorial());
        return "dominios/historial/historial-titularidad";
    }
}