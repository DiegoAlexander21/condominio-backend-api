package pe.edu.utp.condominio.api.dominios.saludambiental.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.RegistroMantenimientoAmbiental;

public interface RegistroMantenimientoAmbientalRepository extends JpaRepository<RegistroMantenimientoAmbiental, Long> {

    @Query("select r from RegistroMantenimientoAmbiental r where r.areaComun.id = :areaComunId order by r.fechaRegistro desc")
    List<RegistroMantenimientoAmbiental> listarPorArea(@Param("areaComunId") Long areaComunId);
}

