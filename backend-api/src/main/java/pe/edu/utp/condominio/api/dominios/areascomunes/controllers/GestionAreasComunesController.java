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
    public String listarAreas(@RequestParam(value = "condominioId", required = false) Long condominioId, Model modelo) {
        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        modelo.addAttribute("condominioIdSeleccionado", condominioId);
        if (condominioId != null) {
            modelo.addAttribute("areas", gestionAreasComunesService.listarPorCondominio(condominioId));
        }
        return "dominios/areascomunes/gestion-areas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        modelo.addAttribute("formularioArea", new AreaComunForm());
        return "dominios/areascomunes/registro-area";
    }

    @PostMapping
    public String registrarArea(
            @Valid @ModelAttribute("formularioArea") AreaComunForm formulario,
            BindingResult resultadoValidacion,
            Model modelo,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            if (formulario.getId() != null)
                modelo.addAttribute("esEdicion", true);
            return "dominios/areascomunes/registro-area";
        }

        try {
            gestionAreasComunesService.registrarOActualizarArea(formulario);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Área común guardada correctamente.");
            return "redirect:/areas-comunes?condominioId=" + formulario.getCondominioId();
        } catch (IllegalArgumentException excepcion) {
            modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            if (formulario.getId() != null)
                modelo.addAttribute("esEdicion", true);
            modelo.addAttribute("mensajeError", excepcion.getMessage());
            return "dominios/areascomunes/registro-area";
        }
    }

    @GetMapping("/editar-area")
    public String mostrarFormularioEdicion(@RequestParam("id") Long id, Model modelo) {
        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        modelo.addAttribute("formularioArea", gestionAreasComunesService.obtenerFormularioArea(id));
        modelo.addAttribute("esEdicion", true);
        return "dominios/areascomunes/registro-area";
    }

    @GetMapping("/eliminar-area")
    public String eliminarArea(@RequestParam("id") Long id, RedirectAttributes atributosRedireccion) {
        Long condominioId = gestionAreasComunesService.obtenerFormularioArea(id).getCondominioId();
        gestionAreasComunesService.eliminarArea(id);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Área común eliminada correctamente.");
        return "redirect:/areas-comunes?condominioId=" + condominioId;
    }
}
