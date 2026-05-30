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
    public synchronized Condominio registrarOActualizarCondominio(CondominioForm formulario) {
        validarCondominio(formulario);

        String nombreNormalizado = formulario.getNombre().trim();
        
        if (formulario.getId() != null) {
            if (condominioRepository.existePorNombreEstrictoYIdDiferente(nombreNormalizado, formulario.getId())) {
                throw new IllegalArgumentException("El condominio '" + nombreNormalizado + "' ya se encuentra registrado.");
            }
            Condominio condominio = condominioRepository.findById(formulario.getId()).orElseThrow(() -> new IllegalArgumentException("Condominio no encontrado."));
            condominio.setNombre(nombreNormalizado);
            condominio.setTorres(formulario.getTorres());
            condominio.setPisosPorTorre(formulario.getPisosPorTorre());
            return condominioRepository.save(condominio);
        } else {
            if (condominioRepository.existePorNombreEstricto(nombreNormalizado)) {
                throw new IllegalArgumentException("El condominio '" + nombreNormalizado + "' ya se encuentra registrado.");
            }
            Condominio condominio = new Condominio();
            condominio.setNombre(nombreNormalizado);
            condominio.setTorres(formulario.getTorres());
            condominio.setPisosPorTorre(formulario.getPisosPorTorre());
            return condominioRepository.save(condominio);
        }
    }

    public synchronized void eliminarCondominio(Long id) {
        condominioRepository.deleteById(id);
    }

    public synchronized CondominioForm obtenerFormCondominio(Long id) {
        Condominio condominio = condominioRepository.findById(id).orElse(null);
        if (condominio == null) return null;
        CondominioForm formulario = new CondominioForm();
        formulario.setId(condominio.getId());
        formulario.setNombre(condominio.getNombre());
        formulario.setTorres(condominio.getTorres());
        formulario.setPisosPorTorre(condominio.getPisosPorTorre());
        return formulario;
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

    private void validarCondominio(CondominioForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario del condominio es obligatorio.");
        }

        if (formulario.getNombre() == null || formulario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del condominio no puede estar vacio.");
        }

        if (formulario.getTorres() == null || formulario.getTorres() <= 0) {
            throw new IllegalArgumentException("El numero de torres debe ser mayor a cero.");
        }

        if (formulario.getPisosPorTorre() == null || formulario.getPisosPorTorre() <= 0) {
            throw new IllegalArgumentException("El numero de pisos por torre debe ser mayor a cero.");
        }
    }
}

