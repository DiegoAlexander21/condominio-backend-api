package pe.edu.utp.condominio.api.dominios.areascomunes.controllers;

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
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.AreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.services.GestionAreasComunesService;
import pe.edu.utp.condominio.api.dominios.condominio.services.GestionCondominioService;

@Controller
@RequestMapping("/areas-comunes")
public class GestionAreasComunesController {

    private final GestionAreasComunesService gestionAreasComunesService;
    private final GestionCondominioService gestionCondominioService;

    public GestionAreasComunesController(GestionAreasComunesService gestionAreasComunesService,
            GestionCondominioService gestionCondominioService) {
        this.gestionAreasComunesService = gestionAreasComunesService;
        this.gestionCondominioService = gestionCondominioService;
    }

    @GetMapping
    public String listarAreas(@RequestParam(value = "condominioId", required = false) Long condominioId, Model model) {
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        model.addAttribute("condominioIdSeleccionado", condominioId);
        if (condominioId != null) {
            model.addAttribute("areas", gestionAreasComunesService.listarPorCondominio(condominioId));
        }
        return "dominios/areascomunes/gestion-areas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        model.addAttribute("areaForm", new AreaComunForm());
        return "dominios/areascomunes/registro-area";
    }

    @PostMapping
    public String registrarArea(
            @Valid @ModelAttribute("areaForm") AreaComunForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            if (form.getId() != null) model.addAttribute("esEdicion", true);
            return "dominios/areascomunes/registro-area";
        }

        try {
            gestionAreasComunesService.registrarOActualizarArea(form);
            redirectAttributes.addFlashAttribute("successMessage", "Área común guardada correctamente.");
            return "redirect:/areas-comunes?condominioId=" + form.getCondominioId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            if (form.getId() != null) model.addAttribute("esEdicion", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "dominios/areascomunes/registro-area";
        }
    }

    @GetMapping("/editar-area")
    public String mostrarFormularioEdicion(@RequestParam("id") Long id, Model model) {
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        model.addAttribute("areaForm", gestionAreasComunesService.obtenerFormularioArea(id));
        model.addAttribute("esEdicion", true);
        return "dominios/areascomunes/registro-area";
    }

    @GetMapping("/eliminar-area")
    public String eliminarArea(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        Long condominioId = gestionAreasComunesService.obtenerFormularioArea(id).getCondominioId();
        gestionAreasComunesService.eliminarArea(id);
        redirectAttributes.addFlashAttribute("successMessage", "Área común eliminada correctamente.");
        return "redirect:/areas-comunes?condominioId=" + condominioId;
    }
}
