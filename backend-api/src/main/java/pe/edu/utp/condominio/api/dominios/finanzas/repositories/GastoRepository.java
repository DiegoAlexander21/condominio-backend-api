package pe.edu.utp.condominio.api.dominios.finanzas.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Gasto;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;

public interface GastoRepository extends JpaRepository<Gasto, Long> {

    @Query("select g from Gasto g where g.tipoGasto = :tipo order by g.fechaRegistro desc")
    List<Gasto> listarPorTipo(@Param("tipo") TipoGasto tipo);
}

