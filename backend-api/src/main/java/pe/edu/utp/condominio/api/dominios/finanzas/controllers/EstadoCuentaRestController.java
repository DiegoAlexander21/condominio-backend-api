package pe.edu.utp.condominio.api.dominios.finanzas.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.EstadoCuentaForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.DetalleGastoUnidadResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.EstadoCuentaResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.PagoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.services.EstadoCuentaService;
import pe.edu.utp.condominio.api.dominios.finanzas.services.GeneradorEstadoCuentaService;
import pe.edu.utp.condominio.api.dominios.finanzas.services.PagoService;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Usuario;
import pe.edu.utp.condominio.api.dominios.seguridad.repositories.UsuarioRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.services.OcupanteService;

@RestController
@RequestMapping("/api/finanzas/estados-cuenta")
public class EstadoCuentaRestController {

    private final EstadoCuentaService estadoCuentaService;
    private final GeneradorEstadoCuentaService generadorEstadoCuentaService;
    private final PagoService pagoService;
    private final OcupanteService ocupanteService;
    private final UsuarioRepository usuarioRepository;

    public EstadoCuentaRestController(EstadoCuentaService estadoCuentaService,
            GeneradorEstadoCuentaService generadorEstadoCuentaService,
            PagoService pagoService,
            OcupanteService ocupanteService,
            UsuarioRepository usuarioRepository) {
        this.estadoCuentaService = estadoCuentaService;
        this.generadorEstadoCuentaService = generadorEstadoCuentaService;
        this.pagoService = pagoService;
        this.ocupanteService = ocupanteService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<EstadoCuentaResponse>> listarEstadosCuenta() {
        return ResponseEntity.ok(estadoCuentaService.listarTodosEstadosCuenta());
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generarEstadoCuenta(@Valid @RequestBody EstadoCuentaForm formulario) {
        try {
            EstadoCuentaResponse respuesta = generadorEstadoCuentaService.generarEstadoCuenta(formulario);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al generar estado de cuenta."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoCuentaResponse> obtenerEstadoCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(estadoCuentaService.obtenerEstadoCuentaResponse(id));
    }

    @GetMapping("/{id}/desglose")
    public ResponseEntity<List<DetalleGastoUnidadResponse>> verDesgloseEstadoCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(estadoCuentaService.listarDesgloseEstadoCuenta(id));
    }

    @GetMapping("/{id}/pagos")
    public ResponseEntity<List<PagoResponse>> verDesglosePagosEstadoCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.listarPagosPorEstadoCuenta(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstadoCuenta(@PathVariable Long id) {
        try {
            estadoCuentaService.eliminarEstadoCuenta(id);
            return ResponseEntity.ok(Map.of("mensaje", "Estado de cuenta eliminado."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al eliminar estado de cuenta."));
        }
    }

    @GetMapping("/mis-estados")
    @Transactional(readOnly = true)
    public ResponseEntity<?> verMiEstadoCuenta() {
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.buscarPorIdentificador(nombreUsuario).orElse(null);
        if (usuario != null) {
            List<Unidad> unidades = ocupanteService.buscarUnidadesPorDni(usuario.getNumeroDocumento());
            if (!unidades.isEmpty()) {
                Unidad miUnidad = unidades.get(0);

                Map<String, Object> condominioRes = new java.util.HashMap<>();
                condominioRes.put("id", miUnidad.getCondominio().getId());
                condominioRes.put("nombre", miUnidad.getCondominio().getNombre());

                Map<String, Object> unidadRes = new java.util.HashMap<>();
                unidadRes.put("id", miUnidad.getId());
                unidadRes.put("numeroUnidad", miUnidad.getNumeroUnidad());
                unidadRes.put("torre", miUnidad.getTorre());
                unidadRes.put("piso", miUnidad.getPiso());
                unidadRes.put("condominio", condominioRes);

                Map<String, Object> respuesta = Map.of(
                        "unidad", unidadRes,
                        "estadosCuenta", estadoCuentaService.listarEstadosCuentaPorUnidad(miUnidad.getId()),
                        "pagos", pagoService.listarPagosPorUnidad(miUnidad.getId()));
                return ResponseEntity.ok(respuesta);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "No se encontró unidad asociada al usuario."));
    }
}
