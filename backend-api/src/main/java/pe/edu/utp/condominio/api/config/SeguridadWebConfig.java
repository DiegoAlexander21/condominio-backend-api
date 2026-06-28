package pe.edu.utp.condominio.api.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import pe.edu.utp.condominio.api.dominios.seguridad.security.FiltroJwt;

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
                        .requestMatchers("/auth/**", "/api/auth/**", "/css/**", "/js/**", "/images/**", "/assets/**",
                                "/webjars/**",
                                "/error")
                        .permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/finanzas/pagos")
                        .hasAnyRole("ADMINISTRADOR", "RESIDENTE")
                        .requestMatchers("/api/usuarios/**", "/api/condominios/**", "/api/unidades/**",
                                "/api/areas-comunes/**", "/api/reservas-areas/**", "/api/incidencias/**",
                                "/api/finanzas/estados-cuenta/mis-estados", 
                                "/api/finanzas/pagos/unidad/**", "/api/finanzas/estados-cuenta/*/desglose", 
                                "/api/finanzas/estados-cuenta/*/pagos")
                        .hasAnyRole("ADMINISTRADOR", "RESIDENTE")
                        .anyRequest().hasRole("ADMINISTRADOR"))
                .exceptionHandling(excepcion -> excepcion
                        .authenticationEntryPoint((solicitud, respuesta, excepcionAutenticacion) -> {
                            respuesta.setContentType("application/json");
                            respuesta.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            respuesta.getWriter().write("{\"error\": \"No autorizado\"}");
                        })
                        .accessDeniedHandler((solicitud, respuesta, excepcionAccesoDenegado) -> {
                            respuesta.setContentType("application/json");
                            respuesta.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            respuesta.getWriter().write("{\"error\": \"Acceso denegado\"}");
                        }))
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
        configuracion.setAllowedOriginPatterns(
                Arrays.asList("http://localhost:4200", "https://*.vercel.app", "*"));
        configuracion.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracion.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuracion.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource origen = new UrlBasedCorsConfigurationSource();
        origen.registerCorsConfiguration("/**", configuracion);
        return origen;
    }
}
