package pe.edu.utp.condominio.api.dominios.mantenimiento.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.UsoInsumo;

public interface UsoInsumoRepository extends JpaRepository<UsoInsumo, Long> {

    @Query("select u from UsoInsumo u where u.tarea.id = :tareaId")
    List<UsoInsumo> listarPorTarea(@Param("tareaId") Long tareaId);

    @Query("select u from UsoInsumo u where u.insumo.id = :insumoId order by u.fechaRegistro desc")
    List<UsoInsumo> listarPorInsumo(@Param("insumoId") Long insumoId);
}

