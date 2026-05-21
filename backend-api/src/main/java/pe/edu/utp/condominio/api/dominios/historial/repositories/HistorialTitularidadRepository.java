package pe.edu.utp.condominio.api.dominios.historial.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.utp.condominio.api.dominios.historial.models.HistorialTitularidad;

public interface HistorialTitularidadRepository extends JpaRepository<HistorialTitularidad, Long> {

    @Query("select h from HistorialTitularidad h order by h.fechaCambio desc")
    List<HistorialTitularidad> listarTodoOrdenado();
}

