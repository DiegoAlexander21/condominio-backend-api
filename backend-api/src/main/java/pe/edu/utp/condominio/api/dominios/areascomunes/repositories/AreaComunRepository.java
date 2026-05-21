package pe.edu.utp.condominio.api.dominios.areascomunes.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;

public interface AreaComunRepository extends JpaRepository<AreaComun, Long> {

    @Query("select a from AreaComun a where a.condominio.id = :condominioId order by lower(a.nombre)")
    List<AreaComun> listarPorCondominio(@Param("condominioId") Long condominioId);

    @Query("select a from AreaComun a where a.condominio.id = :condominioId and lower(a.nombre) = lower(:nombre)")
    AreaComun buscarPorNombre(@Param("condominioId") Long condominioId, @Param("nombre") String nombre);
}

