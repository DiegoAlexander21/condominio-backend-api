package pe.edu.utp.condominio.api.controllers;

import pe.edu.utp.condominio.api.dto.UnidadForm;
import pe.edu.utp.condominio.api.models.Unidad;
import pe.edu.utp.condominio.api.services.GestionUnidadesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GestionUnidadesController {

    private final GestionUnidadesService gestionUnidadesService;

    public GestionUnidadesController(GestionUnidadesService gestionUnidadesService) {
        this.gestionUnidadesService = gestionUnidadesService;
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
        return "registro-unidad";
    }

    @PostMapping("/gestion-unidades/unidad")
    public String registrarUnidad(@ModelAttribute("unidadForm") UnidadForm unidadForm,
            RedirectAttributes redirectAttributes) {
        try {
            gestionUnidadesService.registrarOActualizarUnidad(unidadForm);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Unidad registrada o actualizada correctamente.");
            return "redirect:/gestion-unidades";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("unidadForm", unidadForm);
            return "redirect:/registro-unidad";
        }
    }

    private void cargarModeloGestion(Model model) {
        model.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
        model.addAttribute("totalPropietarios", gestionUnidadesService.contarPropietarios());
        model.addAttribute("totalResidentes", gestionUnidadesService.contarResidentesActivos());
    }
}
