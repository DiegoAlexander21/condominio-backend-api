package pe.edu.utp.condominio.api.dominios.condominio.controllers;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.condominio.dto.request.CondominioForm;
import pe.edu.utp.condominio.api.dominios.condominio.services.GestionCondominioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GestionCondominioController {

    private final GestionCondominioService gestionCondominioService;

    public GestionCondominioController(GestionCondominioService gestionCondominioService) {
        this.gestionCondominioService = gestionCondominioService;
    }

    @GetMapping("/")
    public String redireccionarRaiz() {
        return "redirect:/reportes/dashboard";
    }

    @GetMapping("/gestion-condominio")
    public String mostrarGestionCondominio(Model modelo) {
        cargarModeloGestion(modelo);
        return "dominios/condominios/gestion-condominios";
    }

    @GetMapping("/registro-condominio")
    public String mostrarRegistroCondominio(Model modelo) {
        if (!modelo.containsAttribute("condominioForm")) {
            modelo.addAttribute("condominioForm", new CondominioForm());
        }
        modelo.addAttribute("esEdicion", false);
        return "dominios/condominios/registro-condominio";
    }

    @GetMapping("/editar-condominio")
    public String editarCondominio(Long id, Model modelo, RedirectAttributes atributosRedireccion) {
        CondominioForm formulario = gestionCondominioService.obtenerFormCondominio(id);
        if (formulario == null) {
            atributosRedireccion.addFlashAttribute("mensajeError", "Condominio no encontrado.");
            return "redirect:/gestion-condominio";
        }
        modelo.addAttribute("condominioForm", formulario);
        modelo.addAttribute("esEdicion", true);
        return "dominios/condominios/registro-condominio";
    }

    @GetMapping("/eliminar-condominio")
    public String eliminarCondominio(Long id, RedirectAttributes atributosRedireccion) {
        try {
            gestionCondominioService.eliminarCondominio(id);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Condominio eliminado correctamente.");
        } catch (Exception e) {
            atributosRedireccion.addFlashAttribute("mensajeError", "Error al eliminar el condominio.");
        }
        return "redirect:/gestion-condominio";
    }

    @PostMapping("/gestion-condominio/condominio")
    public String registrarCondominio(
            @Valid @ModelAttribute CondominioForm condominioForm,
            BindingResult resultadoValidacion,
            Model modelo,
            RedirectAttributes atributosRedireccion) {
        if (resultadoValidacion.hasErrors()) {
            modelo.addAttribute("mensajeError", "Revisa los campos del formulario.");
            return "dominios/condominios/registro-condominio";
        }

        try {
            gestionCondominioService.registrarOActualizarCondominio(condominioForm);
            atributosRedireccion.addFlashAttribute("mensajeExito",
                    "Condominio registrado o actualizado correctamente.");
            return "redirect:/gestion-condominio";
        } catch (IllegalArgumentException ex) {
            modelo.addAttribute("mensajeError", ex.getMessage());
            return "dominios/condominios/registro-condominio";
        }
    }

    private void cargarModeloGestion(Model modelo) {
        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        modelo.addAttribute("totalTorres", gestionCondominioService.obtenerTotalTorres());
        modelo.addAttribute("totalPisos", gestionCondominioService.obtenerTotalPisos());
    }
}
