package pe.edu.utp.condominio.api.dominios.finanzas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EvidenciaPago;

import java.util.List;

public interface EvidenciaPagoRepository extends JpaRepository<EvidenciaPago, Long> {

    @Query("select e from EvidenciaPago e where e.pago.id = :pagoId order by e.fechaRegistro")
    List<EvidenciaPago> listarPorPago(@Param("pagoId") Long pagoId);
}
