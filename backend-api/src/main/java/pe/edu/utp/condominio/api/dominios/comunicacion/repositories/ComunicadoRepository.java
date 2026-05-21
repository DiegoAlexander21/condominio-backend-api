package pe.edu.utp.condominio.api.dominios.comunicacion.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.Comunicado;

public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {

    @Query("select c from Comunicado c where c.condominio.id = :condominioId order by c.fechaPublicacion desc")
    List<Comunicado> listarPorCondominio(@Param("condominioId") Long condominioId);
}

