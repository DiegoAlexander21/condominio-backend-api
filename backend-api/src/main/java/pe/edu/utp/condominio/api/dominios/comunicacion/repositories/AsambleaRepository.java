package pe.edu.utp.condominio.api.dominios.comunicacion.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.Asamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.EstadoAsamblea;

public interface AsambleaRepository extends JpaRepository<Asamblea, Long> {

    @Query("select a from Asamblea a where a.condominio.id = :condominioId order by a.fechaInicio desc")
    List<Asamblea> listarPorCondominio(@Param("condominioId") Long condominioId);

    @Query("select a from Asamblea a where a.estado = :estado order by a.fechaInicio desc")
    List<Asamblea> listarPorEstado(@Param("estado") EstadoAsamblea estado);
}

