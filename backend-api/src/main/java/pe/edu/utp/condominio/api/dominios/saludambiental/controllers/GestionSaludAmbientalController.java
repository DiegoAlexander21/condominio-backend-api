package pe.edu.utp.condominio.api.dominios.saludambiental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.ChecklistForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.EvaluacionForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.MantenimientoAmbientalForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.services.GestionSaludAmbientalService;

@Controller
@RequestMapping("/salud-ambiental")
public class GestionSaludAmbientalController {

    private final GestionSaludAmbientalService saludAmbientalService;

    public GestionSaludAmbientalController(GestionSaludAmbientalService saludAmbientalService) {
        this.saludAmbientalService = saludAmbientalService;
    }

    @GetMapping
    public String mostrarDashboardSalud(Model modelo) {
        return "salud-ambiental/dashboard";
    }

    @GetMapping("/nuevo-checklist")
    public String mostrarFormularioChecklist(Model modelo) {
        if (!modelo.containsAttribute("checklistForm")) {
            modelo.addAttribute("checklistForm", new ChecklistForm());
        }
        return "salud-ambiental/formulario-checklist";
    }

    @PostMapping("/checklists")
    public String crearChecklist(
            @Valid @ModelAttribute("checklistForm") ChecklistForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {
        
        if (resultadoValidacion.hasErrors()) {
            return "salud-ambiental/formulario-checklist";
        }

        saludAmbientalService.crearChecklist(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Checklist creado correctamente.");
        return "redirect:/salud-ambiental";
    }

    @GetMapping("/evaluar/{checklistId}")
    public String mostrarFormularioEvaluacion(@PathVariable Long checklistId, Model modelo) {
        EvaluacionForm formulario = new EvaluacionForm();
        formulario.setChecklistId(checklistId);
        modelo.addAttribute("evaluacionForm", formulario);
        return "salud-ambiental/formulario-evaluacion";
    }

    @PostMapping("/evaluaciones")
    public String registrarEvaluacion(
            @Valid @ModelAttribute("evaluacionForm") EvaluacionForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {
        
        if (resultadoValidacion.hasErrors()) {
            return "salud-ambiental/formulario-evaluacion";
        }

        saludAmbientalService.evaluarChecklist(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Evaluación registrada correctamente.");
        return "redirect:/salud-ambiental";
    }

    @GetMapping("/mantenimiento/nuevo")
    public String mostrarFormularioMantenimiento(Model modelo) {
        modelo.addAttribute("mantenimientoForm", new MantenimientoAmbientalForm());
        return "salud-ambiental/formulario-mantenimiento";
    }

    @PostMapping("/mantenimientos")
    public String registrarMantenimiento(
            @Valid @ModelAttribute("mantenimientoForm") MantenimientoAmbientalForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {
        
        if (resultadoValidacion.hasErrors()) {
            return "salud-ambiental/formulario-mantenimiento";
        }

        saludAmbientalService.registrarMantenimiento(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Registro de mantenimiento ambiental guardado.");
        return "redirect:/salud-ambiental";
    }
}
