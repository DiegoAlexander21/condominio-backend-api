package pe.edu.utp.condominio.api.dominios.finanzas.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.DistribucionGastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.EstadoCuentaForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.AprobacionPagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.EvidenciaPagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.GastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.PagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.DetalleGastoUnidadResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.EstadoCuentaResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.GastoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.PagoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.EstadoPago;
import pe.edu.utp.condominio.api.dominios.finanzas.services.GestionFinanzasService;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Usuario;
import pe.edu.utp.condominio.api.dominios.seguridad.repositories.UsuarioRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.services.GestionUnidadesService;

@RestController
@RequestMapping("/api/finanzas")
public class FinanzasRestController {

    private final GestionFinanzasService gestionFinanzasService;
    private final GestionUnidadesService gestionUnidadesService;
    private final UsuarioRepository usuarioRepository;

    public FinanzasRestController(GestionFinanzasService gestionFinanzasService,
            GestionUnidadesService gestionUnidadesService,
            UsuarioRepository usuarioRepository) {
        this.gestionFinanzasService = gestionFinanzasService;
        this.gestionUnidadesService = gestionUnidadesService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/gastos")
    public ResponseEntity<Page<GastoResponse>> listarGastos(
            @RequestParam(value = "tipo", required = false) TipoGasto tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(gestionFinanzasService.listarGastosPorTipo(tipo, pageable));
    }

    @GetMapping("/gastos/{id}")
    public ResponseEntity<GastoForm> obtenerGasto(@PathVariable Long id) {
        return ResponseEntity.ok(gestionFinanzasService.obtenerGastoParaEdicion(id));
    }

    @PostMapping("/gastos")
    public ResponseEntity<?> registrarGasto(@Valid @RequestBody GastoForm formulario) {
        try {
            GastoResponse respuesta = gestionFinanzasService.registrarGasto(formulario);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno al registrar el gasto"));
        }
    }

    @PutMapping("/gastos/{id}")
    public ResponseEntity<?> actualizarGasto(@PathVariable Long id, @Valid @RequestBody GastoForm formulario) {
        try {
            GastoResponse respuesta = gestionFinanzasService.actualizarGasto(id, formulario);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno al actualizar el gasto"));
        }
    }

    @DeleteMapping("/gastos/{id}")
    public ResponseEntity<?> eliminarGasto(@PathVariable Long id) {
        try {
            gestionFinanzasService.eliminarGasto(id);
            return ResponseEntity.ok(Map.of("mensaje", "Gasto eliminado correctamente."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al eliminar el gasto."));
        }
    }

    @PostMapping("/gastos/distribuir")
    public ResponseEntity<?> distribuirGasto(@Valid @RequestBody DistribucionGastoForm formulario) {
        try {
            List<DetalleGastoUnidadResponse> respuesta = gestionFinanzasService.distribuirGasto(formulario);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno al distribuir el gasto."));
        }
    }

    @GetMapping("/gastos/{id}/detalles")
    public ResponseEntity<List<DetalleGastoUnidadResponse>> listarDetallesGasto(@PathVariable Long id) {
        return ResponseEntity.ok(gestionFinanzasService.listarDetallesPorGasto(id));
    }

    @GetMapping("/estados-cuenta")
    public ResponseEntity<List<EstadoCuentaResponse>> listarEstadosCuenta() {
        return ResponseEntity.ok(gestionFinanzasService.listarTodosEstadosCuenta());
    }

    @PostMapping("/estados-cuenta/generar")
    public ResponseEntity<?> generarEstadoCuenta(@Valid @RequestBody EstadoCuentaForm formulario) {
        try {
            EstadoCuentaResponse respuesta = gestionFinanzasService.generarEstadoCuenta(formulario);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al generar estado de cuenta."));
        }
    }

    @GetMapping("/estados-cuenta/{id}")
    public ResponseEntity<EstadoCuentaResponse> obtenerEstadoCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(gestionFinanzasService.obtenerEstadoCuentaResponse(id));
    }

    @GetMapping("/estados-cuenta/{id}/desglose")
    public ResponseEntity<List<DetalleGastoUnidadResponse>> verDesgloseEstadoCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(gestionFinanzasService.listarDesgloseEstadoCuenta(id));
    }

    @GetMapping("/estados-cuenta/{id}/pagos")
    public ResponseEntity<List<PagoResponse>> verDesglosePagosEstadoCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(gestionFinanzasService.listarPagosPorEstadoCuenta(id));
    }

    @DeleteMapping("/estados-cuenta/{id}")
    public ResponseEntity<?> eliminarEstadoCuenta(@PathVariable Long id) {
        try {
            gestionFinanzasService.eliminarEstadoCuenta(id);
            return ResponseEntity.ok(Map.of("mensaje", "Estado de cuenta eliminado."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al eliminar estado de cuenta."));
        }
    }

    @GetMapping("/estados-cuenta/mis-estados")
    @Transactional(readOnly = true)
    public ResponseEntity<?> verMiEstadoCuenta() {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.buscarPorIdentificador(nombreUsuario).orElse(null);
        if (usuario != null) {
            List<Unidad> unidades = gestionUnidadesService.buscarUnidadesPorDni(usuario.getNumeroDocumento());
            if (!unidades.isEmpty()) {
                Unidad miUnidad = unidades.get(0);

                java.util.Map<String, Object> condominioRes = new java.util.HashMap<>();
                condominioRes.put("id", miUnidad.getCondominio().getId());
                condominioRes.put("nombre", miUnidad.getCondominio().getNombre());

                java.util.Map<String, Object> unidadRes = new java.util.HashMap<>();
                unidadRes.put("id", miUnidad.getId());
                unidadRes.put("numeroUnidad", miUnidad.getNumeroUnidad());
                unidadRes.put("torre", miUnidad.getTorre());
                unidadRes.put("piso", miUnidad.getPiso());
                unidadRes.put("condominio", condominioRes);

                Map<String, Object> respuesta = Map.of(
                        "unidad", unidadRes,
                        "estadosCuenta", gestionFinanzasService.listarEstadosCuentaPorUnidad(miUnidad.getId()),
                        "pagos", gestionFinanzasService.listarPagosPorUnidad(miUnidad.getId()));
                return ResponseEntity.ok(respuesta);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "No se encontró unidad asociada al usuario."));
    }

    @GetMapping("/pagos/unidad/{unidadId}")
    public ResponseEntity<List<PagoResponse>> listarPagosPorUnidad(@PathVariable Long unidadId) {
        return ResponseEntity.ok(gestionFinanzasService.listarPagosPorUnidad(unidadId));
    }

    @PostMapping("/pagos")
    public ResponseEntity<?> registrarPago(@Valid @RequestBody PagoForm formulario) {
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

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno al registrar el pago"));
        }
    }

    @GetMapping("/pagos")
    public ResponseEntity<Page<PagoResponse>> listarPagos(
            @RequestParam(value = "estado", required = false) EstadoPago estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaPago"));
        return ResponseEntity.ok(gestionFinanzasService.listarPagos(estado, pageable));
    }

    @GetMapping("/pagos/pendientes")
    public ResponseEntity<List<PagoResponse>> listarPagosPendientes() {
        return ResponseEntity.ok(gestionFinanzasService.listarPagosPendientes());
    }

    @PutMapping("/pagos/{id}/aprobar")
    public ResponseEntity<?> aprobarPago(@PathVariable Long id, @Valid @RequestBody AprobacionPagoForm formulario) {
        try {
            PagoResponse respuesta = gestionFinanzasService.aprobarPago(id, formulario.getAprobar(),
                    formulario.getObservacionAdmin());
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al aprobar/rechazar el pago"));
        }
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos.");
        return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
    }
}
