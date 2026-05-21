package pe.edu.utp.condominio.api.dominios.paqueteria.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.paqueteria.models.Paquete;
import pe.edu.utp.condominio.api.dominios.paqueteria.enums.EstadoPaquete;

public interface PaqueteRepository extends JpaRepository<Paquete, Long> {

    @Query("select p from Paquete p where p.unidad.id = :unidadId order by p.fechaRecepcion desc")
    List<Paquete> listarPorUnidad(@Param("unidadId") Long unidadId);

    @Query("select p from Paquete p where p.estado = :estado order by p.fechaRecepcion desc")
    List<Paquete> listarPorEstado(@Param("estado") EstadoPaquete estado);
}

