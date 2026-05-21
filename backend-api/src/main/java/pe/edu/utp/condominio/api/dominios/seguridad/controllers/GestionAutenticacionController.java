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

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.request.LoginRequest;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.request.RegistroUsuarioRequest;
import pe.edu.utp.condominio.api.dominios.seguridad.services.AutenticacionService;

@Controller
@RequestMapping("/auth")
public class GestionAutenticacionController {

    private final AutenticacionService autenticacionService;
    private final AuthenticationManager authenticationManager;

    public GestionAutenticacionController(AutenticacionService autenticacionService,
            AuthenticationManager authenticationManager) {
        this.autenticacionService = autenticacionService;
        this.authenticationManager = authenticationManager;
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
            Model model) {
        
        if (bindingResult.hasErrors()) {
            return "seguridad/login";
        }

        try {
            var autenticacion = new UsernamePasswordAuthenticationToken(
                    request.getIdentificador(), request.getContrasena());
            authenticationManager.authenticate(autenticacion);
            return "redirect:/gestion-condominio";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Credenciales inválidas.");
            return "seguridad/login";
        }
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
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "seguridad/registro";
        }

        autenticacionService.registrar(request);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
        return "redirect:/auth/login";
    }
}
