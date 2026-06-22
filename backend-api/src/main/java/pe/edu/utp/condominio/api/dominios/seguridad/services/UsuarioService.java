package pe.edu.utp.condominio.api.dominios.seguridad.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Usuario;
import pe.edu.utp.condominio.api.dominios.seguridad.repositories.UsuarioRepository;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.response.UsuarioPerfilResponse;
import pe.edu.utp.condominio.api.dominios.unidades.models.Residente;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.ResidenteRepository;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final UnidadRepository unidadRepository;
    private final ResidenteRepository residenteRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, 
                          UnidadRepository unidadRepository, 
                          ResidenteRepository residenteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.unidadRepository = unidadRepository;
        this.residenteRepository = residenteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identificador) {
        Usuario usuario = usuarioRepository.buscarPorIdentificador(identificador)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado."));
        return construirUserDetails(usuario);
    }

    public UserDetails loadUserById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado."));
        return construirUserDetails(usuario);
    }

    public Usuario obtenerUsuarioPorIdentificador(String identificador) {
        return usuarioRepository.buscarPorIdentificador(identificador)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    private UserDetails construirUserDetails(Usuario usuario) {
        List<GrantedAuthority> autoridades = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre().name()))
                .collect(Collectors.toList());

        return User.withUsername(usuario.getNumeroDocumento())
                .password(usuario.getContrasenaHash())
                .authorities(autoridades)
                .disabled(!usuario.isActivo())
                .build();
    }

    @Transactional(readOnly = true)
    public UsuarioPerfilResponse obtenerMiPerfil(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String rol = usuario.getRoles().stream()
                .map(r -> r.getNombre().name())
                .findFirst()
                .orElse("SIN_ROL");

        Optional<Residente> residenteOpt = residenteRepository.findByDni(usuario.getNumeroDocumento());
        Long unidadId = residenteOpt
                .filter(Residente::isActivo)
                .map(r -> r.getUnidad().getId())
                .orElse(null);

        String nombreCondominio = null;
        String torre = null;
        Integer piso = null;
        String numeroUnidad = null;

        if (unidadId != null) {
            pe.edu.utp.condominio.api.dominios.unidades.models.Unidad unidad = residenteOpt.get().getUnidad();
            if (unidad.getCondominio() != null) {
                nombreCondominio = unidad.getCondominio().getNombre();
            }
            torre = unidad.getTorre();
            piso = unidad.getPiso();
            numeroUnidad = unidad.getNumeroUnidad();
        }

        return new UsuarioPerfilResponse(
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getNumeroDocumento(),
                usuario.getCorreo(),
                rol,
                unidadId,
                nombreCondominio,
                torre,
                piso,
                numeroUnidad
        );
    }

    @Transactional
    public void vincularUnidad(Long usuarioId, Long unidadId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Optional<Residente> residenteOpt = residenteRepository.findByDni(usuario.getNumeroDocumento());
        if (residenteOpt.isPresent()) {
            throw new IllegalArgumentException("El usuario ya está vinculado a una unidad como residente.");
        }

        Unidad unidad = unidadRepository.findById(unidadId)
                .orElseThrow(() -> new IllegalArgumentException("Unidad no encontrada"));

        if (unidad.getResidente() != null && unidad.getResidente().isActivo()) {
            throw new IllegalArgumentException("La unidad seleccionada ya se encuentra ocupada por un residente activo. Por favor, contacte al Administrador.");
        }

        Residente residente = new Residente();
        residente.setUnidad(unidad);
        residente.setNombre(usuario.getNombres() + " " + usuario.getApellidos());
        residente.setDni(usuario.getNumeroDocumento());
        residente.setEmail(usuario.getCorreo());
        residente.setActivo(true);
        residente.setParentesco("Titular");

        residenteRepository.save(residente);
    }
}

