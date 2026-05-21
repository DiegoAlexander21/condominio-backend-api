package pe.edu.utp.condominio.api.dominios.areascomunes.controllers;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
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
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.ReservaAreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.services.GestionAreasComunesService;

@Controller
@RequestMapping("/areas-comunes")
public class GestionAreasComunesController {

    private final GestionAreasComunesService gestionAreasComunesService;

    public GestionAreasComunesController(GestionAreasComunesService gestionAreasComunesService) {
        this.gestionAreasComunesService = gestionAreasComunesService;
    }

    @GetMapping
    public String listarAreas(@RequestParam("condominioId") Long condominioId, Model model) {
        model.addAttribute("areas", gestionAreasComunesService.listarPorCondominio(condominioId));
        return "areas-comunes/lista-areas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("areaForm", new AreaComunForm());
        return "areas-comunes/formulario-area";
    }

    @PostMapping
    public String registrarArea(
            @Valid @ModelAttribute("areaForm") AreaComunForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "areas-comunes/formulario-area";
        }

        gestionAreasComunesService.registrarOActualizarArea(form);
        redirectAttributes.addFlashAttribute("successMessage", "Área común registrada correctamente.");
        return "redirect:/areas-comunes?condominioId=" + form.getCondominioId();
    }

    @GetMapping("/reservas")
    public String listarReservas(
            @RequestParam("areaComunId") Long areaComunId,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {
        model.addAttribute("reservas", gestionAreasComunesService.listarReservas(areaComunId, fecha));
        return "areas-comunes/lista-reservas";
    }

    @GetMapping("/reservas/nuevo")
    public String mostrarFormularioReserva(@RequestParam("areaId") Long areaId, Model model) {
        ReservaAreaComunForm form = new ReservaAreaComunForm();
        form.setAreaComunId(areaId);
        model.addAttribute("reservaForm", form);
        return "areas-comunes/formulario-reserva";
    }

    @PostMapping("/reservas")
    public String registrarReserva(
            @Valid @ModelAttribute("reservaForm") ReservaAreaComunForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "areas-comunes/formulario-reserva";
        }

        gestionAreasComunesService.registrarReserva(form);
        redirectAttributes.addFlashAttribute("successMessage", "Reserva realizada correctamente.");
        return "redirect:/areas-comunes/reservas?areaComunId=" + form.getAreaComunId() + "&fecha=" + form.getFechaReserva();
    }
}
