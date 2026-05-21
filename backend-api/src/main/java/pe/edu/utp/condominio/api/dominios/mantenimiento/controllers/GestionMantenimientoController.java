package pe.edu.utp.condominio.api.dominios.mantenimiento.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request.InsumoForm;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request.TareaMantenimientoForm;
import pe.edu.utp.condominio.api.dominios.mantenimiento.services.GestionMantenimientoService;

@Controller
@RequestMapping("/mantenimiento")
public class GestionMantenimientoController {

    private final GestionMantenimientoService mantenimientoService;

    public GestionMantenimientoController(GestionMantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @GetMapping
    public String mostrarPanelControl(Model model) {
        model.addAttribute("insumos", mantenimientoService.listarInsumos());
        model.addAttribute("insumosCriticos", mantenimientoService.listarInsumosCriticos());
        return "mantenimiento/panel-control";
    }

    @GetMapping("/nuevo-insumo")
    public String mostrarFormularioInsumo(Model model) {
        model.addAttribute("insumoForm", new InsumoForm());
        return "mantenimiento/formulario-insumo";
    }

    @PostMapping("/insumos")
    public String registrarInsumo(
            @Valid @ModelAttribute("insumoForm") InsumoForm formulario,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "mantenimiento/formulario-insumo";
        }

        mantenimientoService.registrarInsumo(formulario);
        redirectAttributes.addFlashAttribute("successMessage", "Insumo registrado correctamente.");
        return "redirect:/mantenimiento";
    }

    @GetMapping("/nueva-tarea")
    public String mostrarFormularioTarea(Model model) {
        model.addAttribute("tareaForm", new TareaMantenimientoForm());
        model.addAttribute("insumosDisponibles", mantenimientoService.listarInsumos());
        return "mantenimiento/formulario-tarea";
    }

    @PostMapping("/tareas")
    public String registrarTarea(
            @Valid @ModelAttribute("tareaForm") TareaMantenimientoForm formulario,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("insumosDisponibles", mantenimientoService.listarInsumos());
            return "mantenimiento/formulario-tarea";
        }

        try {
            mantenimientoService.registrarTareaConInsumos(formulario);
            redirectAttributes.addFlashAttribute("successMessage", "Tarea registrada y costos distribuidos en finanzas.");
            return "redirect:/mantenimiento";
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("insumosDisponibles", mantenimientoService.listarInsumos());
            return "mantenimiento/formulario-tarea";
        }
    }
}
