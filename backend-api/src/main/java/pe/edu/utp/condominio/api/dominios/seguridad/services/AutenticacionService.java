package pe.edu.utp.condominio.api.dominios.seguridad.services;

import java.util.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.utp.condominio.api.dominios.seguridad.dto.request.RegistroUsuarioRequest;
import pe.edu.utp.condominio.api.dominios.seguridad.enums.NombreRol;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Rol;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Usuario;
import pe.edu.utp.condominio.api.dominios.seguridad.repositories.RolRepository;
import pe.edu.utp.condominio.api.dominios.seguridad.repositories.UsuarioRepository;

@Service
public class AutenticacionService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder codificadorContrasena;

    public AutenticacionService(UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder codificadorContrasena) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.codificadorContrasena = codificadorContrasena;
    }

    public Usuario registrar(RegistroUsuarioRequest peticion) {
        validarRegistro(peticion);

        Rol rol = obtenerRol(peticion.getRol());

        Usuario usuario = new Usuario();
        usuario.setNombres(peticion.getNombres().trim());
        usuario.setApellidos(peticion.getApellidos().trim());
        usuario.setTipoDocumento(peticion.getTipoDocumento());
        usuario.setNumeroDocumento(peticion.getNumeroDocumento().trim());
        usuario.setTelefono(peticion.getTelefono().trim());
        usuario.setCorreo(peticion.getCorreo().trim().toLowerCase());
        usuario.setContrasenaHash(codificadorContrasena.encode(peticion.getContrasena()));
        usuario.setRoles(Collections.singleton(rol));

        return usuarioRepository.save(usuario);
    }

    private Rol obtenerRol(NombreRol nombreRol) {
        return rolRepository.findByNombre(nombreRol)
                .orElseGet(() -> rolRepository.save(new Rol(nombreRol)));
    }

    private void validarRegistro(RegistroUsuarioRequest peticion) {
        if (usuarioRepository.existsByNumeroDocumento(peticion.getNumeroDocumento())) {
            throw new IllegalArgumentException("El numero de documento ya esta registrado.");
        }

        if (usuarioRepository.existsByTelefono(peticion.getTelefono())) {
            throw new IllegalArgumentException("El telefono ya esta registrado.");
        }

        if (usuarioRepository.existsByCorreo(peticion.getCorreo().trim().toLowerCase())) {
            throw new IllegalArgumentException("El correo ya esta registrado.");
        }

        if (usuarioRepository.existsByNombresAndApellidos(peticion.getNombres(), peticion.getApellidos())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese nombre y apellido.");
        }

        if (!peticion.getTelefono().trim().matches("\\d{9}")) {
            throw new IllegalArgumentException("El teléfono debe tener 9 dígitos numéricos.");
        }

        if (peticion.getTipoDocumento() == pe.edu.utp.condominio.api.dominios.seguridad.enums.TipoDocumento.DNI) {
            if (!peticion.getNumeroDocumento().trim().matches("\\d{8}")) {
                throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos numéricos.");
            }
        } else if (peticion.getTipoDocumento() == pe.edu.utp.condominio.api.dominios.seguridad.enums.TipoDocumento.CE) {
            if (peticion.getNumeroDocumento().trim().length() < 9) {
                throw new IllegalArgumentException("El Carné de Extranjería (CE) debe tener al menos 9 caracteres.");
            }
        }
    }
}

