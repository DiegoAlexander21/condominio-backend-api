package pe.edu.utp.condominio.api.dominios.mantenimiento.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.InsumoMantenimiento;

public interface InsumoMantenimientoRepository extends JpaRepository<InsumoMantenimiento, Long> {

    @Query("select i from InsumoMantenimiento i order by i.nombre")
    List<InsumoMantenimiento> listarTodos();

    @Query("select i from InsumoMantenimiento i where i.stockActual <= i.stockMinimo order by i.nombre")
    List<InsumoMantenimiento> listarCriticos();
}

