package pe.edu.utp.condominio.api.dominios.condominio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.condominio.api.dominios.condominio.models.Conserje;

import java.util.Optional;

public interface ConserjeRepository extends JpaRepository<Conserje, Long> {
    Optional<Conserje> findByDni(String dni);
}
