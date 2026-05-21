package pe.edu.utp.condominio.api.dominios.saludambiental.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.ChecklistSaludAmbiente;

public interface ChecklistSaludAmbienteRepository extends JpaRepository<ChecklistSaludAmbiente, Long> {

    @Query("select c from ChecklistSaludAmbiente c where c.areaComun.id = :areaComunId order by c.nombre")
    List<ChecklistSaludAmbiente> listarPorArea(@Param("areaComunId") Long areaComunId);
}

