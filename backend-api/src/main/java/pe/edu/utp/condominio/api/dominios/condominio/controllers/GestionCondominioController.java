package pe.edu.utp.condominio.api.dominios.condominio.controllers;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.condominio.dto.request.CondominioForm;
import pe.edu.utp.condominio.api.dominios.condominio.services.GestionCondominioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        return "dominios/condominios/gestion-condominios";
    }

    @GetMapping("/registro-condominio")
    public String mostrarRegistroCondominio(Model model) {
        if (!model.containsAttribute("condominioForm")) {
            model.addAttribute("condominioForm", new CondominioForm());
        }
        model.addAttribute("esEdicion", false);
        return "dominios/condominios/registro-condominio";
    }

    @GetMapping("/editar-condominio")
    public String editarCondominio(Long id, Model model, RedirectAttributes redirectAttributes) {
        CondominioForm form = gestionCondominioService.obtenerFormCondominio(id);
        if (form == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Condominio no encontrado.");
            return "redirect:/gestion-condominio";
        }
        model.addAttribute("condominioForm", form);
        model.addAttribute("esEdicion", true);
        return "dominios/condominios/registro-condominio";
    }

    @GetMapping("/eliminar-condominio")
    public String eliminarCondominio(Long id, RedirectAttributes redirectAttributes) {
        try {
            gestionCondominioService.eliminarCondominio(id);
            redirectAttributes.addFlashAttribute("successMessage", "Condominio eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el condominio.");
        }
        return "redirect:/gestion-condominio";
    }

    @PostMapping("/gestion-condominio/condominio")
    public String registrarCondominio(
            @Valid @ModelAttribute CondominioForm condominioForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Revisa los campos del formulario.");
            return "dominios/condominios/registro-condominio";
        }

        try {
            gestionCondominioService.registrarOActualizarCondominio(condominioForm);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Condominio registrado o actualizado correctamente.");
            return "redirect:/gestion-condominio";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "dominios/condominios/registro-condominio";
        }
    }

    private void cargarModeloGestion(Model model) {
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        model.addAttribute("totalTorres", gestionCondominioService.obtenerTotalTorres());
        model.addAttribute("totalPisos", gestionCondominioService.obtenerTotalPisos());
    }
}
