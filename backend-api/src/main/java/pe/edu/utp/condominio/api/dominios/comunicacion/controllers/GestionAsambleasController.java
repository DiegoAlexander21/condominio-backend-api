package pe.edu.utp.condominio.api.dominios.comunicacion.controllers;

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
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.AsambleaForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.VotoAsambleaForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.services.GestionAsambleasService;

@Controller
@RequestMapping("/comunicacion/asambleas")
public class GestionAsambleasController {

    private final GestionAsambleasService gestionAsambleasService;

    public GestionAsambleasController(GestionAsambleasService gestionAsambleasService) {
        this.gestionAsambleasService = gestionAsambleasService;
    }

    @GetMapping
    public String listarAsambleas(@RequestParam("condominioId") Long condominioId, Model modelo) {
        modelo.addAttribute("asambleas", gestionAsambleasService.listarPorCondominio(condominioId));
        return "comunicacion/lista-asambleas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("asambleaForm", new AsambleaForm());
        return "comunicacion/formulario-asamblea";
    }

    @PostMapping
    public String registrarAsamblea(
            @Valid @ModelAttribute("asambleaForm") AsambleaForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            return "comunicacion/formulario-asamblea";
        }

        gestionAsambleasService.registrarAsamblea(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Asamblea programada correctamente.");
        return "redirect:/comunicacion/asambleas?condominioId=" + formulario.getCondominioId();
    }

    @GetMapping("/votar")
    public String mostrarFormularioVotacion(@RequestParam("asambleaId") Long asambleaId, Model modelo) {
        VotoAsambleaForm formulario = new VotoAsambleaForm();
        formulario.setAsambleaId(asambleaId);
        modelo.addAttribute("votoForm", formulario);
        return "comunicacion/formulario-voto";
    }

    @PostMapping("/votos")
    public String registrarVoto(
            @Valid @ModelAttribute("votoForm") VotoAsambleaForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            return "comunicacion/formulario-voto";
        }

        gestionAsambleasService.registrarVoto(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Voto registrado correctamente.");
        return "redirect:/comunicacion/asambleas/resultados?asambleaId=" + formulario.getAsambleaId();
    }

    @GetMapping("/resultados")
    public String obtenerResultados(@RequestParam("asambleaId") Long asambleaId, Model modelo) {
        modelo.addAttribute("resultado", gestionAsambleasService.obtenerResultados(asambleaId));
        return "comunicacion/resultados-asamblea";
    }
}
