package pe.edu.utp.condominio.api.dominios.finanzas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Gasto;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;

public interface GastoRepository extends JpaRepository<Gasto, Long> {

    @Query("select g from Gasto g where g.tipoGasto = :tipo")
    Page<Gasto> listarPorTipo(@Param("tipo") TipoGasto tipo, Pageable pageable);
}
