package pe.edu.utp.condominio.api.dominios.historial.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.utp.condominio.api.dominios.historial.models.HistorialTitularidad;

public interface HistorialTitularidadRepository extends JpaRepository<HistorialTitularidad, Long> {

    @EntityGraph(attributePaths = {"unidad"})
    @Query("SELECT h FROM HistorialTitularidad h JOIN h.unidad u " +
           "WHERE (:termino IS NULL OR " +
           "LOWER(u.numeroUnidad) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(h.propietarioAnterior) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(h.nuevoPropietario) LIKE LOWER(CONCAT('%', :termino, '%')))")
    Page<HistorialTitularidad> buscarHistorialPaginado(@Param("termino") String termino, Pageable paginacion);
}
