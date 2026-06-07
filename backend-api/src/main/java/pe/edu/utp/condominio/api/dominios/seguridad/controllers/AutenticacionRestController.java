package pe.edu.utp.condominio.api.dominios.seguridad.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.request.LoginRequest;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.request.RegistroUsuarioRequest;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.response.LoginResponse;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Usuario;
import pe.edu.utp.condominio.api.dominios.seguridad.repositories.UsuarioRepository;
import pe.edu.utp.condominio.api.dominios.seguridad.services.AutenticacionService;
import pe.edu.utp.condominio.api.dominios.seguridad.services.TokenService;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionRestController {

    private final AuthenticationManager administradorAutenticacion;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final AutenticacionService autenticacionService;

    public AutenticacionRestController(AuthenticationManager administradorAutenticacion,
            TokenService tokenService,
            UsuarioRepository usuarioRepository,
            AutenticacionService autenticacionService) {
        this.administradorAutenticacion = administradorAutenticacion;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.autenticacionService = autenticacionService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> procesarLogin(@Valid @RequestBody LoginRequest peticion) {
        var usuarioOpt = usuarioRepository.buscarPorIdentificador(peticion.getIdentificador());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("{\"codigo\":\"identificador_no_encontrado\", \"error\":\"El correo no está registrado.\"}");
        }

        try {
            var autenticacion = new UsernamePasswordAuthenticationToken(
                    peticion.getIdentificador(), peticion.getContrasena());
            administradorAutenticacion.authenticate(autenticacion);

            Usuario usuario = usuarioOpt.get();
            String token = tokenService.generarToken(usuario);

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (Exception ex) {
            return ResponseEntity.status(401).body("{\"codigo\":\"contrasena_incorrecta\", \"error\":\"La contraseña es incorrecta.\"}");
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegistroUsuarioRequest peticion) {
        try {
            autenticacionService.registrar(peticion);
            return ResponseEntity.status(201).body("{\"mensaje\":\"Usuario registrado exitosamente\"}");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("{\"error\":\"Error interno al registrar el usuario\"}");
        }
    }
}
