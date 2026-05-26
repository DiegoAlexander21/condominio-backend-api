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

    @Query("select u from Unidad u left join u.propietario p left join u.residente r where p.dni = :dni or r.dni = :dni")
    List<Unidad> buscarPorDniOcupante(@Param("dni") String dni);

    @Query("select u from Unidad u where u.condominio.id = :condominioId order by upper(u.torre), upper(u.numeroUnidad)")
    List<Unidad> listarPorCondominio(@Param("condominioId") Long condominioId);

    @Query("select u from Unidad u where u.condominio.id = :condominioId and upper(u.torre) = upper(:torre) order by upper(u.numeroUnidad)")
    List<Unidad> listarPorCondominioYTorre(@Param("condominioId") Long condominioId, @Param("torre") String torre);

    @Query("select distinct u.torre from Unidad u where u.condominio.id = :condominioId order by u.torre")
    List<String> listarTorresPorCondominio(@Param("condominioId") Long condominioId);
}
