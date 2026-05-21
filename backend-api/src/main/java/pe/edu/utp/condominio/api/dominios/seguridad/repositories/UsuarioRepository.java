package pe.edu.utp.condominio.api.dominios.seguridad.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.seguridad.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.numeroDocumento = :numeroDocumento")
    Optional<Usuario> findByNumeroDocumento(@Param("numeroDocumento") String numeroDocumento);

    @Query("select u from Usuario u where u.telefono = :telefono")
    Optional<Usuario> findByTelefono(@Param("telefono") String telefono);

    @Query("select count(u) > 0 from Usuario u where u.numeroDocumento = :numeroDocumento")
    boolean existsByNumeroDocumento(@Param("numeroDocumento") String numeroDocumento);

    @Query("select count(u) > 0 from Usuario u where u.telefono = :telefono")
    boolean existsByTelefono(@Param("telefono") String telefono);

    @Query("select count(u) > 0 from Usuario u where u.correo = :correo")
    boolean existsByCorreo(@Param("correo") String correo);

    @Query("select u from Usuario u where u.numeroDocumento = :identificador or u.telefono = :identificador or u.correo = :identificador")
    Optional<Usuario> buscarPorIdentificador(@Param("identificador") String identificador);
}

