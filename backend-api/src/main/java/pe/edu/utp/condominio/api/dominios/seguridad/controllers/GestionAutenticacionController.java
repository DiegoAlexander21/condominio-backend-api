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
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public GestionAutenticacionController(AutenticacionService autenticacionService,
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            UsuarioRepository usuarioRepository) {
        this.autenticacionService = autenticacionService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        if (!model.containsAttribute("loginRequest")) {
            model.addAttribute("loginRequest", new LoginRequest());
        }
        return "dominios/seguridad/login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @Valid @ModelAttribute("loginRequest") LoginRequest request,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest httpRequest,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "dominios/seguridad/login";
        }

        try {
            var autenticacion = new UsernamePasswordAuthenticationToken(
                    request.getIdentificador(), request.getContrasena());
            authenticationManager.authenticate(autenticacion);

            Usuario usuario = usuarioRepository.buscarPorIdentificador(request.getIdentificador())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            String token = tokenService.generarToken(usuario);
            boolean seguro = httpRequest.isSecure();
            ResponseCookie cookieJwt = ResponseCookie.from("tokenAcceso", token)
                    .httpOnly(true)
                    .secure(seguro)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofSeconds(tokenService.obtenerExpiracionSegundos()))
                    .build();
            response.addHeader("Set-Cookie", cookieJwt.toString());

            return "redirect:/reportes/dashboard";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Credenciales inválidas.");
            return "dominios/seguridad/login";
        }
    }

    @GetMapping("/logout")
    public String procesarLogout(HttpServletRequest httpRequest, HttpServletResponse response) {
        boolean seguro = httpRequest.isSecure();
        ResponseCookie cookieJwt = ResponseCookie.from("tokenAcceso", "")
                .httpOnly(true)
                .secure(seguro)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader("Set-Cookie", cookieJwt.toString());
        return "redirect:/auth/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registroRequest", new RegistroUsuarioRequest());
        return "dominios/seguridad/registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @Valid @ModelAttribute("registroRequest") RegistroUsuarioRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "dominios/seguridad/registro";
        }

        try {
            autenticacionService.registrar(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "dominios/seguridad/registro";
        }
    }
}
