package pe.edu.utp.condominio.api.dominios.incidencias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.incidencias.models.Incidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.EstadoIncidencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    @Query("select i from Incidencia i where i.estado = :estado")
    Page<Incidencia> listarPorEstado(@Param("estado") EstadoIncidencia estado, Pageable pageable);

    @Query("select i from Incidencia i where " +
           "i.id in (select iac.id from IncidenciaAreaComun iac where iac.areaComun.condominio.id = :condominioId) or " +
           "i.id in (select iu.id from IncidenciaUnidad iu where iu.unidad.condominio.id = :condominioId)")
    Page<Incidencia> listarPorCondominio(@Param("condominioId") Long condominioId, Pageable pageable);

    @Query("select i from Incidencia i where " +
           "(i.id in (select iac.id from IncidenciaAreaComun iac where iac.areaComun.condominio.id = :condominioId) or " +
           "i.id in (select iu.id from IncidenciaUnidad iu where iu.unidad.condominio.id = :condominioId)) " +
           "and i.estado = :estado")
    Page<Incidencia> listarPorCondominioYEstado(@Param("condominioId") Long condominioId, @Param("estado") EstadoIncidencia estado,
            Pageable pageable);

    @Query("select count(iac) from IncidenciaAreaComun iac where iac.areaComun.id = :areaComunId")
    long contarPorArea(@Param("areaComunId") Long areaComunId);
}
