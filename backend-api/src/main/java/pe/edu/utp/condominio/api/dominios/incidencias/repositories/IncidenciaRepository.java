package pe.edu.utp.condominio.api.dominios.incidencias.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.incidencias.models.Incidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.EstadoIncidencia;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    @Query("select i from Incidencia i where i.estado = :estado order by i.fechaReporte desc")
    List<Incidencia> listarPorEstado(@Param("estado") EstadoIncidencia estado);

    @Query("select count(i) from Incidencia i where i.areaComun.id = :areaComunId")
    long contarPorArea(@Param("areaComunId") Long areaComunId);
}

