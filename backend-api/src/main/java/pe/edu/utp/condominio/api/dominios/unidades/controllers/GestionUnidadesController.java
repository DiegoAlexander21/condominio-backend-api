package pe.edu.utp.condominio.api.dominios.unidades.controllers;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.UnidadForm;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.condominio.services.GestionCondominioService;
import pe.edu.utp.condominio.api.dominios.unidades.services.GestionUnidadesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GestionUnidadesController {

    private final GestionUnidadesService gestionUnidadesService;
    private final GestionCondominioService gestionCondominioService;

    public GestionUnidadesController(GestionUnidadesService gestionUnidadesService,
            GestionCondominioService gestionCondominioService) {
        this.gestionUnidadesService = gestionUnidadesService;
        this.gestionCondominioService = gestionCondominioService;
    }

    @GetMapping("/gestion-unidades")
    public String mostrarGestionUnidades(Model model) {
        cargarModeloGestion(model);
        return "gestion-unidades";
    }

    @GetMapping("/registro-unidad")
    public String mostrarRegistroUnidad(Model model) {
        if (!model.containsAttribute("unidadForm")) {
            model.addAttribute("unidadForm", new UnidadForm());
            model.addAttribute("esEdicion", false);
        }
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        return "registro-unidad";
    }

    @GetMapping("/editar-unidad")
    public String editarUnidad(@RequestParam("id") Long id, Model model) {
        Unidad unidad = gestionUnidadesService.buscarPorId(id);
        if (unidad == null) {
            return "redirect:/gestion-unidades";
        }
        UnidadForm form = gestionUnidadesService.convertirAForm(unidad);
        model.addAttribute("unidadForm", form);
        model.addAttribute("esEdicion", true);
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        return "registro-unidad";
    }

    @PostMapping("/gestion-unidades/unidad")
    public String registrarUnidad(
            @Valid @ModelAttribute("unidadForm") UnidadForm unidadForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Revisa los campos del formulario.");
            model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            return "registro-unidad";
        }

        try {
            gestionUnidadesService.registrarOActualizarUnidad(unidadForm);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Unidad registrada o actualizada correctamente.");
            return "redirect:/gestion-unidades";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            return "registro-unidad";
        }
    }

    private void cargarModeloGestion(Model model) {
        model.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
        model.addAttribute("totalPropietarios", gestionUnidadesService.contarPropietarios());
        model.addAttribute("totalResidentes", gestionUnidadesService.contarResidentesActivos());
    }
}