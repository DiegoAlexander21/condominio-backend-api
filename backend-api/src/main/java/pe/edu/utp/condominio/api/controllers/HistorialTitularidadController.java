package pe.edu.utp.condominio.api.controllers;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import pe.edu.utp.condominio.api.dto.HistorialForm;
import pe.edu.utp.condominio.api.services.HistorialTitularidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HistorialTitularidadController {

    private final HistorialTitularidadService historialService;

    public HistorialTitularidadController(HistorialTitularidadService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/historial-titularidad")
    public String mostrarHistorial(Model model) {
        model.addAttribute("listaHistorial", historialService.obtenerTodoElHistorial());
        if (!model.containsAttribute("historialForm")) {
            model.addAttribute("historialForm", new HistorialForm());
        }
        return "historial-titularidad";
    }

    @PostMapping("/historial-titularidad/registrar")
    public String registrarCambio(
            @Valid @ModelAttribute("historialForm") HistorialForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Revisa los campos del formulario.");
            model.addAttribute("listaHistorial", historialService.obtenerTodoElHistorial());
            return "historial-titularidad";
        }

        try {
            historialService.registrarCambioTitularidad(form);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Cambio de propietario registrado exitosamente.");
            return "redirect:/historial-titularidad";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("listaHistorial", historialService.obtenerTodoElHistorial());
            return "historial-titularidad";
        }
    }
}