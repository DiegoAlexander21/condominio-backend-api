package pe.edu.utp.condominio.api.dominios.paqueteria.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.paqueteria.dto.request.PaqueteForm;
import pe.edu.utp.condominio.api.dominios.paqueteria.dto.request.RegistroEntregaPaqueteForm;
import pe.edu.utp.condominio.api.dominios.paqueteria.dto.response.PaqueteResponse;
import pe.edu.utp.condominio.api.dominios.paqueteria.enums.EstadoPaquete;
import pe.edu.utp.condominio.api.dominios.paqueteria.services.PaqueteService;
import pe.edu.utp.condominio.api.dominios.seguridad.services.TokenService;
import pe.edu.utp.condominio.api.dominios.seguridad.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/paqueteria")
public class PaqueteRestController {

    private final PaqueteService paqueteService;
    private final UsuarioService usuarioService;
    private final TokenService tokenService;

    public PaqueteRestController(PaqueteService paqueteService, UsuarioService usuarioService, TokenService tokenService) {
        this.paqueteService = paqueteService;
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

    @GetMapping
    public ResponseEntity<List<PaqueteResponse>> listarPaquetes(
            @RequestParam(value = "estado", required = false) EstadoPaquete estado,
            HttpServletRequest request) {
        Long usuarioId = obtenerIdUsuarioDeRequest(request);
        Long condominioId = usuarioService.obtenerCondominioIdDeUsuario(usuarioId);
        
        if (estado != null) {
            return ResponseEntity.ok(paqueteService.listarPorCondominioYEstado(condominioId, estado));
        }
        return ResponseEntity.ok(paqueteService.listarPorCondominio(condominioId));
    }

    @GetMapping("/unidad/{unidadId}")
    public ResponseEntity<List<PaqueteResponse>> listarPorUnidad(@PathVariable Long unidadId) {
        return ResponseEntity.ok(paqueteService.listarPorUnidad(unidadId));
    }

    @PostMapping
    public ResponseEntity<PaqueteResponse> registrarRecepcion(@Valid @RequestBody PaqueteForm formulario) {
        PaqueteResponse respuesta = paqueteService.registrarRecepcion(formulario);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}/entrega")
    public ResponseEntity<PaqueteResponse> registrarEntrega(
            @PathVariable Long id,
            @Valid @RequestBody RegistroEntregaPaqueteForm formulario) {
        formulario.setPaqueteId(id);
        PaqueteResponse respuesta = paqueteService.registrarEntrega(formulario);
        return ResponseEntity.ok(respuesta);
    }
}
