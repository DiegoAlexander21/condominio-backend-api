package pe.edu.utp.condominio.api.dominios.unidades.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Residente;

public interface ResidenteRepository extends JpaRepository<Residente, Long> {
    Optional<Residente> findByDni(String dni);
    Optional<Residente> findByEmail(String email);
}
