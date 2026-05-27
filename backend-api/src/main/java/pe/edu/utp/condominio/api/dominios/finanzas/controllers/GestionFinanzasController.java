package pe.edu.utp.condominio.api.dominios.finanzas.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
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
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.services.GestionCondominioService;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.DistribucionGastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.EstadoCuentaForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.EvidenciaPagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.GastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.PagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.PagoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;
import pe.edu.utp.condominio.api.dominios.finanzas.services.GestionFinanzasService;
import pe.edu.utp.condominio.api.dominios.incidencias.services.GestionIncidenciasService;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Usuario;
import pe.edu.utp.condominio.api.dominios.seguridad.repositories.UsuarioRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.services.GestionUnidadesService;

@Controller
@RequestMapping("/finanzas")
public class GestionFinanzasController {

    private final GestionFinanzasService gestionFinanzasService;
    private final GestionUnidadesService gestionUnidadesService;
    private final UsuarioRepository usuarioRepository;
    private final GestionIncidenciasService gestionIncidenciasService;
    private final GestionCondominioService gestionCondominioService;

    public GestionFinanzasController(GestionFinanzasService gestionFinanzasService,
            GestionUnidadesService gestionUnidadesService,
            UsuarioRepository usuarioRepository,
            GestionIncidenciasService gestionIncidenciasService,
            GestionCondominioService gestionCondominioService) {
        this.gestionFinanzasService = gestionFinanzasService;
        this.gestionUnidadesService = gestionUnidadesService;
        this.usuarioRepository = usuarioRepository;
        this.gestionIncidenciasService = gestionIncidenciasService;
        this.gestionCondominioService = gestionCondominioService;
    }

    @GetMapping("/gastos")
    public String listarGastos(@RequestParam(value = "tipo", required = false) TipoGasto tipo, Model model) {
        if (tipo != null) {
            model.addAttribute("gastos", gestionFinanzasService.listarGastosPorTipo(tipo));
        }
        model.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
        return "dominios/finanzas/gastos/lista-gastos";
    }

    @GetMapping("/gastos/nuevo")
    public String mostrarFormularioGasto(Model model) {
        model.addAttribute("gastoForm", new GastoForm());
        model.addAttribute("incidencias", gestionIncidenciasService.listarTodas());

        List<Condominio> condominios = gestionCondominioService.obtenerCondominios();
        Map<Long, List<String>> torresPorCondominio = new HashMap<>();
        for (Condominio c : condominios) {
            torresPorCondominio.put(c.getId(), gestionUnidadesService.listarTorresPorCondominio(c.getId()));
        }

        model.addAttribute("condominios", condominios);
        model.addAttribute("torresPorCondominio", torresPorCondominio);
        return "dominios/finanzas/gastos/formulario-gasto";
    }

    @GetMapping("/gastos/{id}/editar")
    public String mostrarFormularioEdicion(@org.springframework.web.bind.annotation.PathVariable("id") Long id,
            Model model) {
        try {
            GastoForm form = gestionFinanzasService.obtenerGastoParaEdicion(id);
            model.addAttribute("gastoForm", form);
            model.addAttribute("incidencias", gestionIncidenciasService.listarTodas());

            List<Condominio> condominios = gestionCondominioService.obtenerCondominios();
            Map<Long, List<String>> torresPorCondominio = new HashMap<>();
            for (Condominio c : condominios) {
                torresPorCondominio.put(c.getId(), gestionUnidadesService.listarTorresPorCondominio(c.getId()));
            }

            model.addAttribute("condominios", condominios);
            model.addAttribute("torresPorCondominio", torresPorCondominio);
            return "dominios/finanzas/gastos/formulario-gasto";
        } catch (IllegalArgumentException e) {
            return "redirect:/finanzas/gastos";
        }
    }

    @PostMapping("/gastos/eliminar")
    public String eliminarGasto(@RequestParam("id") Long id,
            @RequestParam(value = "tipoGasto", defaultValue = "FIJO") String tipoGasto,
            RedirectAttributes redirectAttributes) {
        try {
            gestionFinanzasService.eliminarGasto(id);
            redirectAttributes.addFlashAttribute("successMessage", "Gasto eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/finanzas/gastos?tipo=" + tipoGasto;
    }

    @PostMapping("/gastos")
    public String registrarGasto(
            @Valid @ModelAttribute("gastoForm") GastoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("incidencias", gestionIncidenciasService.listarTodas());

            List<Condominio> condominios = gestionCondominioService.obtenerCondominios();
            Map<Long, List<String>> torresPorCondominio = new HashMap<>();
            for (Condominio c : condominios) {
                torresPorCondominio.put(c.getId(), gestionUnidadesService.listarTorresPorCondominio(c.getId()));
            }

            model.addAttribute("condominios", condominios);
            model.addAttribute("torresPorCondominio", torresPorCondominio);
            return "dominios/finanzas/gastos/formulario-gasto";
        }

        try {
            if (form.getId() != null) {
                gestionFinanzasService.actualizarGasto(form.getId(), form);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Gasto actualizado correctamente. Por favor, vuelva a distribuirlo si corresponde.");
            } else {
                gestionFinanzasService.registrarGasto(form);
                redirectAttributes.addFlashAttribute("successMessage", "Gasto registrado correctamente.");
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("incidencias", gestionIncidenciasService.listarTodas());
            return "dominios/finanzas/gastos/formulario-gasto";
        }

        return "redirect:/finanzas/gastos?tipo=" + form.getTipoGasto();
    }

    @PostMapping("/estados-cuenta/eliminar")
    public String eliminarEstadoCuenta(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            gestionFinanzasService.eliminarEstadoCuenta(id);
            redirectAttributes.addFlashAttribute("successMessage", "Estado de cuenta eliminado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/finanzas/estados-cuenta";
    }

    @GetMapping("/pagos")
    public String listarPagos(@RequestParam("unidadId") Long unidadId, Model model) {
        model.addAttribute("pagos", gestionFinanzasService.listarPagosPorUnidad(unidadId));
        return "dominios/finanzas/pagos/lista-pagos";
    }

    @GetMapping("/api/pagos/unidad/{unidadId}")
    @ResponseBody
    public List<PagoResponse> listarPagosApi(@PathVariable("unidadId") Long unidadId) {
        return gestionFinanzasService.listarPagosPorUnidad(unidadId);
    }

    @GetMapping("/pagos/nuevo")
    public String mostrarFormularioPago(
            @RequestParam("unidadId") Long unidadId,
            @RequestParam(value = "estadoCuentaId", required = false) Long estadoCuentaId,
            @RequestParam(value = "saldo", required = false) Double saldo,
            Model model) {
        PagoForm form = new PagoForm();
        form.setUnidadId(unidadId);
        form.setEstadoCuentaId(estadoCuentaId);
        if (saldo != null && saldo > 0) {
            form.setMonto(saldo);
        }
        model.addAttribute("pagoForm", form);
        model.addAttribute("saldoPendiente", saldo);
        return "dominios/finanzas/pagos/formulario-pago";
    }

    @PostMapping("/pagos")
    public String registrarPago(
            @Valid @ModelAttribute("pagoForm") PagoForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            Double saldo = 0.0;
            if (form.getEstadoCuentaId() != null) {
                try {
                    var ec = gestionFinanzasService.obtenerEstadoCuentaResponse(form.getEstadoCuentaId());
                    saldo = ec.getSaldo();
                } catch (Exception ex) {
                }
            }
            model.addAttribute("saldoPendiente", saldo);
            return "dominios/finanzas/pagos/formulario-pago";
        }

        try {
            PagoResponse response = gestionFinanzasService.registrarPago(form);

            if (form.getEvidenciaUrl() != null && !form.getEvidenciaUrl().isBlank()) {
                String[] enlaces = form.getEvidenciaUrl().split(",");
                for (String enlace : enlaces) {
                    if (!enlace.isBlank()) {
                        EvidenciaPagoForm evidenciaForm = new EvidenciaPagoForm();
                        evidenciaForm.setPagoId(response.getId());
                        evidenciaForm.setUrlArchivo(enlace.trim());
                        gestionFinanzasService.registrarEvidenciaPago(evidenciaForm);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Pago registrado correctamente.");
            return "redirect:/finanzas/estados-cuenta";
        } catch (IllegalArgumentException e) {
            Double saldo = 0.0;
            if (form.getEstadoCuentaId() != null) {
                try {
                    var ec = gestionFinanzasService.obtenerEstadoCuentaResponse(form.getEstadoCuentaId());
                    saldo = ec.getSaldo();
                } catch (Exception ex) {
                    // Ignorar
                }
            }
            model.addAttribute("saldoPendiente", saldo);
            model.addAttribute("errorMessage", e.getMessage());
            return "dominios/finanzas/pagos/formulario-pago";
        }
    }

    @PostMapping("/gastos/distribuir")
    public String distribuirGasto(
            @Valid @ModelAttribute("distribucionForm") DistribucionGastoForm form,
            BindingResult bindingResult,
            @RequestParam(value = "tipoGastoRedirect", required = false, defaultValue = "FIJO") String tipoGastoRedirect,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Datos inválidos para distribución.");
            return "redirect:/finanzas/gastos?tipo=" + tipoGastoRedirect;
        }
        try {
            gestionFinanzasService.distribuirGasto(form);
            redirectAttributes.addFlashAttribute("successMessage", "Gasto distribuido exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/finanzas/gastos?tipo=" + tipoGastoRedirect;
    }

    @GetMapping("/estados-cuenta")
    public String listarEstadosCuenta(Model model) {
        model.addAttribute("estados", gestionFinanzasService.listarTodosEstadosCuenta());
        model.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
        model.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        model.addAttribute("estadoCuentaForm", new EstadoCuentaForm());
        return "dominios/finanzas/gastos/lista-estados-cuenta";
    }

    @GetMapping("/estados-cuenta/desglose")
    public String verDesglose(@RequestParam("id") Long id, Model model) {
        try {
            model.addAttribute("estadoCuenta", gestionFinanzasService.obtenerEstadoCuentaResponse(id));
            model.addAttribute("desglose", gestionFinanzasService.listarDesgloseEstadoCuenta(id));
            return "dominios/finanzas/gastos/desglose-estado-cuenta";
        } catch (IllegalArgumentException e) {
            return "redirect:/finanzas/estados-cuenta";
        }
    }

    @GetMapping("/estados-cuenta/desglose-pagos")
    public String verDesglosePagos(@RequestParam("id") Long id, Model model) {
        try {
            model.addAttribute("estadoCuenta", gestionFinanzasService.obtenerEstadoCuentaResponse(id));
            model.addAttribute("pagos", gestionFinanzasService.listarPagosPorEstadoCuenta(id));
            return "dominios/finanzas/pagos/desglose-pagos";
        } catch (IllegalArgumentException e) {
            return "redirect:/finanzas/estados-cuenta";
        }
    }

    @PostMapping("/estados-cuenta/generar")
    public String generarEstadoCuenta(
            @Valid @ModelAttribute("estadoCuentaForm") EstadoCuentaForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Datos de formulario inválidos.");
            return "redirect:/finanzas/estados-cuenta";
        }
        try {
            gestionFinanzasService.generarEstadoCuenta(form);
            redirectAttributes.addFlashAttribute("successMessage", "Estado de cuenta generado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/finanzas/estados-cuenta";
    }

    @GetMapping("/mi-estado-cuenta")
    public String verMiEstadoCuenta(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.buscarPorIdentificador(username).orElse(null);
        if (usuario != null) {
            List<Unidad> unidades = gestionUnidadesService.buscarUnidadesPorDni(usuario.getNumeroDocumento());
            if (!unidades.isEmpty()) {
                Unidad miUnidad = unidades.get(0);
                model.addAttribute("miUnidad", miUnidad);
                model.addAttribute("misEstados", gestionFinanzasService.listarEstadosCuentaPorUnidad(miUnidad.getId()));
                model.addAttribute("misPagos", gestionFinanzasService.listarPagosPorUnidad(miUnidad.getId()));
            }
        }
        return "dominios/finanzas/gastos/mi-estado-cuenta";
    }
}
