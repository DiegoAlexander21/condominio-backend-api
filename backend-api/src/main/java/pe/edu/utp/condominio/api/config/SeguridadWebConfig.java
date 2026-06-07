package pe.edu.utp.condominio.api.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pe.edu.utp.condominio.api.dominios.seguridad.security.FiltroJwt;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SeguridadWebConfig {

    private final FiltroJwt filtroJwt;

    public SeguridadWebConfig(FiltroJwt filtroJwt) {
        this.filtroJwt = filtroJwt;
    }

    @Bean
    public SecurityFilterChain cadenaFiltrosSeguridad(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(configuracionCors()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(autenticacion -> autenticacion
                        .requestMatchers("/auth/**", "/api/auth/**", "/css/**", "/js/**", "/images/**", "/assets/**", "/webjars/**",
                                "/error")
                        .permitAll()
                        .anyRequest().hasRole("ADMINISTRADOR"))
                .exceptionHandling(excepcion -> excepcion
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**"))
                        .defaultAccessDeniedHandlerFor(
                                (solicitud, respuesta, excepcionAccesoDenegado) -> respuesta
                                        .sendError(HttpServletResponse.SC_FORBIDDEN),
                                new AntPathRequestMatcher("/api/**"))
                        .authenticationEntryPoint(
                                (solicitud, respuesta, excepcionAutenticacion) -> respuesta.sendRedirect("/auth/login"))
                        .accessDeniedHandler(
                                (solicitud, respuesta, excepcionAccesoDenegado) -> respuesta.sendRedirect("/auth/sin-panel")))
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder codificadorContrasena() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager gestorAutenticacion(AuthenticationConfiguration configuracion) throws Exception {
        return configuracion.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource configuracionCors() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuracion.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracion.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuracion.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource origen = new UrlBasedCorsConfigurationSource();
        origen.registerCorsConfiguration("/**", configuracion);
        return origen;
    }
}
