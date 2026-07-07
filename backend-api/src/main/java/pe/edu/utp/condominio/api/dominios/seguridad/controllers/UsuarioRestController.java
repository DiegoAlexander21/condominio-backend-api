package pe.edu.utp.condominio.api.dominios.seguridad.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.response.UsuarioPerfilResponse;
import pe.edu.utp.condominio.api.dominios.seguridad.services.TokenService;
import pe.edu.utp.condominio.api.dominios.seguridad.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;

    public UsuarioRestController(UsuarioService usuarioService, TokenService tokenService) {
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

    @GetMapping("/me")
    public ResponseEntity<UsuarioPerfilResponse> obtenerMiPerfil(HttpServletRequest request) {
        try {
            Long usuarioId = obtenerIdUsuarioDeRequest(request);
            UsuarioPerfilResponse perfil = usuarioService.obtenerMiPerfil(usuarioId);
            return ResponseEntity.ok(perfil);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/me/vincular")
    public ResponseEntity<?> vincularAUnidad(@RequestParam("unidadId") Long unidadId, HttpServletRequest request) {
        try {
            Long usuarioId = obtenerIdUsuarioDeRequest(request);
            usuarioService.vincularUnidad(usuarioId, unidadId);
            return ResponseEntity.ok().body("{\"mensaje\":\"Unidad vinculada exitosamente.\"}");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("{\"error\":\"Error interno al vincular la unidad.\"}");
        }
    }
    @PutMapping("/me/vincular-conserje")
    public ResponseEntity<?> vincularConserje(@RequestParam("condominioId") Long condominioId, HttpServletRequest request) {
        try {
            Long usuarioId = obtenerIdUsuarioDeRequest(request);
            usuarioService.vincularConserje(usuarioId, condominioId);
            return ResponseEntity.ok().body("{\"mensaje\":\"Condominio vinculado exitosamente.\"}");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("{\"error\":\"Error interno al vincular el condominio.\"}");
        }
    }
}
