package pe.edu.utp.condominio.api.controllers;

import pe.edu.utp.condominio.api.dto.CondominioForm;
import pe.edu.utp.condominio.api.services.GestionCondominioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GestionCondominioController {

    private final GestionCondominioService gestionCondominioService;

    public GestionCondominioController(GestionCondominioService gestionCondominioService) {
        this.gestionCondominioService = gestionCondominioService;
    }

    @GetMapping("/gestion-condominio")
    public String mostrarGestionCondominio(Model model) {
        cargarModeloGestion(model);
        return "gestion-condominio";
    }

    @GetMapping({ "/registro-condominio", "/registro-condomino" })
    public String mostrarRegistroCondominio(Model model) {
        if (!model.containsAttribute("condominioForm")) {
            model.addAttribute("condominioForm", new CondominioForm());
        }
        return "registro-condominio";
    }

    @PostMapping("/gestion-condominio/condominio")
    public String registrarCondominio(@ModelAttribute("condominioForm") CondominioForm condominioForm,
            RedirectAttributes redirectAttributes) {
        try {
            gestionCondominioService.registrarOActualizarCondominio(condominioForm);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Condominio registrado o actualizado correctamente.");
            return "redirect:/gestion-condominio";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("condominioForm", condominioForm);
            return "redirect:/registro-condominio";
        }
    }

    private void cargarModeloGestion(Model model) {
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        model.addAttribute("totalTorres", gestionCondominioService.obtenerTotalTorres());
        model.addAttribute("totalPisos", gestionCondominioService.obtenerTotalPisosPorTorre());
    }
}
