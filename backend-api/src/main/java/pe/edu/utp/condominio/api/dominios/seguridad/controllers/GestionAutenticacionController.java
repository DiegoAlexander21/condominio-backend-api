package pe.edu.utp.condominio.api.dominios.seguridad.controllers;

import java.time.Duration;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.request.LoginRequest;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.request.RegistroUsuarioRequest;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Usuario;
import pe.edu.utp.condominio.api.dominios.seguridad.repositories.UsuarioRepository;
import pe.edu.utp.condominio.api.dominios.seguridad.services.AutenticacionService;
import pe.edu.utp.condominio.api.dominios.seguridad.services.TokenService;

@Controller
@RequestMapping("/auth")
public class GestionAutenticacionController {

    private final AutenticacionService autenticacionService;
    private final AuthenticationManager administradorAutenticacion;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public GestionAutenticacionController(AutenticacionService autenticacionService,
            AuthenticationManager administradorAutenticacion,
            TokenService tokenService,
            UsuarioRepository usuarioRepository) {
        this.autenticacionService = autenticacionService;
        this.administradorAutenticacion = administradorAutenticacion;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/login")
    public String mostrarLogin(Model modelo) {
        if (!modelo.containsAttribute("loginRequest")) {
            modelo.addAttribute("loginRequest", new LoginRequest());
        }
        return "dominios/seguridad/login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @Valid @ModelAttribute("loginRequest") LoginRequest peticion,
            BindingResult resultadoValidacion,
            Model modelo,
            HttpServletRequest peticionHttp,
            HttpServletResponse respuestaHttp) {

        if (resultadoValidacion.hasErrors()) {
            return "dominios/seguridad/login";
        }

        try {
            var autenticacion = new UsernamePasswordAuthenticationToken(
                    peticion.getIdentificador(), peticion.getContrasena());
            administradorAutenticacion.authenticate(autenticacion);

            Usuario usuario = usuarioRepository.buscarPorIdentificador(peticion.getIdentificador())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            String token = tokenService.generarToken(usuario);
            boolean seguro = peticionHttp.isSecure();
            ResponseCookie cookieJwt = ResponseCookie.from("tokenAcceso", token)
                    .httpOnly(true)
                    .secure(seguro)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofSeconds(tokenService.obtenerExpiracionSegundos()))
                    .build();
            respuestaHttp.addHeader("Set-Cookie", cookieJwt.toString());

            String rolNombre = usuario.getRoles().stream()
                    .map(rol -> rol.getNombre().name())
                    .findFirst()
                    .orElse("");

            return "ADMINISTRADOR".equals(rolNombre)
                    ? "redirect:/reportes/dashboard"
                    : "redirect:/auth/sin-panel";
        } catch (Exception ex) {
            modelo.addAttribute("mensajeError", "Credenciales inválidas.");
            return "dominios/seguridad/login";
        }
    }

    @GetMapping("/logout")
    public String procesarLogout(HttpServletRequest peticionHttp, HttpServletResponse respuestaHttp) {
        boolean seguro = peticionHttp.isSecure();
        ResponseCookie cookieJwt = ResponseCookie.from("tokenAcceso", "")
                .httpOnly(true)
                .secure(seguro)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
        respuestaHttp.addHeader("Set-Cookie", cookieJwt.toString());
        return "redirect:/auth/login";
    }

    @GetMapping("/sin-panel")
    public String mostrarSinPanel() {
        return "dominios/seguridad/sin-panel";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model modelo) {
        modelo.addAttribute("registroRequest", new RegistroUsuarioRequest());
        return "dominios/seguridad/registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @Valid @ModelAttribute("registroRequest") RegistroUsuarioRequest peticion,
            BindingResult resultadoValidacion,
            Model modelo,
            RedirectAttributes atributosRedireccion) {

        if (resultadoValidacion.hasErrors()) {
            return "dominios/seguridad/registro";
        }

        try {
            autenticacionService.registrar(peticion);
            atributosRedireccion.addFlashAttribute("mensajeExito",
                    "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException ex) {
            modelo.addAttribute("mensajeError", ex.getMessage());
            return "dominios/seguridad/registro";
        }
    }
}
