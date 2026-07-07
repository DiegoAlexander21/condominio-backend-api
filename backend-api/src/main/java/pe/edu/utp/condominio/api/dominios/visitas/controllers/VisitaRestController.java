package pe.edu.utp.condominio.api.dominios.visitas.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.visitas.dto.request.RegistroIngresoVisitaForm;
import pe.edu.utp.condominio.api.dominios.visitas.dto.request.RegistroSalidaVisitaForm;
import pe.edu.utp.condominio.api.dominios.visitas.dto.request.VisitaForm;
import pe.edu.utp.condominio.api.dominios.visitas.dto.response.VisitaResponse;
import pe.edu.utp.condominio.api.dominios.visitas.enums.EstadoVisita;
import pe.edu.utp.condominio.api.dominios.visitas.services.VisitaService;
import pe.edu.utp.condominio.api.dominios.seguridad.services.TokenService;
import pe.edu.utp.condominio.api.dominios.seguridad.services.UsuarioService;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.response.UsuarioPerfilResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/visitas")
public class VisitaRestController {

    private final VisitaService visitaService;
    private final UsuarioService usuarioService;
    private final TokenService tokenService;

    public VisitaRestController(VisitaService visitaService, UsuarioService usuarioService, TokenService tokenService) {
        this.visitaService = visitaService;
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
    }

    private Long obtenerIdUsuarioDeRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token inválido o ausente.");
        }
        String token = authHeader.substring(7);
        return tokenService.obtenerIdUsuario(token);
    }

    @PostMapping
    public ResponseEntity<?> registrarVisita(@Valid @RequestBody VisitaForm formulario) {
        try {
            VisitaResponse respuesta = visitaService.registrarVisita(formulario);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al programar visita"));
        }
    }

    @PostMapping("/ingreso")
    public ResponseEntity<?> registrarIngreso(@Valid @RequestBody RegistroIngresoVisitaForm formulario) {
        try {
            VisitaResponse respuesta = visitaService.registrarIngreso(formulario);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al registrar ingreso"));
        }
    }

    @PostMapping("/salida")
    public ResponseEntity<?> registrarSalida(@Valid @RequestBody RegistroSalidaVisitaForm formulario) {
        try {
            VisitaResponse respuesta = visitaService.registrarSalida(formulario);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al registrar salida"));
        }
    }

    @GetMapping
    public ResponseEntity<List<VisitaResponse>> listarVisitas(
            @RequestParam(value = "estado", required = false) EstadoVisita estado,
            HttpServletRequest request) {
        Long usuarioId = obtenerIdUsuarioDeRequest(request);
        UsuarioPerfilResponse perfil = usuarioService.obtenerMiPerfil(usuarioId);

        if ("ADMINISTRADOR".equals(perfil.getRol())) {
            if (estado != null) {
                return ResponseEntity.ok(visitaService.listarTodas(estado));
            }
            return ResponseEntity.ok(visitaService.listarTodas());
        }

        Long condominioId = usuarioService.obtenerCondominioIdDeUsuario(usuarioId);

        if (estado != null) {
            return ResponseEntity.ok(visitaService.listarPorCondominioYEstado(condominioId, estado));
        }
        return ResponseEntity.ok(visitaService.listarPorCondominio(condominioId));
    }

    @GetMapping("/unidad/{unidadId}")
    public ResponseEntity<List<VisitaResponse>> listarVisitasPorUnidad(@PathVariable Long unidadId) {
        return ResponseEntity.ok(visitaService.listarPorUnidad(unidadId));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionesDeValidacion(MethodArgumentNotValidException ex) {
        String mensajeError = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos.");
        return ResponseEntity.badRequest().body(Map.of("error", mensajeError));
    }
}
