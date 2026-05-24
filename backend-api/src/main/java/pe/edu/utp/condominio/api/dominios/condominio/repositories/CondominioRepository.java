package pe.edu.utp.condominio.api.dominios.condominio.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;

public interface CondominioRepository extends JpaRepository<Condominio, Long> {

    @Query("select c from Condominio c where lower(c.nombre) = lower(:nombre)")
    Optional<Condominio> buscarPorNombre(@Param("nombre") String nombre);

    @Query("select count(c) > 0 from Condominio c where lower(replace(c.nombre, ' ', '')) = lower(replace(:nombre, ' ', ''))")
    boolean existePorNombreEstricto(@Param("nombre") String nombre);

    @Query("select count(c) > 0 from Condominio c where lower(replace(c.nombre, ' ', '')) = lower(replace(:nombre, ' ', '')) and c.id != :id")
    boolean existePorNombreEstrictoYIdDiferente(@Param("nombre") String nombre, @Param("id") Long id);

    @Query("select c from Condominio c order by lower(c.nombre)")
    List<Condominio> listarTodosOrdenado();
}

