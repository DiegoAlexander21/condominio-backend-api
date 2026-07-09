package pe.edu.utp.condominio.api.dominios.areascomunes.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.AreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.response.AreaComunResponse;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;

@Service
public class AreaComunService {

    private final AreaComunRepository areaComunRepository;
    private final CondominioRepository condominioRepository;

    public AreaComunService(AreaComunRepository areaComunRepository,
            CondominioRepository condominioRepository) {
        this.areaComunRepository = areaComunRepository;
        this.condominioRepository = condominioRepository;
    }

    @Transactional
    public synchronized AreaComunResponse registrarOActualizarArea(AreaComunForm formulario) {
        validarArea(formulario);

        Condominio condominio = condominioRepository.findById(formulario.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("Condominio no encontrado."));

        AreaComun existente = areaComunRepository.buscarPorNombre(condominio.getId(), formulario.getNombre().trim());
        if (existente != null && !existente.getId().equals(formulario.getId())) {
            throw new IllegalArgumentException(
                    "Ya existe un área común con el nombre '" + formulario.getNombre() + "' en este condominio.");
        }

        AreaComun areaComun = obtenerAreaParaRegistro(formulario, condominio.getId());
        areaComun.setCondominio(condominio);
        areaComun.setNombre(formulario.getNombre().trim());
        areaComun.setCapacidad(formulario.getCapacidad());
        areaComun.setHoraInicio(formulario.getHoraInicio());
        areaComun.setHoraFin(formulario.getHoraFin());
        areaComun.setNormasUso(normalizarTexto(formulario.getNormasUso()));

        AreaComun guardada = areaComunRepository.save(areaComun);
        return convertirAreaResponse(guardada);
    }

    public synchronized List<AreaComunResponse> listarPorCondominio(Long condominioId) {
        if (condominioId == null) {
            throw new IllegalArgumentException("Debe seleccionar un condominio valido.");
        }
        return areaComunRepository.listarPorCondominio(condominioId).stream()
                .map(this::convertirAreaResponse)
                .collect(Collectors.toList());
    }

    public synchronized Page<AreaComunResponse> listarPorCondominioPaginado(Long condominioId, Pageable paginacion) {
        if (condominioId == null) {
            throw new IllegalArgumentException("Debe seleccionar un condominio valido.");
        }
        return areaComunRepository.listarPorCondominioPaginado(condominioId, paginacion)
                .map(this::convertirAreaResponse);
    }

    public synchronized List<AreaComunResponse> obtenerTodasLasAreasComunes() {
        return areaComunRepository.listarTodosConCondominio().stream()
                .map(this::convertirAreaResponse)
                .collect(Collectors.toList());
    }

    public synchronized Page<AreaComunResponse> obtenerTodasLasAreasComunesPaginado(Pageable paginacion) {
        return areaComunRepository.listarTodosConCondominioPaginado(paginacion)
                .map(this::convertirAreaResponse);
    }

    @Transactional
    public synchronized void eliminarArea(Long id) {
        if (!areaComunRepository.existsById(id)) {
            throw new IllegalArgumentException("El área común no existe.");
        }
        areaComunRepository.deleteById(id);
    }

    public synchronized AreaComunForm obtenerFormularioArea(Long id) {
        AreaComun area = areaComunRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El área común no existe."));

        AreaComunForm formulario = new AreaComunForm();
        formulario.setId(area.getId());
        formulario.setCondominioId(area.getCondominio().getId());
        formulario.setNombre(area.getNombre());
        formulario.setCapacidad(area.getCapacidad());
        formulario.setHoraInicio(area.getHoraInicio());
        formulario.setHoraFin(area.getHoraFin());
        formulario.setNormasUso(area.getNormasUso());
        return formulario;
    }

    private AreaComun obtenerAreaParaRegistro(AreaComunForm form, Long condominioId) {
        if (form.getId() != null) {
            return areaComunRepository.findById(form.getId())
                    .orElseThrow(() -> new IllegalArgumentException("El área común no existe."));
        }
        return new AreaComun();
    }

    private AreaComunResponse convertirAreaResponse(AreaComun areaComun) {
        return new AreaComunResponse(areaComun.getId(),
                areaComun.getCondominio() != null ? areaComun.getCondominio().getId() : null,
                areaComun.getNombre(),
                areaComun.getCapacidad(),
                areaComun.getHoraInicio(),
                areaComun.getHoraFin(),
                areaComun.getNormasUso());
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private void validarArea(AreaComunForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario del area comun es obligatorio.");
        }
        if (formulario.getCondominioId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un condominio.");
        }
        if (formulario.getNombre() == null || formulario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del area comun es obligatorio.");
        }
        if (formulario.getCapacidad() == null || formulario.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
        }
        if (formulario.getHoraInicio() == null || formulario.getHoraFin() == null) {
            throw new IllegalArgumentException("Debe indicar el horario disponible.");
        }
    }
}
