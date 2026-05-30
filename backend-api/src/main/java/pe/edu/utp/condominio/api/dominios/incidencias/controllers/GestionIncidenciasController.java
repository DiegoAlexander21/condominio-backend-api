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
            Model modelo) {
        if (estado == null) {
            return "redirect:/incidencias?estado=REGISTRADO";
        }
        modelo.addAttribute("incidencias", gestionIncidenciasService.listarPorEstado(estado));
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
    public String mostrarFormularioRegistroArea(Model modelo) {
        modelo.addAttribute("incidenciaForm", new IncidenciaForm());
        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        modelo.addAttribute("areasComunes", gestionAreasComunesService.obtenerTodasLasAreasComunes());
        return "dominios/incidencias/formulario-incidencia-area";
    }

    @GetMapping("/nuevo/unidad")
    public String mostrarFormularioRegistroUnidad(Model modelo) {
        modelo.addAttribute("incidenciaForm", new IncidenciaForm());
        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        modelo.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
        return "dominios/incidencias/formulario-incidencia-unidad";
    }

    @PostMapping("/area")
    public String registrarIncidenciaArea(
            @Valid @ModelAttribute("incidenciaForm") IncidenciaForm formulario,
            BindingResult resultadoValidacion,
            Model modelo,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            modelo.addAttribute("areasComunes", gestionAreasComunesService.obtenerTodasLasAreasComunes());
            return "dominios/incidencias/formulario-incidencia-area";
        }

        try {
            IncidenciaResponse respuesta = gestionIncidenciasService.registrarIncidencia(formulario);

            if (formulario.getEvidenciaUrl() != null && !formulario.getEvidenciaUrl().isBlank()) {
                String[] enlaces = formulario.getEvidenciaUrl().split(",");
                for (String enlace : enlaces) {
                    if (!enlace.isBlank()) {
                        EvidenciaIncidenciaForm formularioEvidencia = new EvidenciaIncidenciaForm();
                        formularioEvidencia.setIncidenciaId(respuesta.getId());
                        formularioEvidencia.setUrlArchivo(enlace.trim());
                        gestionIncidenciasService.registrarEvidencia(formularioEvidencia);
                    }
                }
            }

            atributosRedireccion.addFlashAttribute("mensajeExito", "Incidencia de área reportada correctamente.");
            return "redirect:/incidencias";
        } catch (IllegalArgumentException ex) {
            modelo.addAttribute("mensajeError", ex.getMessage());
            modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            modelo.addAttribute("areasComunes", gestionAreasComunesService.obtenerTodasLasAreasComunes());
            return "dominios/incidencias/formulario-incidencia-area";
        }
    }

    @PostMapping("/unidad")
    public String registrarIncidenciaUnidad(
            @Valid @ModelAttribute("incidenciaForm") IncidenciaForm formulario,
            BindingResult resultadoValidacion,
            Model modelo,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            modelo.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
            return "dominios/incidencias/formulario-incidencia-unidad";
        }

        try {
            IncidenciaResponse respuesta = gestionIncidenciasService.registrarIncidencia(formulario);

            if (formulario.getEvidenciaUrl() != null && !formulario.getEvidenciaUrl().isBlank()) {
                String[] enlaces = formulario.getEvidenciaUrl().split(",");
                for (String enlace : enlaces) {
                    if (!enlace.isBlank()) {
                        EvidenciaIncidenciaForm formularioEvidencia = new EvidenciaIncidenciaForm();
                        formularioEvidencia.setIncidenciaId(respuesta.getId());
                        formularioEvidencia.setUrlArchivo(enlace.trim());
                        gestionIncidenciasService.registrarEvidencia(formularioEvidencia);
                    }
                }
            }

            atributosRedireccion.addFlashAttribute("mensajeExito", "Incidencia de unidad reportada correctamente.");
            return "redirect:/incidencias";
        } catch (IllegalArgumentException ex) {
            modelo.addAttribute("mensajeError", ex.getMessage());
            modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            modelo.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
            return "dominios/incidencias/formulario-incidencia-unidad";
        }
    }

    @GetMapping("/actualizar")
    public String mostrarFormularioActualizacion(@RequestParam("incidenciaId") Long incidenciaId, Model modelo) {
        ActualizacionIncidenciaForm formulario = new ActualizacionIncidenciaForm();
        formulario.setIncidenciaId(incidenciaId);

        Incidencia incidenciaExistente = gestionIncidenciasService.obtenerPorId(incidenciaId);
        if (incidenciaExistente != null) {
            formulario.setEstado(incidenciaExistente.getEstado());
            formulario.setResponsableAtencion(incidenciaExistente.getResponsableAtencion());
        }

        modelo.addAttribute("actualizacionForm", formulario);
        return "dominios/incidencias/formulario-actualizacion";
    }

    @PostMapping("/actualizar")
    public String actualizarEstado(
            @Valid @ModelAttribute("actualizacionForm") ActualizacionIncidenciaForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            return "dominios/incidencias/formulario-actualizacion";
        }

        gestionIncidenciasService.actualizarEstado(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Estado de la incidencia actualizado.");
        return "redirect:/incidencias";
    }
}
