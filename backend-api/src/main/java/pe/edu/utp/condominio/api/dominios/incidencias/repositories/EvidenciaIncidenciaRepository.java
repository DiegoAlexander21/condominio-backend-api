package pe.edu.utp.condominio.api.dominios.incidencias.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.incidencias.models.EvidenciaIncidencia;

public interface EvidenciaIncidenciaRepository extends JpaRepository<EvidenciaIncidencia, Long> {

    @Query("select e from EvidenciaIncidencia e where e.incidencia.id = :incidenciaId order by e.fechaRegistro")
    List<EvidenciaIncidencia> listarPorIncidencia(@Param("incidenciaId") Long incidenciaId);
}

