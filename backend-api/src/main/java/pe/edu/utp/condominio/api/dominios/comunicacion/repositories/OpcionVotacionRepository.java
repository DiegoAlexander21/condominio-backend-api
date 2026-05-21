package pe.edu.utp.condominio.api.dominios.comunicacion.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.OpcionVotacion;

public interface OpcionVotacionRepository extends JpaRepository<OpcionVotacion, Long> {

    @Query("select o from OpcionVotacion o where o.asamblea.id = :asambleaId")
    List<OpcionVotacion> listarPorAsamblea(@Param("asambleaId") Long asambleaId);
}

