package pe.edu.utp.condominio.api.dominios.seguridad.controllers;

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

import jakarta.servlet.http.Cookie;
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
        return "seguridad/login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @Valid @ModelAttribute("loginRequest") LoginRequest request,
            BindingResult bindingResult,
            Model model,
            HttpServletResponse response) {
        
        if (bindingResult.hasErrors()) {
            return "seguridad/login";
        }

        try {
            var autenticacion = new UsernamePasswordAuthenticationToken(
                    request.getIdentificador(), request.getContrasena());
            authenticationManager.authenticate(autenticacion);

            Usuario usuario = usuarioRepository.buscarPorIdentificador(request.getIdentificador())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            String token = tokenService.generarToken(usuario);
            Cookie cookie = new Cookie("tokenAcceso", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/gestion-condominio";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Credenciales inválidas.");
            return "seguridad/login";
        }
    }

    @GetMapping("/logout")
    public String procesarLogout(HttpServletResponse response) {
        Cookie cookie = new Cookie("tokenAcceso", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/auth/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registroRequest", new RegistroUsuarioRequest());
        return "seguridad/registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @Valid @ModelAttribute("registroRequest") RegistroUsuarioRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "seguridad/registro";
        }

        try {
            autenticacionService.registrar(request);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "seguridad/registro";
        }
    }
}
