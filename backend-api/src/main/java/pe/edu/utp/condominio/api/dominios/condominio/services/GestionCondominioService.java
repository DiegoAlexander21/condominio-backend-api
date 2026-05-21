package pe.edu.utp.condominio.api.dominios.condominio.services;

import java.util.List;
import pe.edu.utp.condominio.api.dominios.condominio.dto.request.CondominioForm;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestionCondominioService {

    private final CondominioRepository condominioRepository;

    public GestionCondominioService(CondominioRepository condominioRepository) {
        this.condominioRepository = condominioRepository;
    }

    @Transactional
    public synchronized Condominio registrarOActualizarCondominio(CondominioForm form) {
        validarCondominio(form);

        String nombreNormalizado = form.getNombre().trim();
        int torres = form.getTorres();
        int pisosPorTorre = form.getPisosPorTorre();

        Condominio condominio = condominioRepository.buscarPorNombre(nombreNormalizado)
                .orElseGet(Condominio::new);

        condominio.setNombre(nombreNormalizado);
        condominio.setTorres(torres);
        condominio.setPisosPorTorre(pisosPorTorre);

        return condominioRepository.save(condominio);
    }

    public synchronized List<Condominio> obtenerCondominios() {
        return condominioRepository.listarTodosOrdenado();
    }

    public synchronized int obtenerTotalTorres() {
        List<Condominio> condominios = condominioRepository.findAll();
        int total = 0;

        for (Condominio condominio : condominios) {
            total += condominio.getTorres();
        }

        return total;
    }

    public synchronized int obtenerTotalPisos() {
        List<Condominio> condominios = condominioRepository.findAll();
        int total = 0;

        for (Condominio condominio : condominios) {
            total += condominio.getTorres() * condominio.getPisosPorTorre();
        }

        return total;
    }

    private void validarCondominio(CondominioForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario del condominio es obligatorio.");
        }

        if (form.getNombre() == null || form.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del condominio no puede estar vacio.");
        }

        if (form.getTorres() == null || form.getTorres() <= 0) {
            throw new IllegalArgumentException("El numero de torres debe ser mayor a cero.");
        }

        if (form.getTorres() > 100) {
            throw new IllegalArgumentException("El numero de torres no puede ser mayor a 100.");
        }

        if (form.getPisosPorTorre() == null || form.getPisosPorTorre() <= 0) {
            throw new IllegalArgumentException("El numero de pisos por torre debe ser mayor a cero.");
        }

        if (form.getPisosPorTorre() > 200) {
            throw new IllegalArgumentException("El numero de pisos por torre no puede ser mayor a 200.");
        }
    }
}

