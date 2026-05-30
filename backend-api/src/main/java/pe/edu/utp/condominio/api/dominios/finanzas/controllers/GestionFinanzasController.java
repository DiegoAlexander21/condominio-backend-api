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
    public String listarGastos(@RequestParam(value = "tipo", required = false) TipoGasto tipo, Model modelo) {
        if (tipo != null) {
            modelo.addAttribute("gastos", gestionFinanzasService.listarGastosPorTipo(tipo));
        }
        modelo.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
        return "dominios/finanzas/gastos/lista-gastos";
    }

    @GetMapping("/gastos/nuevo")
    public String mostrarFormularioGasto(Model modelo) {
        modelo.addAttribute("gastoForm", new GastoForm());
        modelo.addAttribute("incidencias", gestionIncidenciasService.listarTodas());

        List<Condominio> condominios = gestionCondominioService.obtenerCondominios();
        Map<Long, List<String>> torresPorCondominio = new HashMap<>();
        for (Condominio c : condominios) {
            torresPorCondominio.put(c.getId(), gestionUnidadesService.listarTorresPorCondominio(c.getId()));
        }

        modelo.addAttribute("condominios", condominios);
        modelo.addAttribute("torresPorCondominio", torresPorCondominio);
        return "dominios/finanzas/gastos/formulario-gasto";
    }

    @GetMapping("/gastos/{id}/editar")
    public String mostrarFormularioEdicion(@org.springframework.web.bind.annotation.PathVariable("id") Long id,
            Model modelo) {
        try {
            GastoForm formulario = gestionFinanzasService.obtenerGastoParaEdicion(id);
            modelo.addAttribute("gastoForm", formulario);
            modelo.addAttribute("incidencias", gestionIncidenciasService.listarTodas());

            List<Condominio> condominios = gestionCondominioService.obtenerCondominios();
            Map<Long, List<String>> torresPorCondominio = new HashMap<>();
            for (Condominio c : condominios) {
                torresPorCondominio.put(c.getId(), gestionUnidadesService.listarTorresPorCondominio(c.getId()));
            }

            modelo.addAttribute("condominios", condominios);
            modelo.addAttribute("torresPorCondominio", torresPorCondominio);
            return "dominios/finanzas/gastos/formulario-gasto";
        } catch (IllegalArgumentException e) {
            return "redirect:/finanzas/gastos";
        }
    }

    @PostMapping("/gastos/eliminar")
    public String eliminarGasto(@RequestParam("id") Long id,
            @RequestParam(value = "tipoGasto", defaultValue = "FIJO") String tipoGasto,
            RedirectAttributes atributosRedireccion) {
        try {
            gestionFinanzasService.eliminarGasto(id);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Gasto eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            atributosRedireccion.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/finanzas/gastos?tipo=" + tipoGasto;
    }

    @PostMapping("/gastos")
    public String registrarGasto(
            @Valid @ModelAttribute("gastoForm") GastoForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion,
            Model modelo) {

        if (resultadoValidacion.hasErrors()) {
            modelo.addAttribute("incidencias", gestionIncidenciasService.listarTodas());

            List<Condominio> condominios = gestionCondominioService.obtenerCondominios();
            Map<Long, List<String>> torresPorCondominio = new HashMap<>();
            for (Condominio c : condominios) {
                torresPorCondominio.put(c.getId(), gestionUnidadesService.listarTorresPorCondominio(c.getId()));
            }

            modelo.addAttribute("condominios", condominios);
            modelo.addAttribute("torresPorCondominio", torresPorCondominio);
            return "dominios/finanzas/gastos/formulario-gasto";
        }

        try {
            if (formulario.getId() != null) {
                gestionFinanzasService.actualizarGasto(formulario.getId(), formulario);
                atributosRedireccion.addFlashAttribute("mensajeExito",
                        "Gasto actualizado correctamente. Por favor, vuelva a distribuirlo si corresponde.");
            } else {
                gestionFinanzasService.registrarGasto(formulario);
                atributosRedireccion.addFlashAttribute("mensajeExito", "Gasto registrado correctamente.");
            }
        } catch (IllegalArgumentException e) {
            modelo.addAttribute("mensajeError", e.getMessage());
            modelo.addAttribute("incidencias", gestionIncidenciasService.listarTodas());
            return "dominios/finanzas/gastos/formulario-gasto";
        }

        return "redirect:/finanzas/gastos?tipo=" + formulario.getTipoGasto();
    }

    @PostMapping("/estados-cuenta/eliminar")
    public String eliminarEstadoCuenta(@RequestParam("id") Long id, RedirectAttributes atributosRedireccion) {
        try {
            gestionFinanzasService.eliminarEstadoCuenta(id);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Estado de cuenta eliminado.");
        } catch (Exception e) {
            atributosRedireccion.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/finanzas/estados-cuenta";
    }

    @GetMapping("/pagos")
    public String listarPagos(@RequestParam("unidadId") Long unidadId, Model modelo) {
        modelo.addAttribute("pagos", gestionFinanzasService.listarPagosPorUnidad(unidadId));
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
            Model modelo) {
        PagoForm formulario = new PagoForm();
        formulario.setUnidadId(unidadId);
        formulario.setEstadoCuentaId(estadoCuentaId);
        if (saldo != null && saldo > 0) {
            formulario.setMonto(saldo);
        }
        modelo.addAttribute("pagoForm", formulario);
        modelo.addAttribute("saldoPendiente", saldo);
        return "dominios/finanzas/pagos/formulario-pago";
    }

    @PostMapping("/pagos")
    public String registrarPago(
            @Valid @ModelAttribute("pagoForm") PagoForm formulario,
            BindingResult resultadoValidacion,
            Model modelo,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            Double saldo = 0.0;
            if (formulario.getEstadoCuentaId() != null) {
                try {
                    var ec = gestionFinanzasService.obtenerEstadoCuentaResponse(formulario.getEstadoCuentaId());
                    saldo = ec.getSaldo();
                } catch (Exception ex) {
                }
            }
            modelo.addAttribute("saldoPendiente", saldo);
            return "dominios/finanzas/pagos/formulario-pago";
        }

        try {
            PagoResponse respuesta = gestionFinanzasService.registrarPago(formulario);

            if (formulario.getEvidenciaUrl() != null && !formulario.getEvidenciaUrl().isBlank()) {
                String[] enlaces = formulario.getEvidenciaUrl().split(",");
                for (String enlace : enlaces) {
                    if (!enlace.isBlank()) {
                        EvidenciaPagoForm formularioEvidencia = new EvidenciaPagoForm();
                        formularioEvidencia.setPagoId(respuesta.getId());
                        formularioEvidencia.setUrlArchivo(enlace.trim());
                        gestionFinanzasService.registrarEvidenciaPago(formularioEvidencia);
                    }
                }
            }

            atributosRedireccion.addFlashAttribute("mensajeExito", "Pago registrado correctamente.");
            return "redirect:/finanzas/estados-cuenta";
        } catch (IllegalArgumentException e) {
            Double saldo = 0.0;
            if (formulario.getEstadoCuentaId() != null) {
                try {
                    var ec = gestionFinanzasService.obtenerEstadoCuentaResponse(formulario.getEstadoCuentaId());
                    saldo = ec.getSaldo();
                } catch (Exception ex) {
                }
            }
            modelo.addAttribute("saldoPendiente", saldo);
            modelo.addAttribute("mensajeError", e.getMessage());
            return "dominios/finanzas/pagos/formulario-pago";
        }
    }

    @PostMapping("/gastos/distribuir")
    public String distribuirGasto(
            @Valid @ModelAttribute("distribucionForm") DistribucionGastoForm formulario,
            BindingResult resultadoValidacion,
            @RequestParam(value = "tipoGastoRedirect", required = false, defaultValue = "FIJO") String tipoGastoRedirect,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            atributosRedireccion.addFlashAttribute("mensajeError", "Datos inválidos para distribución.");
            return "redirect:/finanzas/gastos?tipo=" + tipoGastoRedirect;
        }
        try {
            gestionFinanzasService.distribuirGasto(formulario);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Gasto distribuido exitosamente.");
        } catch (IllegalArgumentException e) {
            atributosRedireccion.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/finanzas/gastos?tipo=" + tipoGastoRedirect;
    }

    @GetMapping("/estados-cuenta")
    public String listarEstadosCuenta(Model modelo) {
        modelo.addAttribute("estados", gestionFinanzasService.listarTodosEstadosCuenta());
        modelo.addAttribute("unidades", gestionUnidadesService.obtenerUnidades());
        modelo.addAttribute("condominios", gestionCondominioService.obtenerCondominios());
        modelo.addAttribute("estadoCuentaForm", new EstadoCuentaForm());
        return "dominios/finanzas/gastos/lista-estados-cuenta";
    }

    @GetMapping("/estados-cuenta/desglose")
    public String verDesglose(@RequestParam("id") Long id, Model modelo) {
        try {
            modelo.addAttribute("estadoCuenta", gestionFinanzasService.obtenerEstadoCuentaResponse(id));
            modelo.addAttribute("desglose", gestionFinanzasService.listarDesgloseEstadoCuenta(id));
            return "dominios/finanzas/gastos/desglose-estado-cuenta";
        } catch (IllegalArgumentException e) {
            return "redirect:/finanzas/estados-cuenta";
        }
    }

    @GetMapping("/estados-cuenta/desglose-pagos")
    public String verDesglosePagos(@RequestParam("id") Long id, Model modelo) {
        try {
            modelo.addAttribute("estadoCuenta", gestionFinanzasService.obtenerEstadoCuentaResponse(id));
            modelo.addAttribute("pagos", gestionFinanzasService.listarPagosPorEstadoCuenta(id));
            return "dominios/finanzas/pagos/desglose-pagos";
        } catch (IllegalArgumentException e) {
            return "redirect:/finanzas/estados-cuenta";
        }
    }

    @PostMapping("/estados-cuenta/generar")
    public String generarEstadoCuenta(
            @Valid @ModelAttribute("estadoCuentaForm") EstadoCuentaForm formulario,
            BindingResult resultadoValidacion,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            atributosRedireccion.addFlashAttribute("mensajeError", "Datos de formulario inválidos.");
            return "redirect:/finanzas/estados-cuenta";
        }
        try {
            gestionFinanzasService.generarEstadoCuenta(formulario);
            atributosRedireccion.addFlashAttribute("mensajeExito", "Estado de cuenta generado correctamente.");
        } catch (IllegalArgumentException e) {
            atributosRedireccion.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/finanzas/estados-cuenta";
    }

    @GetMapping("/mi-estado-cuenta")
    public String verMiEstadoCuenta(Model modelo) {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.buscarPorIdentificador(nombreUsuario).orElse(null);
        if (usuario != null) {
            List<Unidad> unidades = gestionUnidadesService.buscarUnidadesPorDni(usuario.getNumeroDocumento());
            if (!unidades.isEmpty()) {
                Unidad miUnidad = unidades.get(0);
                modelo.addAttribute("miUnidad", miUnidad);
                modelo.addAttribute("misEstados", gestionFinanzasService.listarEstadosCuentaPorUnidad(miUnidad.getId()));
                modelo.addAttribute("misPagos", gestionFinanzasService.listarPagosPorUnidad(miUnidad.getId()));
            }
        }
        return "dominios/finanzas/gastos/mi-estado-cuenta";
    }
}
