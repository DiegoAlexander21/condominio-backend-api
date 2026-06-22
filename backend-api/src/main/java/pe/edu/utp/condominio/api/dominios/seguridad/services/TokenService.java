package pe.edu.utp.condominio.api.dominios.seguridad.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import pe.edu.utp.condominio.api.dominios.seguridad.config.JwtProperties;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Usuario;
import pe.edu.utp.condominio.api.dominios.unidades.models.Residente;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.ResidenteRepository;
import java.util.Optional;

@Service
public class TokenService {

    private final JwtProperties propiedadesJwt;
    private final ResidenteRepository residenteRepository;
    private Key clave;
    private long expiracionMillis;

    public TokenService(JwtProperties propiedadesJwt, ResidenteRepository residenteRepository) {
        this.propiedadesJwt = propiedadesJwt;
        this.residenteRepository = residenteRepository;
    }

    @PostConstruct
    public void init() {
        this.clave = Keys.hmacShaKeyFor(propiedadesJwt.getSecreto().getBytes(StandardCharsets.UTF_8));
        this.expiracionMillis = propiedadesJwt.getExpiracionMinutos() * 60 * 1000;
    }

    public String generarToken(Usuario usuario) {
        List<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getNombre().name())
                .collect(Collectors.toList());

        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expiracionMillis);

        Optional<Residente> residenteOpt = residenteRepository.findByDni(usuario.getNumeroDocumento());
        Long unidadId = residenteOpt
                .filter(Residente::isActivo)
                .map(r -> r.getUnidad().getId())
                .orElse(null);

        var builder = Jwts.builder()
                .setSubject(String.valueOf(usuario.getId()))
                .claim("roles", roles)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(clave, SignatureAlgorithm.HS256);
                
        if (unidadId != null) {
            builder.claim("unidadId", unidadId);
        }

        return builder.compact();
    }

    public Long obtenerIdUsuario(String token) {
        Claims reclamaciones = obtenerClaims(token);
        return Long.parseLong(reclamaciones.getSubject());
    }

    public boolean esTokenValido(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public long obtenerExpiracionSegundos() {
        return expiracionMillis / 1000;
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(clave)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
