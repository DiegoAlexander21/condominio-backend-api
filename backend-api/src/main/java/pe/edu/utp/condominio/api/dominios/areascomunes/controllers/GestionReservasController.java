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
            Model modelo) {

        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        modelo.addAttribute("areasComunes", areaComunRepository.listarTodosConCondominio());
        modelo.addAttribute("unidades", unidadRepository.listarTodosConCondominioOrdenado());
        modelo.addAttribute("condominioIdSeleccionado", condominioId);
        modelo.addAttribute("areaComunIdSeleccionada", areaComunId);

        if (areaComunId != null) {
            modelo.addAttribute("reservas", gestionAreasComunesService.listarReservas(areaComunId, null));
        }
        return "dominios/areascomunes/gestion-reservas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioReserva(@RequestParam(value = "areaId", required = false) Long idArea,
            Model modelo) {
        ReservaAreaComunForm formulario = new ReservaAreaComunForm();
        if (idArea != null) {
            formulario.setAreaComunId(idArea);
        }
        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        modelo.addAttribute("areasComunes", areaComunRepository.listarTodosConCondominio());
        modelo.addAttribute("unidades", unidadRepository.listarTodosConCondominioOrdenado());
        modelo.addAttribute("formularioReserva", formulario);
        return "dominios/areascomunes/registro-reserva";
    }

    @PostMapping
    public String registrarReserva(
            @Valid @ModelAttribute("formularioReserva") ReservaAreaComunForm formulario,
            BindingResult resultadoValidacion,
            Model modelo,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            modelo.addAttribute("areasComunes", areaComunRepository.listarTodosConCondominio());
            modelo.addAttribute("unidades", unidadRepository.listarTodosConCondominioOrdenado());
            return "dominios/areascomunes/registro-reserva";
        }

        try {
            gestionAreasComunesService.registrarReserva(formulario);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Reserva realizada correctamente.");
            return "redirect:/reservas?areaComunId=" + formulario.getAreaComunId() + "&fecha="
                    + formulario.getFechaReserva() + "&condominioId=" + formulario.getCondominioId();
        } catch (IllegalArgumentException excepcion) {
            modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
            modelo.addAttribute("areasComunes", areaComunRepository.listarTodosConCondominio());
            modelo.addAttribute("unidades", unidadRepository.listarTodosConCondominioOrdenado());
            modelo.addAttribute("mensajeError", excepcion.getMessage());
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
