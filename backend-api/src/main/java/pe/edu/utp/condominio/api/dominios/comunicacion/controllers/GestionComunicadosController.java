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
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.ComunicadoForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.ComunicadoIAForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.services.GestionComunicadosService;

@Controller
@RequestMapping("/comunicacion/comunicados")
public class GestionComunicadosController {

    private final GestionComunicadosService gestionComunicadosService;

    public GestionComunicadosController(GestionComunicadosService gestionComunicadosService) {
        this.gestionComunicadosService = gestionComunicadosService;
    }

    @GetMapping
    public String listarComunicados(@RequestParam("condominioId") Long condominioId, Model modelo) {
        modelo.addAttribute("comunicados", gestionComunicadosService.listarPorCondominio(condominioId));
        return "comunicacion/lista-comunicados";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model modelo) {
        if (!modelo.containsAttribute("comunicadoForm")) {
            modelo.addAttribute("comunicadoForm", new ComunicadoForm());
        }
        return "comunicacion/formulario-comunicado";
    }

    @PostMapping
    public String registrarComunicado(
            @Valid @ModelAttribute("comunicadoForm") ComunicadoForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {
        
        if (resultadoValidacion.hasErrors()) {
            return "comunicacion/formulario-comunicado";
        }

        gestionComunicadosService.registrarComunicado(formulario);
        atributosRedireccion.addFlashAttribute("mensajeExito", "Comunicado registrado correctamente.");
        return "redirect:/comunicacion/comunicados?condominioId=" + formulario.getCondominioId();
    }

    @PostMapping("/ia")
    public String generarConIA(
            @Valid @ModelAttribute("comunicadoIAForm") ComunicadoIAForm formulario,
            BindingResult resultadoValidacion,
            Model modelo) {
        
        if (resultadoValidacion.hasErrors()) {
            return "comunicacion/formulario-comunicado";
        }

        modelo.addAttribute("comunicadoGenerado", gestionComunicadosService.generarConIA(formulario));
        return "comunicacion/formulario-comunicado";
    }
}
