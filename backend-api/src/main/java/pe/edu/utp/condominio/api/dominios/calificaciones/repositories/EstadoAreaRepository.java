package pe.edu.utp.condominio.api.dominios.calificaciones.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.calificaciones.models.EstadoArea;

public interface EstadoAreaRepository extends JpaRepository<EstadoArea, Long> {

    @Query("select e from EstadoArea e where e.areaComun.id = :areaComunId order by e.fechaCalculo desc")
    List<EstadoArea> listarPorArea(@Param("areaComunId") Long areaComunId);
}

