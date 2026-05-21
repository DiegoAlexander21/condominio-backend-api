package pe.edu.utp.condominio.api.dominios.calificaciones.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.calificaciones.models.CalificacionArea;

public interface CalificacionAreaRepository extends JpaRepository<CalificacionArea, Long> {

    @Query("select c from CalificacionArea c where c.areaComun.id = :areaComunId order by c.fechaRegistro desc")
    List<CalificacionArea> listarPorArea(@Param("areaComunId") Long areaComunId);

    @Query("select c from CalificacionArea c where c.unidad.id = :unidadId order by c.fechaRegistro desc")
    List<CalificacionArea> listarPorUnidad(@Param("unidadId") Long unidadId);

    @Query("select avg(c.puntaje) from CalificacionArea c where c.areaComun.id = :areaComunId")
    Double obtenerPromedioPorArea(@Param("areaComunId") Long areaComunId);
}

