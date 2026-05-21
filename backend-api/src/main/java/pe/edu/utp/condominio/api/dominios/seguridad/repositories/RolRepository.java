package pe.edu.utp.condominio.api.dominios.seguridad.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.condominio.api.dominios.seguridad.enums.NombreRol;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(NombreRol nombre);
}

