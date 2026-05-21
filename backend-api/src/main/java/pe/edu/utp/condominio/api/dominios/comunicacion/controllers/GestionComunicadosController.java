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
    public String listarComunicados(@RequestParam("condominioId") Long condominioId, Model model) {
        model.addAttribute("comunicados", gestionComunicadosService.listarPorCondominio(condominioId));
        return "comunicacion/lista-comunicados";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        if (!model.containsAttribute("comunicadoForm")) {
            model.addAttribute("comunicadoForm", new ComunicadoForm());
        }
        return "comunicacion/formulario-comunicado";
    }

    @PostMapping
    public String registrarComunicado(
            @Valid @ModelAttribute("comunicadoForm") ComunicadoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "comunicacion/formulario-comunicado";
        }

        gestionComunicadosService.registrarComunicado(form);
        redirectAttributes.addFlashAttribute("successMessage", "Comunicado registrado correctamente.");
        return "redirect:/comunicacion/comunicados?condominioId=" + form.getCondominioId();
    }

    @PostMapping("/ia")
    public String generarConIA(
            @Valid @ModelAttribute("comunicadoIAForm") ComunicadoIAForm form,
            BindingResult bindingResult,
            Model model) {
        
        if (bindingResult.hasErrors()) {
            return "comunicacion/formulario-comunicado";
        }

        model.addAttribute("comunicadoGenerado", gestionComunicadosService.generarConIA(form));
        return "comunicacion/formulario-comunicado";
    }
}
