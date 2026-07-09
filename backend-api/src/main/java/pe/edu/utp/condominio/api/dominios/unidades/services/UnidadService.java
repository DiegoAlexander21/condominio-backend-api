package pe.edu.utp.condominio.api.dominios.unidades.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.UnidadForm;
import pe.edu.utp.condominio.api.dominios.unidades.dto.response.TorreDto;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class UnidadService {

    private final UnidadRepository unidadRepository;
    private final CondominioRepository condominioRepository;

    public UnidadService(UnidadRepository unidadRepository,
            CondominioRepository condominioRepository) {
        this.unidadRepository = unidadRepository;
        this.condominioRepository = condominioRepository;
    }

    @Transactional
    public synchronized Unidad registrarOActualizarUnidad(UnidadForm formulario) {
        validarUnidad(formulario);

        Condominio condominio = obtenerCondominio(formulario.getNombreCondominio());
        Optional<Unidad> existente = unidadRepository.buscarPorCondominioTorreYNumero(condominio.getId(),
                formulario.getTorre(), formulario.getNumeroUnidad());

        if (existente.isPresent() && !existente.get().getId().equals(formulario.getId())) {
            throw new IllegalArgumentException("Ya existe la Unidad '" + formulario.getNumeroUnidad()
                    + "' en la torre/bloque '" + formulario.getTorre() + "' de este condominio.");
        }

        if (formulario.getId() != null) {
            return unidadRepository.findById(formulario.getId())
                    .map(unidad -> actualizarUnidadExistente(unidad, formulario))
                    .orElseGet(() -> crearNuevaUnidad(formulario));
        }

        return existente.map(unidad -> actualizarUnidadExistente(unidad, formulario))
                .orElseGet(() -> crearNuevaUnidad(formulario));
    }

    public synchronized Page<Unidad> obtenerUnidadesPaginadas(Pageable paginacion) {
        return unidadRepository.listarTodosConCondominioPaginado(paginacion);
    }

    public synchronized List<Unidad> obtenerUnidades() {
        return unidadRepository.listarTodosConCondominioOrdenado();
    }

    public synchronized List<String> listarTorresPorCondominio(Long condominioId) {
        if (condominioId == null) return List.of();
        return unidadRepository.listarTorresPorCondominio(condominioId);
    }

    public synchronized List<TorreDto> buscarTorresPorCondominios(List<Long> condominioIds) {
        if (condominioIds == null || condominioIds.isEmpty()) return List.of();
        return unidadRepository.listarTorresPorCondominios(condominioIds);
    }

    public synchronized List<Unidad> buscarUnidadesPorTorres(List<Long> condominioIds, List<String> torres) {
        if (condominioIds == null || condominioIds.isEmpty() || torres == null || torres.isEmpty()) return List.of();
        return unidadRepository.listarUnidadesPorTorres(condominioIds, torres);
    }

    public synchronized Unidad buscarPorId(Long id) {
        return unidadRepository.findById(id).orElse(null);
    }

    @Transactional
    public synchronized void eliminarUnidad(Long id) {
        unidadRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public synchronized UnidadForm obtenerFormUnidad(Long id) {
        Unidad unidad = unidadRepository.findById(id).orElse(null);
        if (unidad == null) {
            return null;
        }
        return convertirAForm(unidad);
    }

    private UnidadForm convertirAForm(Unidad unidad) {
        UnidadForm formulario = new UnidadForm();
        formulario.setId(unidad.getId());
        if (unidad.getCondominio() != null) {
            formulario.setNombreCondominio(unidad.getCondominio().getNombre());
            formulario.setCondominioId(unidad.getCondominio().getId());
        }
        formulario.setNumeroUnidad(unidad.getNumeroUnidad());
        formulario.setTorre(unidad.getTorre());
        formulario.setPiso(unidad.getPiso());
        formulario.setArea(unidad.getArea());
        return formulario;
    }

    private Unidad actualizarUnidadExistente(Unidad unidad, UnidadForm formulario) {
        Condominio condominio = obtenerCondominio(formulario.getNombreCondominio());

        if (formulario.getPiso() > condominio.getPisosPorTorre()) {
            throw new IllegalArgumentException("El piso ingresado (" + formulario.getPiso()
                    + ") supera el máximo de pisos por torre del condominio (" + condominio.getPisosPorTorre() + ").");
        }

        unidad.setNumeroUnidad(formulario.getNumeroUnidad());
        unidad.setTorre(formulario.getTorre());
        unidad.setPiso(formulario.getPiso());
        unidad.setArea(formulario.getArea());
        unidad.setCondominio(condominio);
        return unidadRepository.save(unidad);
    }

    private Unidad crearNuevaUnidad(UnidadForm formulario) {
        Condominio condominio = obtenerCondominio(formulario.getNombreCondominio());

        if (formulario.getPiso() > condominio.getPisosPorTorre()) {
            throw new IllegalArgumentException("El piso ingresado (" + formulario.getPiso()
                    + ") supera el máximo de pisos por torre del condominio (" + condominio.getPisosPorTorre() + ").");
        }

        Unidad nueva = new Unidad();
        nueva.setCondominio(condominio);
        nueva.setNumeroUnidad(formulario.getNumeroUnidad());
        nueva.setTorre(formulario.getTorre());
        nueva.setPiso(formulario.getPiso());
        nueva.setArea(formulario.getArea());
        return unidadRepository.save(nueva);
    }

    private Condominio obtenerCondominio(String nombreCondominio) {
        return condominioRepository.buscarPorNombre(nombreCondominio)
                .orElseThrow(() -> new IllegalArgumentException("Debe seleccionar un condominio valido."));
    }

    private void validarUnidad(UnidadForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de la unidad es obligatorio.");
        }
        if (formulario.getNombreCondominio() == null || formulario.getNombreCondominio().isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar un condominio.");
        }
        if (formulario.getNumeroUnidad() == null || formulario.getNumeroUnidad().isBlank()) {
            throw new IllegalArgumentException("El numero de unidad no puede estar vacio.");
        }
        if (formulario.getTorre() == null || formulario.getTorre().isBlank()) {
            throw new IllegalArgumentException("La torre no puede estar vacia.");
        }
        if (formulario.getPiso() <= 0) {
            throw new IllegalArgumentException("El piso debe ser mayor a cero.");
        }
        if (formulario.getArea() <= 0) {
            throw new IllegalArgumentException("El area debe ser mayor a cero.");
        }
    }
}
