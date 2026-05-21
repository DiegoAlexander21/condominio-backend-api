package pe.edu.utp.condominio.api.dominios.finanzas.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.finanzas.models.DetalleGastoUnidad;

public interface DetalleGastoUnidadRepository extends JpaRepository<DetalleGastoUnidad, Long> {

    @Query("select d from DetalleGastoUnidad d where d.gasto.id = :gastoId order by d.montoAsignado desc")
    List<DetalleGastoUnidad> listarPorGasto(@Param("gastoId") Long gastoId);

    @Query("select d from DetalleGastoUnidad d where d.unidad.id = :unidadId order by d.fechaRegistro desc")
    List<DetalleGastoUnidad> listarPorUnidad(@Param("unidadId") Long unidadId);
}

