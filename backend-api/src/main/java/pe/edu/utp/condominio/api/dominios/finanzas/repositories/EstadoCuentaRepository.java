package pe.edu.utp.condominio.api.dominios.finanzas.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EstadoCuenta;

public interface EstadoCuentaRepository extends JpaRepository<EstadoCuenta, Long> {

    @Query("select e from EstadoCuenta e where e.unidad.id = :unidadId and e.periodo = :periodo")
    Optional<EstadoCuenta> buscarPorUnidadYPeriodo(@Param("unidadId") Long unidadId,
            @Param("periodo") LocalDate periodo);

    List<EstadoCuenta> findByUnidadId(Long unidadId);
}
