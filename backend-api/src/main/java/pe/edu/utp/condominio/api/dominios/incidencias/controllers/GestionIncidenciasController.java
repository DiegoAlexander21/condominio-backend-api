package pe.edu.utp.condominio.api.dominios.incidencias.controllers;

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
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.ActualizacionIncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.IncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.EstadoIncidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.services.GestionIncidenciasService;

@Controller
@RequestMapping("/incidencias")
public class GestionIncidenciasController {

    private final GestionIncidenciasService gestionIncidenciasService;

    public GestionIncidenciasController(GestionIncidenciasService gestionIncidenciasService) {
        this.gestionIncidenciasService = gestionIncidenciasService;
    }

    @GetMapping
    public String listarPorEstado(@RequestParam(value = "estado", required = false) EstadoIncidencia estado, Model model) {
        if (estado != null) {
            model.addAttribute("incidencias", gestionIncidenciasService.listarPorEstado(estado));
        }
        return "incidencias/lista-incidencias";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("incidenciaForm", new IncidenciaForm());
        return "incidencias/formulario-incidencia";
    }

    @PostMapping
    public String registrarIncidencia(
            @Valid @ModelAttribute("incidenciaForm") IncidenciaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "incidencias/formulario-incidencia";
        }

        gestionIncidenciasService.registrarIncidencia(form);
        redirectAttributes.addFlashAttribute("successMessage", "Incidencia reportada correctamente.");
        return "redirect:/incidencias";
    }

    @GetMapping("/actualizar")
    public String mostrarFormularioActualizacion(@RequestParam("incidenciaId") Long incidenciaId, Model model) {
        ActualizacionIncidenciaForm form = new ActualizacionIncidenciaForm();
        form.setIncidenciaId(incidenciaId);
        model.addAttribute("actualizacionForm", form);
        return "incidencias/formulario-actualizacion";
    }

    @PostMapping("/actualizar")
    public String actualizarEstado(
            @Valid @ModelAttribute("actualizacionForm") ActualizacionIncidenciaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "incidencias/formulario-actualizacion";
        }

        gestionIncidenciasService.actualizarEstado(form);
        redirectAttributes.addFlashAttribute("successMessage", "Estado de la incidencia actualizado.");
        return "redirect:/incidencias";
    }
}
