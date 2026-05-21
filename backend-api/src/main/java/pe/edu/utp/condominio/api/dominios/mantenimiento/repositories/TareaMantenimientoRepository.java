package pe.edu.utp.condominio.api.dominios.mantenimiento.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.TareaMantenimiento;

public interface TareaMantenimientoRepository extends JpaRepository<TareaMantenimiento, Long> {

    @Query("select t from TareaMantenimiento t where t.areaComun.id = :areaComunId order by t.fechaProgramada desc")
    List<TareaMantenimiento> listarPorArea(@Param("areaComunId") Long areaComunId);

    @Query("select t from TareaMantenimiento t where t.fechaProgramada between :inicio and :fin order by t.fechaProgramada")
    List<TareaMantenimiento> listarPorRango(@Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
}

