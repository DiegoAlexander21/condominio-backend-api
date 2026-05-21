package pe.edu.utp.condominio.api.dominios.unidades.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;

public interface UnidadRepository extends JpaRepository<Unidad, Long> {

    @Query("select u from Unidad u where upper(u.torre) = upper(:torre) and u.piso = :piso and upper(u.numeroUnidad) = upper(:numeroUnidad)")
    Optional<Unidad> buscarPorClave(@Param("numeroUnidad") String numeroUnidad,
            @Param("torre") String torre,
            @Param("piso") int piso);

    @Query("select u from Unidad u order by upper(u.numeroUnidad)")
    List<Unidad> listarTodosOrdenado();
}

