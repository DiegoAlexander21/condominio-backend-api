package pe.edu.utp.condominio.api.dominios.areascomunes.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;

public interface AreaComunRepository extends JpaRepository<AreaComun, Long> {

    @Query("select a from AreaComun a where a.condominio.id = :condominioId order by a.id desc")
    List<AreaComun> listarPorCondominio(@Param("condominioId") Long condominioId);

    @Query("select a from AreaComun a where a.condominio.id = :condominioId and lower(a.nombre) = lower(:nombre)")
    AreaComun buscarPorNombre(@Param("condominioId") Long condominioId, @Param("nombre") String nombre);

    @Query("select a from AreaComun a join fetch a.condominio order by a.id desc")
    List<AreaComun> listarTodosConCondominio();

    @Query(value = "select a from AreaComun a join fetch a.condominio where a.condominio.id = :condominioId order by a.id desc", countQuery = "select count(a) from AreaComun a where a.condominio.id = :condominioId")
    Page<AreaComun> listarPorCondominioPaginado(@Param("condominioId") Long condominioId, Pageable paginacion);

    @Query(value = "select a from AreaComun a join fetch a.condominio order by a.id desc", countQuery = "select count(a) from AreaComun a")
    Page<AreaComun> listarTodosConCondominioPaginado(Pageable paginacion);
}

