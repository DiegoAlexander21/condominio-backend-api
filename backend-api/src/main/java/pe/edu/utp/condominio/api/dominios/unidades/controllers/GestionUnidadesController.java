package pe.edu.utp.condominio.api.dominios.unidades.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.condominio.services.GestionCondominioService;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.UnidadForm;
import pe.edu.utp.condominio.api.dominios.unidades.services.GestionUnidadesService;

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
    public String mostrarGestionUnidades(Model modelo) {
        cargarModeloGestion(modelo);
        return "dominios/unidades/gestion-unidades";
    }

    @GetMapping("/registro-unidad")
    public String mostrarRegistroUnidad(Model modelo) {
        if (!modelo.containsAttribute("unidadForm")) {
            modelo.addAttribute("unidadForm", new UnidadForm());
            modelo.addAttribute("esEdicion", false);
        }
        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        return "dominios/unidades/registro-unidad";
    }

    @GetMapping("/editar-unidad")
    public String editarUnidad(@RequestParam("id") Long id, Model modelo) {
        UnidadForm formulario = gestionUnidadesService.obtenerFormUnidad(id);
        if (formulario == null) {
            return "redirect:/gestion-unidades";
        }
        modelo.addAttribute("unidadForm", formulario);
        modelo.addAttribute("esEdicion", true);
        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        return "dominios/unidades/registro-unidad";
    }

    @GetMapping("/eliminar-unidad")
    public String eliminarUnidad(@RequestParam("id") Long id, RedirectAttributes atributosRedireccion) {
        try {
            gestionUnidadesService.eliminarUnidad(id);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Unidad eliminada correctamente.");
        } catch (Exception ex) {
            atributosRedireccion.addFlashAttribute("mensajeError", "Error al eliminar la unidad.");
        }
        return "redirect:/gestion-unidades";
    }

    @PostMapping("/gestion-unidades/unidad")
    public String registrarUnidad(
            @Valid @ModelAttribute("unidadForm") UnidadForm formularioUnidad,
            BindingResult resultadoValidacion,
            Model modelo,
            RedirectAttributes atributosRedireccion) {
        if (resultadoValidacion.hasErrors()) {
            modelo.addAttribute("mensajeError", "Revisa los campos del formulario.");
            modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            return "dominios/unidades/registro-unidad";
        }

        try {
            gestionUnidadesService.registrarOActualizarUnidad(formularioUnidad);
            atributosRedireccion.addFlashAttribute("mensajeExito",
                    "Unidad registrada o actualizada correctamente.");
            return "redirect:/gestion-unidades";
        } catch (IllegalArgumentException ex) {
            modelo.addAttribute("mensajeError", ex.getMessage());
            modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            return "dominios/unidades/registro-unidad";
        }
    }

    @GetMapping("/asignar-ocupantes")
    public String mostrarAsignarOcupantes(@RequestParam("id") Long id, Model modelo) {
        pe.edu.utp.condominio.api.dominios.unidades.dto.request.AsignarOcupantesForm formulario = gestionUnidadesService
                .obtenerFormOcupantes(id);
        if (formulario == null) {
            return "redirect:/gestion-unidades";
        }
        modelo.addAttribute("ocupantesForm", formulario);
        return "dominios/unidades/asignar-ocupantes";
    }

    @PostMapping("/gestion-unidades/ocupantes")
    public String asignarOcupantes(
            @Valid @ModelAttribute("ocupantesForm") pe.edu.utp.condominio.api.dominios.unidades.dto.request.AsignarOcupantesForm formulario,
            BindingResult resultadoValidacion,
            Model modelo,
            RedirectAttributes atributosRedireccion) {
        if (resultadoValidacion.hasErrors()) {
            modelo.addAttribute("mensajeError", "Revisa los campos obligatorios del propietario.");
            return "dominios/unidades/asignar-ocupantes";
        }

        try {
            gestionUnidadesService.asignarOcupantes(formulario);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Habitantes asignados correctamente.");
            return "redirect:/gestion-unidades";
        } catch (IllegalArgumentException ex) {
            modelo.addAttribute("mensajeError", ex.getMessage());
            return "dominios/unidades/asignar-ocupantes";
        }
    }

    private void cargarModeloGestion(Model modelo) {
        modelo.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
        modelo.addAttribute("totalPropietarios", gestionUnidadesService.contarPropietarios());
        modelo.addAttribute("totalResidentes", gestionUnidadesService.contarResidentesActivos());
    }
}