package pe.edu.utp.condominio.api.dominios.comunicacion.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.VotoAsamblea;

public interface VotoAsambleaRepository extends JpaRepository<VotoAsamblea, Long> {

    @Query("select v from VotoAsamblea v where v.asamblea.id = :asambleaId")
    List<VotoAsamblea> listarPorAsamblea(@Param("asambleaId") Long asambleaId);

    @Query("select v from VotoAsamblea v where v.unidad.id = :unidadId order by v.fechaVoto desc")
    List<VotoAsamblea> listarPorUnidad(@Param("unidadId") Long unidadId);

    @Query("select count(v) > 0 from VotoAsamblea v where v.asamblea.id = :asambleaId and v.unidad.id = :unidadId")
    boolean existePorAsambleaYUnidad(@Param("asambleaId") Long asambleaId,
            @Param("unidadId") Long unidadId);
}

