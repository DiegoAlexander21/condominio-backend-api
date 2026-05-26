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

import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDate;
import java.util.List;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.response.ReservaAreaComunResponse;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.ReservaAreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.areascomunes.services.GestionAreasComunesService;
import pe.edu.utp.condominio.api.dominios.condominio.services.GestionCondominioService;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Controller
@RequestMapping("/reservas")
public class GestionReservasController {

    private final GestionAreasComunesService gestionAreasComunesService;
    private final GestionCondominioService gestionCondominioService;
    private final AreaComunRepository areaComunRepository;
    private final UnidadRepository unidadRepository;

    public GestionReservasController(GestionAreasComunesService gestionAreasComunesService,
            GestionCondominioService gestionCondominioService,
            AreaComunRepository areaComunRepository,
            UnidadRepository unidadRepository) {
        this.gestionAreasComunesService = gestionAreasComunesService;
        this.gestionCondominioService = gestionCondominioService;
        this.areaComunRepository = areaComunRepository;
        this.unidadRepository = unidadRepository;
    }

    @GetMapping
    public String listarReservas(
            @RequestParam(value = "condominioId", required = false) Long condominioId,
            @RequestParam(value = "areaComunId", required = false) Long areaComunId,
            Model model) {

        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        model.addAttribute("areasComunes", areaComunRepository.listarTodosConCondominio());
        model.addAttribute("unidades", unidadRepository.listarTodosConCondominioOrdenado());
        model.addAttribute("condominioIdSeleccionado", condominioId);
        model.addAttribute("areaComunIdSeleccionada", areaComunId);

        if (areaComunId != null) {
            model.addAttribute("reservas", gestionAreasComunesService.listarReservas(areaComunId, null));
        }
        return "dominios/areascomunes/gestion-reservas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioReserva(@RequestParam(value = "areaId", required = false) Long areaId, Model model) {
        ReservaAreaComunForm form = new ReservaAreaComunForm();
        if (areaId != null) {
            form.setAreaComunId(areaId);
        }
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        model.addAttribute("areasComunes", areaComunRepository.listarTodosConCondominio());
        model.addAttribute("unidades", unidadRepository.listarTodosConCondominioOrdenado());
        model.addAttribute("reservaForm", form);
        return "dominios/areascomunes/registro-reserva";
    }

    @PostMapping
    public String registrarReserva(
            @Valid @ModelAttribute("reservaForm") ReservaAreaComunForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            model.addAttribute("areasComunes", areaComunRepository.listarTodosConCondominio());
            model.addAttribute("unidades", unidadRepository.listarTodosConCondominioOrdenado());
            return "dominios/areascomunes/registro-reserva";
        }

        try {
            gestionAreasComunesService.registrarReserva(form);
            redirectAttributes.addFlashAttribute("successMessage", "Reserva realizada correctamente.");
            return "redirect:/reservas?areaComunId=" + form.getAreaComunId() + "&fecha=" + form.getFechaReserva() + "&condominioId=" + form.getCondominioId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            model.addAttribute("areasComunes", areaComunRepository.listarTodosConCondominio());
            model.addAttribute("unidades", unidadRepository.listarTodosConCondominioOrdenado());
            model.addAttribute("errorMessage", e.getMessage());
            return "dominios/areascomunes/registro-reserva";
        }
    }

    @GetMapping("/api/reservas")
    @ResponseBody
    public List<ReservaAreaComunResponse> obtenerReservasApi(
            @RequestParam("areaComunId") Long areaComunId,
            @RequestParam("fecha") LocalDate fecha) {
        return gestionAreasComunesService.listarReservas(areaComunId, fecha);
    }
}
