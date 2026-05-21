package pe.edu.utp.condominio.api.dominios.paqueteria.controllers;

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
import pe.edu.utp.condominio.api.dominios.paqueteria.dto.request.PaqueteForm;
import pe.edu.utp.condominio.api.dominios.paqueteria.dto.request.RegistroEntregaPaqueteForm;
import pe.edu.utp.condominio.api.dominios.paqueteria.enums.EstadoPaquete;
import pe.edu.utp.condominio.api.dominios.paqueteria.services.GestionPaqueteriaService;

@Controller
@RequestMapping("/paqueteria")
public class GestionPaqueteriaController {

    private final GestionPaqueteriaService gestionPaqueteriaService;

    public GestionPaqueteriaController(GestionPaqueteriaService gestionPaqueteriaService) {
        this.gestionPaqueteriaService = gestionPaqueteriaService;
    }

    @GetMapping
    public String listarPorEstado(@RequestParam(value = "estado", required = false) EstadoPaquete estado, Model model) {
        if (estado != null) {
            model.addAttribute("paquetes", gestionPaqueteriaService.listarPorEstado(estado));
        }
        return "paqueteria/lista-paquetes";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRecepcion(Model model) {
        model.addAttribute("paqueteForm", new PaqueteForm());
        return "paqueteria/formulario-recepcion";
    }

    @PostMapping("/paquetes")
    public String registrarRecepcion(
            @Valid @ModelAttribute("paqueteForm") PaqueteForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "paqueteria/formulario-recepcion";
        }

        gestionPaqueteriaService.registrarRecepcion(form);
        redirectAttributes.addFlashAttribute("successMessage", "Paquete recibido y notificación enviada.");
        return "redirect:/paqueteria";
    }

    @GetMapping("/entregar")
    public String mostrarFormularioEntrega(@RequestParam("paqueteId") Long paqueteId, Model model) {
        RegistroEntregaPaqueteForm form = new RegistroEntregaPaqueteForm();
        form.setPaqueteId(paqueteId);
        model.addAttribute("entregaForm", form);
        return "paqueteria/formulario-entrega";
    }

    @PostMapping("/paquetes/entrega")
    public String registrarEntrega(
            @Valid @ModelAttribute("entregaForm") RegistroEntregaPaqueteForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "paqueteria/formulario-entrega";
        }

        gestionPaqueteriaService.registrarEntrega(form);
        redirectAttributes.addFlashAttribute("successMessage", "Entrega final del paquete registrada.");
        return "redirect:/paqueteria";
    }
}
