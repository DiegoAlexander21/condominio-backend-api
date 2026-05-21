package pe.edu.utp.condominio.api.dominios.saludambiental.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.ItemChecklist;

public interface ItemChecklistRepository extends JpaRepository<ItemChecklist, Long> {

    @Query("select i from ItemChecklist i where i.checklist.id = :checklistId order by i.orden")
    List<ItemChecklist> listarPorChecklist(@Param("checklistId") Long checklistId);
}

