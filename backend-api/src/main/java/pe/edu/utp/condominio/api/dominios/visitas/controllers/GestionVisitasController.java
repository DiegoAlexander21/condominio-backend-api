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
    public String listarPorEstado(@RequestParam(value = "estado", required = false) EstadoVisita estado, Model model) {
        if (estado != null) {
            model.addAttribute("visitas", gestionVisitasService.listarPorEstado(estado));
        }
        return "visitas/lista-visitas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioProgramacion(Model model) {
        model.addAttribute("visitaForm", new VisitaForm());
        return "visitas/formulario-programacion";
    }

    @PostMapping
    public String registrarVisita(
            @Valid @ModelAttribute("visitaForm") VisitaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "visitas/formulario-programacion";
        }

        gestionVisitasService.registrarVisita(form);
        redirectAttributes.addFlashAttribute("successMessage", "Visita programada correctamente.");
        return "redirect:/visitas";
    }

    @GetMapping("/ingreso")
    public String mostrarFormularioIngreso(@RequestParam("visitaId") Long visitaId, Model model) {
        RegistroIngresoVisitaForm form = new RegistroIngresoVisitaForm();
        form.setVisitaId(visitaId);
        model.addAttribute("ingresoForm", form);
        return "visitas/formulario-ingreso";
    }

    @PostMapping("/ingreso")
    public String registrarIngreso(
            @Valid @ModelAttribute("ingresoForm") RegistroIngresoVisitaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "visitas/formulario-ingreso";
        }

        gestionVisitasService.registrarIngreso(form);
        redirectAttributes.addFlashAttribute("successMessage", "Ingreso de visita registrado.");
        return "redirect:/visitas";
    }

    @GetMapping("/salida")
    public String mostrarFormularioSalida(@RequestParam("visitaId") Long visitaId, Model model) {
        RegistroSalidaVisitaForm form = new RegistroSalidaVisitaForm();
        form.setVisitaId(visitaId);
        model.addAttribute("salidaForm", form);
        return "visitas/formulario-salida";
    }

    @PostMapping("/salida")
    public String registrarSalida(
            @Valid @ModelAttribute("salidaForm") RegistroSalidaVisitaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "visitas/formulario-salida";
        }

        gestionVisitasService.registrarSalida(form);
        redirectAttributes.addFlashAttribute("successMessage", "Salida de visita registrada.");
        return "redirect:/visitas";
    }
}
