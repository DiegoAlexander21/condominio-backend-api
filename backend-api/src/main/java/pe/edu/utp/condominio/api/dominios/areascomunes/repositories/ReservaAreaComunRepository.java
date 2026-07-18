package pe.edu.utp.condominio.api.dominios.areascomunes.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.ReservaAreaComun;

public interface ReservaAreaComunRepository extends JpaRepository<ReservaAreaComun, Long> {

        @Query("select r from ReservaAreaComun r where r.areaComun.id = :areaComunId and r.fechaReserva = :fecha and r.estado = 'ACTIVA' order by r.id desc")
        Page<ReservaAreaComun> listarPorAreaYFechaPaginado(@Param("areaComunId") Long areaComunId,
                        @Param("fecha") LocalDate fecha, Pageable paginacion);

        @Query("select r from ReservaAreaComun r where r.areaComun.id = :areaComunId and r.fechaReserva = :fecha and r.estado = 'ACTIVA' order by r.horaInicio")
        List<ReservaAreaComun> listarPorAreaYFecha(@Param("areaComunId") Long areaComunId,
                        @Param("fecha") LocalDate fecha);

        @Query("select r from ReservaAreaComun r where r.areaComun.id = :areaComunId and r.estado = 'ACTIVA' order by r.id desc")
        Page<ReservaAreaComun> listarPorAreaPaginado(@Param("areaComunId") Long areaComunId, Pageable paginacion);

        @Query("select r from ReservaAreaComun r where r.areaComun.id = :areaComunId and r.estado = 'ACTIVA' order by r.fechaReserva desc, r.horaInicio asc")
        List<ReservaAreaComun> listarPorArea(@Param("areaComunId") Long areaComunId);

        @Query("select r from ReservaAreaComun r where r.areaComun.id = :areaComunId and r.unidad.id = :unidadId and r.estado = 'ACTIVA' order by r.id desc")
        Page<ReservaAreaComun> listarPorAreaYUnidadPaginado(@Param("areaComunId") Long areaComunId,
                        @Param("unidadId") Long unidadId, Pageable paginacion);

        @Query("select r from ReservaAreaComun r where r.areaComun.id = :areaComunId and r.fechaReserva = :fecha and r.unidad.id = :unidadId and r.estado = 'ACTIVA' order by r.id desc")
        Page<ReservaAreaComun> listarPorAreaFechaYUnidadPaginado(@Param("areaComunId") Long areaComunId,
                        @Param("fecha") LocalDate fecha, @Param("unidadId") Long unidadId, Pageable paginacion);
}
