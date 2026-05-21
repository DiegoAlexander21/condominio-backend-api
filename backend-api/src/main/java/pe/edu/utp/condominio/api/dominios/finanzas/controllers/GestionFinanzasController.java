package pe.edu.utp.condominio.api.dominios.finanzas.controllers;

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
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.GastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.PagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;
import pe.edu.utp.condominio.api.dominios.finanzas.services.GestionFinanzasService;

@Controller
@RequestMapping("/finanzas")
public class GestionFinanzasController {

    private final GestionFinanzasService gestionFinanzasService;

    public GestionFinanzasController(GestionFinanzasService gestionFinanzasService) {
        this.gestionFinanzasService = gestionFinanzasService;
    }

    @GetMapping("/gastos")
    public String listarGastos(@RequestParam(value = "tipo", required = false) TipoGasto tipo, Model model) {
        if (tipo != null) {
            model.addAttribute("gastos", gestionFinanzasService.listarGastosPorTipo(tipo));
        }
        return "finanzas/lista-gastos";
    }

    @GetMapping("/gastos/nuevo")
    public String mostrarFormularioGasto(Model model) {
        model.addAttribute("gastoForm", new GastoForm());
        return "finanzas/formulario-gasto";
    }

    @PostMapping("/gastos")
    public String registrarGasto(
            @Valid @ModelAttribute("gastoForm") GastoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "finanzas/formulario-gasto";
        }

        gestionFinanzasService.registrarGasto(form);
        redirectAttributes.addFlashAttribute("successMessage", "Gasto registrado correctamente.");
        return "redirect:/finanzas/gastos?tipo=" + form.getTipoGasto();
    }

    @GetMapping("/pagos")
    public String listarPagos(@RequestParam("unidadId") Long unidadId, Model model) {
        model.addAttribute("pagos", gestionFinanzasService.listarPagosPorUnidad(unidadId));
        return "finanzas/lista-pagos";
    }

    @GetMapping("/pagos/nuevo")
    public String mostrarFormularioPago(@RequestParam("unidadId") Long unidadId, Model model) {
        PagoForm form = new PagoForm();
        form.setUnidadId(unidadId);
        model.addAttribute("pagoForm", form);
        return "finanzas/formulario-pago";
    }

    @PostMapping("/pagos")
    public String registrarPago(
            @Valid @ModelAttribute("pagoForm") PagoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "finanzas/formulario-pago";
        }

        gestionFinanzasService.registrarPago(form);
        redirectAttributes.addFlashAttribute("successMessage", "Pago registrado correctamente.");
        return "redirect:/finanzas/pagos?unidadId=" + form.getUnidadId();
    }
}
