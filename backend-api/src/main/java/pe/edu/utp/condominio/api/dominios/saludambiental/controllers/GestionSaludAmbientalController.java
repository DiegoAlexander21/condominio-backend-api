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
    public String mostrarDashboardSalud(Model model) {
        return "salud-ambiental/dashboard";
    }

    @GetMapping("/nuevo-checklist")
    public String mostrarFormularioChecklist(Model model) {
        if (!model.containsAttribute("checklistForm")) {
            model.addAttribute("checklistForm", new ChecklistForm());
        }
        return "salud-ambiental/formulario-checklist";
    }

    @PostMapping("/checklists")
    public String crearChecklist(
            @Valid @ModelAttribute("checklistForm") ChecklistForm formulario,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "salud-ambiental/formulario-checklist";
        }

        saludAmbientalService.crearChecklist(formulario);
        redirectAttributes.addFlashAttribute("successMessage", "Checklist creado correctamente.");
        return "redirect:/salud-ambiental";
    }

    @GetMapping("/evaluar/{checklistId}")
    public String mostrarFormularioEvaluacion(@PathVariable Long checklistId, Model model) {
        EvaluacionForm form = new EvaluacionForm();
        form.setChecklistId(checklistId);
        model.addAttribute("evaluacionForm", form);
        return "salud-ambiental/formulario-evaluacion";
    }

    @PostMapping("/evaluaciones")
    public String registrarEvaluacion(
            @Valid @ModelAttribute("evaluacionForm") EvaluacionForm formulario,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "salud-ambiental/formulario-evaluacion";
        }

        saludAmbientalService.evaluarChecklist(formulario);
        redirectAttributes.addFlashAttribute("successMessage", "Evaluación registrada correctamente.");
        return "redirect:/salud-ambiental";
    }

    @GetMapping("/mantenimiento/nuevo")
    public String mostrarFormularioMantenimiento(Model model) {
        model.addAttribute("mantenimientoForm", new MantenimientoAmbientalForm());
        return "salud-ambiental/formulario-mantenimiento";
    }

    @PostMapping("/mantenimientos")
    public String registrarMantenimiento(
            @Valid @ModelAttribute("mantenimientoForm") MantenimientoAmbientalForm formulario,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "salud-ambiental/formulario-mantenimiento";
        }

        saludAmbientalService.registrarMantenimiento(formulario);
        redirectAttributes.addFlashAttribute("successMessage", "Registro de mantenimiento ambiental guardado.");
        return "redirect:/salud-ambiental";
    }
}
