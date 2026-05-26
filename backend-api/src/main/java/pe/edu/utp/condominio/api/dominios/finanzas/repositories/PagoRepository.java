package pe.edu.utp.condominio.api.dominios.finanzas.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Query("select p from Pago p where p.unidad.id = :unidadId order by p.fechaPago desc")
    List<Pago> listarPorUnidad(@Param("unidadId") Long unidadId);

    @Query("select p from Pago p where p.estadoCuenta.id = :estadoCuentaId order by p.fechaPago desc")
    List<Pago> listarPorEstadoCuenta(@Param("estadoCuentaId") Long estadoCuentaId);
}

