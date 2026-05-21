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
    public String listarAsambleas(@RequestParam("condominioId") Long condominioId, Model model) {
        model.addAttribute("asambleas", gestionAsambleasService.listarPorCondominio(condominioId));
        return "comunicacion/lista-asambleas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("asambleaForm", new AsambleaForm());
        return "comunicacion/formulario-asamblea";
    }

    @PostMapping
    public String registrarAsamblea(
            @Valid @ModelAttribute("asambleaForm") AsambleaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "comunicacion/formulario-asamblea";
        }

        gestionAsambleasService.registrarAsamblea(form);
        redirectAttributes.addFlashAttribute("successMessage", "Asamblea programada correctamente.");
        return "redirect:/comunicacion/asambleas?condominioId=" + form.getCondominioId();
    }

    @GetMapping("/votar")
    public String mostrarFormularioVotacion(@RequestParam("asambleaId") Long asambleaId, Model model) {
        VotoAsambleaForm form = new VotoAsambleaForm();
        form.setAsambleaId(asambleaId);
        model.addAttribute("votoForm", form);
        return "comunicacion/formulario-voto";
    }

    @PostMapping("/votos")
    public String registrarVoto(
            @Valid @ModelAttribute("votoForm") VotoAsambleaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "comunicacion/formulario-voto";
        }

        gestionAsambleasService.registrarVoto(form);
        redirectAttributes.addFlashAttribute("successMessage", "Voto registrado correctamente.");
        return "redirect:/comunicacion/asambleas/resultados?asambleaId=" + form.getAsambleaId();
    }

    @GetMapping("/resultados")
    public String obtenerResultados(@RequestParam("asambleaId") Long asambleaId, Model model) {
        model.addAttribute("resultado", gestionAsambleasService.obtenerResultados(asambleaId));
        return "comunicacion/resultados-asamblea";
    }
}
