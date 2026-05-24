package pe.edu.utp.condominio.api.dominios.areascomunes.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.ReservaAreaComun;

public interface ReservaAreaComunRepository extends JpaRepository<ReservaAreaComun, Long> {

    @Query("select r from ReservaAreaComun r where r.areaComun.id = :areaComunId and r.fechaReserva = :fecha order by r.horaInicio")
    List<ReservaAreaComun> listarPorAreaYFecha(@Param("areaComunId") Long areaComunId,
            @Param("fecha") LocalDate fecha);

    @Query("select r from ReservaAreaComun r where r.areaComun.id = :areaComunId order by r.fechaReserva desc, r.horaInicio asc")
    List<ReservaAreaComun> listarPorArea(@Param("areaComunId") Long areaComunId);
}

