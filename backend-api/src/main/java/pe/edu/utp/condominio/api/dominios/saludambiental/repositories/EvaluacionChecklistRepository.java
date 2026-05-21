package pe.edu.utp.condominio.api.dominios.saludambiental.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.EvaluacionChecklist;
import pe.edu.utp.condominio.api.dominios.saludambiental.enums.ResultadoChecklist;

public interface EvaluacionChecklistRepository extends JpaRepository<EvaluacionChecklist, Long> {

    @Query("select e from EvaluacionChecklist e where e.checklist.areaComun.id = :areaComunId order by e.fechaEvaluacion desc")
    List<EvaluacionChecklist> listarPorArea(@Param("areaComunId") Long areaComunId);

    @Query("select e from EvaluacionChecklist e where e.resultado = :resultado order by e.fechaEvaluacion desc")
    List<EvaluacionChecklist> listarPorResultado(@Param("resultado") ResultadoChecklist resultado);

    @Query("select count(e) from EvaluacionChecklist e where e.checklist.areaComun.id = :areaComunId and e.resultado = :resultado")
    long contarPorAreaYResultado(@Param("areaComunId") Long areaComunId, @Param("resultado") ResultadoChecklist resultado);
}
