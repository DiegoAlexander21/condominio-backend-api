package pe.edu.utp.condominio.api.dominios.visitas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.visitas.dto.request.RegistroIngresoVisitaForm;
import pe.edu.utp.condominio.api.dominios.visitas.dto.request.RegistroSalidaVisitaForm;
import pe.edu.utp.condominio.api.dominios.visitas.dto.request.VisitaForm;
import pe.edu.utp.condominio.api.dominios.visitas.enums.EstadoVisita;
import pe.edu.utp.condominio.api.dominios.visitas.services.GestionVisitasService;

@Controller
@RequestMapping("/visitas")
public class GestionVisitasController {

    private final GestionVisitasService gestionVisitasService;

    public GestionVisitasController(GestionVisitasService gestionVisitasService) {
        this.gestionVisitasService = gestionVisitasService;
    }

    @GetMapping
    public String listarPorEstado(@RequestParam(value = "estado", required = false) EstadoVisita estado, Model modelo) {
        if (estado != null) {
            modelo.addAttribute("visitas", gestionVisitasService.listarPorEstado(estado));
        }
        return "visitas/lista-visitas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioProgramacion(Model modelo) {
        modelo.addAttribute("visitaForm", new VisitaForm());
        return "visitas/formulario-programacion";
    }

    @PostMapping
    public String registrarVisita(
            @Valid @ModelAttribute("visitaForm") VisitaForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {
        
        if (resultadoValidacion.hasErrors()) {
            return "visitas/formulario-programacion";
        }

        gestionVisitasService.registrarVisita(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Visita programada correctamente.");
        return "redirect:/visitas";
    }

    @GetMapping("/ingreso")
    public String mostrarFormularioIngreso(@RequestParam("visitaId") Long visitaId, Model modelo) {
        RegistroIngresoVisitaForm formulario = new RegistroIngresoVisitaForm();
        formulario.setVisitaId(visitaId);
        modelo.addAttribute("ingresoForm", formulario);
        return "visitas/formulario-ingreso";
    }

    @PostMapping("/ingreso")
    public String registrarIngreso(
            @Valid @ModelAttribute("ingresoForm") RegistroIngresoVisitaForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {
        
        if (resultadoValidacion.hasErrors()) {
            return "visitas/formulario-ingreso";
        }

        gestionVisitasService.registrarIngreso(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Ingreso de visita registrado.");
        return "redirect:/visitas";
    }

    @GetMapping("/salida")
    public String mostrarFormularioSalida(@RequestParam("visitaId") Long visitaId, Model modelo) {
        RegistroSalidaVisitaForm formulario = new RegistroSalidaVisitaForm();
        formulario.setVisitaId(visitaId);
        modelo.addAttribute("salidaForm", formulario);
        return "visitas/formulario-salida";
    }

    @PostMapping("/salida")
    public String registrarSalida(
            @Valid @ModelAttribute("salidaForm") RegistroSalidaVisitaForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {
        
        if (resultadoValidacion.hasErrors()) {
            return "visitas/formulario-salida";
        }

        gestionVisitasService.registrarSalida(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Salida de visita registrada.");
        return "redirect:/visitas";
    }
}
