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
    public String mostrarPanelControl(Model modelo) {
        modelo.addAttribute("insumos", mantenimientoService.listarInsumos());
        modelo.addAttribute("insumosCriticos", mantenimientoService.listarInsumosCriticos());
        return "mantenimiento/panel-control";
    }

    @GetMapping("/nuevo-insumo")
    public String mostrarFormularioInsumo(Model modelo) {
        modelo.addAttribute("insumoForm", new InsumoForm());
        return "mantenimiento/formulario-insumo";
    }

    @PostMapping("/insumos")
    public String registrarInsumo(
            @Valid @ModelAttribute("insumoForm") InsumoForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {
        
        if (resultadoValidacion.hasErrors()) {
            return "mantenimiento/formulario-insumo";
        }

        mantenimientoService.registrarInsumo(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Insumo registrado correctamente.");
        return "redirect:/mantenimiento";
    }

    @GetMapping("/nueva-tarea")
    public String mostrarFormularioTarea(Model modelo) {
        modelo.addAttribute("tareaForm", new TareaMantenimientoForm());
        modelo.addAttribute("insumosDisponibles", mantenimientoService.listarInsumos());
        return "mantenimiento/formulario-tarea";
    }

    @PostMapping("/tareas")
    public String registrarTarea(
            @Valid @ModelAttribute("tareaForm") TareaMantenimientoForm formulario,
            BindingResult resultadoValidacion,
            Model modelo,
            RedirectAttributes atributosRedireccion) {
        
        if (resultadoValidacion.hasErrors()) {
            modelo.addAttribute("insumosDisponibles", mantenimientoService.listarInsumos());
            return "mantenimiento/formulario-tarea";
        }

        try {
            mantenimientoService.registrarTareaConInsumos(formulario);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Tarea registrada y costos distribuidos en finanzas.");
            return "redirect:/mantenimiento";
        } catch (RuntimeException ex) {
            modelo.addAttribute("mensajeError", ex.getMessage());
            modelo.addAttribute("insumosDisponibles", mantenimientoService.listarInsumos());
            return "mantenimiento/formulario-tarea";
        }
    }
}
