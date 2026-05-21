package pe.edu.utp.condominio.api.dominios.calificaciones.controllers;

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
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.request.CalificacionForm;
import pe.edu.utp.condominio.api.dominios.calificaciones.services.GestionCalificacionesService;

@Controller
@RequestMapping("/calificaciones")
public class GestionCalificacionesController {

    private final GestionCalificacionesService calificacionesService;

    public GestionCalificacionesController(GestionCalificacionesService calificacionesService) {
        this.calificacionesService = calificacionesService;
    }

    @GetMapping("/area/{areaId}")
    public String mostrarCalificacionesArea(@PathVariable Long areaId, Model model) {
        model.addAttribute("calificaciones", calificacionesService.listarCalificacionesPorArea(areaId));
        model.addAttribute("estadoActual", calificacionesService.obtenerEstadoActual(areaId));
        return "calificaciones/lista-area";
    }

    @GetMapping("/registrar/{areaId}")
    public String mostrarFormularioCalificacion(@PathVariable Long areaId, Model model) {
        CalificacionForm form = new CalificacionForm();
        form.setAreaComunId(areaId);
        model.addAttribute("calificacionForm", form);
        return "calificaciones/formulario-registro";
    }

    @PostMapping("/registrar")
    public String registrarCalificacion(
            @Valid @ModelAttribute("calificacionForm") CalificacionForm formulario,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "calificaciones/formulario-registro";
        }

        calificacionesService.registrarCalificacion(formulario);
        redirectAttributes.addFlashAttribute("successMessage", "¡Gracias por tu calificación!");
        return "redirect:/calificaciones/area/" + formulario.getAreaComunId();
    }

    @PostMapping("/area/{areaId}/actualizar-estado")
    public String actualizarEstado(@PathVariable Long areaId, RedirectAttributes redirectAttributes) {
        calificacionesService.actualizarEstadoAutomatico(areaId);
        redirectAttributes.addFlashAttribute("successMessage", "Estado del área actualizado manualmente.");
        return "redirect:/calificaciones/area/" + areaId;
    }
}
