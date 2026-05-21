package pe.edu.utp.condominio.api.dominios.visitas.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.visitas.models.Visita;
import pe.edu.utp.condominio.api.dominios.visitas.enums.EstadoVisita;

public interface VisitaRepository extends JpaRepository<Visita, Long> {

    @Query("select v from Visita v where v.unidad.id = :unidadId order by v.fechaVisitaProgramada desc")
    List<Visita> listarPorUnidad(@Param("unidadId") Long unidadId);

    @Query("select v from Visita v where v.estado = :estado order by v.fechaRegistro desc")
    List<Visita> listarPorEstado(@Param("estado") EstadoVisita estado);

    @Query("select v from Visita v where v.fechaVisitaProgramada between :inicio and :fin order by v.fechaVisitaProgramada")
    List<Visita> listarPorRango(@Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
}

