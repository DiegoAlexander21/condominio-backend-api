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

    @Query("select i from Incidencia i where type(i) = IncidenciaAreaComun or i.reportadoPorUnidadId = :unidadId or i.id in (select iu.id from IncidenciaUnidad iu where iu.unidad.id = :unidadId)")
    Page<Incidencia> listarPorUnidad(@Param("unidadId") Long unidadId, Pageable pageable);

    @Query("select i from Incidencia i where (type(i) = IncidenciaAreaComun or i.reportadoPorUnidadId = :unidadId or i.id in (select iu.id from IncidenciaUnidad iu where iu.unidad.id = :unidadId)) and i.estado = :estado")
    Page<Incidencia> listarPorUnidadYEstado(@Param("unidadId") Long unidadId, @Param("estado") EstadoIncidencia estado,
            Pageable pageable);

    @Query("select count(i) from Incidencia i where i.areaComun.id = :areaComunId")
    long contarPorArea(@Param("areaComunId") Long areaComunId);
}
