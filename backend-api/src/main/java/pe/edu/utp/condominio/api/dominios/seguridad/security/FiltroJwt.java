package pe.edu.utp.condominio.api.dominios.seguridad.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.edu.utp.condominio.api.dominios.seguridad.services.TokenService;
import pe.edu.utp.condominio.api.dominios.seguridad.services.UsuarioService;

@Component
public class FiltroJwt extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public FiltroJwt(TokenService tokenService, UsuarioService usuarioService) {
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            FilterChain cadenaFiltros) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            cadenaFiltros.doFilter(solicitud, respuesta);
            return;
        }

        String token = null;
        String encabezado = solicitud.getHeader("Authorization");

        if (encabezado != null && encabezado.startsWith("Bearer ")) {
            token = encabezado.substring(7);
        } else if (solicitud.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : solicitud.getCookies()) {
                if ("tokenAcceso".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && tokenService.esTokenValido(token)) {
            try {
                Long usuarioId = tokenService.obtenerIdUsuario(token);
                var detallesUsuario = usuarioService.loadUserById(usuarioId);

                var autenticacion = new UsernamePasswordAuthenticationToken(
                        detallesUsuario, null, detallesUsuario.getAuthorities());
                autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(solicitud));

                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
            }
        }

        cadenaFiltros.doFilter(solicitud, respuesta);
    }
}
