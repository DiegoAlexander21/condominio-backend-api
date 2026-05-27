package pe.edu.utp.condominio.api.dominios.incidencias.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.areascomunes.services.GestionAreasComunesService;
import pe.edu.utp.condominio.api.dominios.condominio.services.GestionCondominioService;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.ActualizacionIncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.EvidenciaIncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.IncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.response.IncidenciaResponse;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.EstadoIncidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.models.Incidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.services.GestionIncidenciasService;
import pe.edu.utp.condominio.api.dominios.unidades.services.GestionUnidadesService;

@Controller
@RequestMapping("/incidencias")
public class GestionIncidenciasController {

    private final GestionIncidenciasService gestionIncidenciasService;
    private final GestionCondominioService gestionCondominioService;
    private final GestionAreasComunesService gestionAreasComunesService;
    private final GestionUnidadesService gestionUnidadesService;

    public GestionIncidenciasController(GestionIncidenciasService gestionIncidenciasService,
            GestionCondominioService gestionCondominioService,
            GestionAreasComunesService gestionAreasComunesService,
            GestionUnidadesService gestionUnidadesService) {
        this.gestionIncidenciasService = gestionIncidenciasService;
        this.gestionCondominioService = gestionCondominioService;
        this.gestionAreasComunesService = gestionAreasComunesService;
        this.gestionUnidadesService = gestionUnidadesService;
    }

    @GetMapping
    public String listarPorEstado(@RequestParam(value = "estado", required = false) EstadoIncidencia estado,
            Model model) {
        if (estado == null) {
            return "redirect:/incidencias?estado=REGISTRADO";
        }
        model.addAttribute("incidencias", gestionIncidenciasService.listarPorEstado(estado));
        return "dominios/incidencias/lista-incidencias";
    }

    @GetMapping("/evidencias/{id}")
    @ResponseBody
    public List<String> obtenerEvidencias(@PathVariable("id") Long id) {
        return gestionIncidenciasService.listarEvidencias(id).stream()
                .map(e -> e.getUrlArchivo())
                .collect(Collectors.toList());
    }

    @GetMapping("/nuevo/area")
    public String mostrarFormularioRegistroArea(Model model) {
        model.addAttribute("incidenciaForm", new IncidenciaForm());
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        model.addAttribute("areasComunes", gestionAreasComunesService.obtenerTodasLasAreasComunes());
        return "dominios/incidencias/formulario-incidencia-area";
    }

    @GetMapping("/nuevo/unidad")
    public String mostrarFormularioRegistroUnidad(Model model) {
        model.addAttribute("incidenciaForm", new IncidenciaForm());
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        model.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
        return "dominios/incidencias/formulario-incidencia-unidad";
    }

    @PostMapping("/area")
    public String registrarIncidenciaArea(
            @Valid @ModelAttribute("incidenciaForm") IncidenciaForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            model.addAttribute("areasComunes", gestionAreasComunesService.obtenerTodasLasAreasComunes());
            return "dominios/incidencias/formulario-incidencia-area";
        }

        try {
            IncidenciaResponse response = gestionIncidenciasService.registrarIncidencia(form);

            if (form.getEvidenciaUrl() != null && !form.getEvidenciaUrl().isBlank()) {
                String[] enlaces = form.getEvidenciaUrl().split(",");
                for (String enlace : enlaces) {
                    if (!enlace.isBlank()) {
                        EvidenciaIncidenciaForm evidenciaForm = new EvidenciaIncidenciaForm();
                        evidenciaForm.setIncidenciaId(response.getId());
                        evidenciaForm.setUrlArchivo(enlace.trim());
                        gestionIncidenciasService.registrarEvidencia(evidenciaForm);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Incidencia de área reportada correctamente.");
            return "redirect:/incidencias";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            model.addAttribute("areasComunes", gestionAreasComunesService.obtenerTodasLasAreasComunes());
            return "dominios/incidencias/formulario-incidencia-area";
        }
    }

    @PostMapping("/unidad")
    public String registrarIncidenciaUnidad(
            @Valid @ModelAttribute("incidenciaForm") IncidenciaForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            model.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
            return "dominios/incidencias/formulario-incidencia-unidad";
        }

        try {
            IncidenciaResponse response = gestionIncidenciasService.registrarIncidencia(form);

            if (form.getEvidenciaUrl() != null && !form.getEvidenciaUrl().isBlank()) {
                String[] enlaces = form.getEvidenciaUrl().split(",");
                for (String enlace : enlaces) {
                    if (!enlace.isBlank()) {
                        EvidenciaIncidenciaForm evidenciaForm = new EvidenciaIncidenciaForm();
                        evidenciaForm.setIncidenciaId(response.getId());
                        evidenciaForm.setUrlArchivo(enlace.trim());
                        gestionIncidenciasService.registrarEvidencia(evidenciaForm);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Incidencia de unidad reportada correctamente.");
            return "redirect:/incidencias";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            model.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
            return "dominios/incidencias/formulario-incidencia-unidad";
        }
    }

    @GetMapping("/actualizar")
    public String mostrarFormularioActualizacion(@RequestParam("incidenciaId") Long incidenciaId, Model model) {
        ActualizacionIncidenciaForm form = new ActualizacionIncidenciaForm();
        form.setIncidenciaId(incidenciaId);

        Incidencia incidenciaExistente = gestionIncidenciasService.obtenerPorId(incidenciaId);
        if (incidenciaExistente != null) {
            form.setEstado(incidenciaExistente.getEstado());
            form.setResponsableAtencion(incidenciaExistente.getResponsableAtencion());
        }

        model.addAttribute("actualizacionForm", form);
        return "dominios/incidencias/formulario-actualizacion";
    }

    @PostMapping("/actualizar")
    public String actualizarEstado(
            @Valid @ModelAttribute("actualizacionForm") ActualizacionIncidenciaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "dominios/incidencias/formulario-actualizacion";
        }

        gestionIncidenciasService.actualizarEstado(form);
        redirectAttributes.addFlashAttribute("successMessage", "Estado de la incidencia actualizado.");
        return "redirect:/incidencias";
    }
}
