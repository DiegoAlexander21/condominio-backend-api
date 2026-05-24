package pe.edu.utp.condominio.api.dominios.unidades.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;

public interface UnidadRepository extends JpaRepository<Unidad, Long> {

    @Query("select u from Unidad u where u.condominio.id = :condominioId and upper(u.torre) = upper(:torre) and upper(u.numeroUnidad) = upper(:numeroUnidad)")
    Optional<Unidad> buscarPorCondominioTorreYNumero(@Param("condominioId") Long condominioId,
            @Param("torre") String torre,
            @Param("numeroUnidad") String numeroUnidad);

    @Query("select u from Unidad u join fetch u.condominio order by upper(u.condominio.nombre), upper(u.torre), upper(u.numeroUnidad)")
    List<Unidad> listarTodosConCondominioOrdenado();
}
