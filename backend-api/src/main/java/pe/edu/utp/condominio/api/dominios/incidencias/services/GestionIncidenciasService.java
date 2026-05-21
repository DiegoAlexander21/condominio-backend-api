package pe.edu.utp.condominio.api.dominios.incidencias.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.ActualizacionIncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.EvidenciaIncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.IncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.response.EvidenciaIncidenciaResponse;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.response.IncidenciaResponse;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.EstadoIncidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.models.EvidenciaIncidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.models.Incidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.EvidenciaIncidenciaRepository;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.IncidenciaRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class GestionIncidenciasService {

    private final IncidenciaRepository incidenciaRepository;
    private final EvidenciaIncidenciaRepository evidenciaIncidenciaRepository;
    private final AreaComunRepository areaComunRepository;
    private final UnidadRepository unidadRepository;

    public GestionIncidenciasService(IncidenciaRepository incidenciaRepository,
            EvidenciaIncidenciaRepository evidenciaIncidenciaRepository,
            AreaComunRepository areaComunRepository,
            UnidadRepository unidadRepository) {
        this.incidenciaRepository = incidenciaRepository;
        this.evidenciaIncidenciaRepository = evidenciaIncidenciaRepository;
        this.areaComunRepository = areaComunRepository;
        this.unidadRepository = unidadRepository;
    }

    @Transactional
    public synchronized IncidenciaResponse registrarIncidencia(IncidenciaForm form) {
        validarIncidencia(form);

        Incidencia incidencia = new Incidencia();
        incidencia.setDescripcion(form.getDescripcion().trim());
        incidencia.setGravedad(form.getGravedad());
        incidencia.setCausa(form.getCausa());
        incidencia.setEstado(EstadoIncidencia.REGISTRADO);

        if (form.getAreaComunId() != null) {
            AreaComun areaComun = areaComunRepository.findById(form.getAreaComunId())
                    .orElseThrow(() -> new IllegalArgumentException("El area comun no existe."));
            incidencia.setAreaComun(areaComun);
        }

        if (form.getUnidadId() != null) {
            Unidad unidad = unidadRepository.findById(form.getUnidadId())
                    .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));
            incidencia.setUnidad(unidad);
        }

        Incidencia guardada = incidenciaRepository.save(incidencia);
        return convertirIncidenciaResponse(guardada);
    }

    @Transactional
    public synchronized IncidenciaResponse actualizarEstado(ActualizacionIncidenciaForm form) {
        validarActualizacion(form);

        Incidencia incidencia = incidenciaRepository.findById(form.getIncidenciaId())
                .orElseThrow(() -> new IllegalArgumentException("La incidencia no existe."));

        incidencia.setEstado(form.getEstado());
        incidencia.setResponsableAtencion(normalizarTexto(form.getResponsableAtencion()));

        Incidencia actualizada = incidenciaRepository.save(incidencia);
        return convertirIncidenciaResponse(actualizada);
    }

    public synchronized List<IncidenciaResponse> listarPorEstado(EstadoIncidencia estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Debe seleccionar un estado.");
        }
        return incidenciaRepository.listarPorEstado(estado).stream()
                .map(this::convertirIncidenciaResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public synchronized EvidenciaIncidenciaResponse registrarEvidencia(EvidenciaIncidenciaForm form) {
        validarEvidencia(form);

        Incidencia incidencia = incidenciaRepository.findById(form.getIncidenciaId())
                .orElseThrow(() -> new IllegalArgumentException("La incidencia no existe."));

        EvidenciaIncidencia evidencia = new EvidenciaIncidencia();
        evidencia.setIncidencia(incidencia);
        evidencia.setUrlArchivo(form.getUrlArchivo().trim());

        EvidenciaIncidencia guardada = evidenciaIncidenciaRepository.save(evidencia);
        return convertirEvidenciaResponse(guardada);
    }

    public synchronized List<EvidenciaIncidenciaResponse> listarEvidencias(Long incidenciaId) {
        if (incidenciaId == null) {
            throw new IllegalArgumentException("Debe seleccionar una incidencia valida.");
        }
        return evidenciaIncidenciaRepository.listarPorIncidencia(incidenciaId).stream()
                .map(this::convertirEvidenciaResponse)
                .collect(Collectors.toList());
    }

    private void validarIncidencia(IncidenciaForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de incidencia es obligatorio.");
        }
        if (form.getAreaComunId() == null && form.getUnidadId() == null) {
            throw new IllegalArgumentException("Debe indicar el area afectada o la unidad.");
        }
        if (form.getDescripcion() == null || form.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        if (form.getGravedad() == null) {
            throw new IllegalArgumentException("La gravedad es obligatoria.");
        }
        if (form.getCausa() == null) {
            throw new IllegalArgumentException("La causa es obligatoria.");
        }
    }

    private void validarActualizacion(ActualizacionIncidenciaForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de actualizacion es obligatorio.");
        }
        if (form.getIncidenciaId() == null) {
            throw new IllegalArgumentException("La incidencia es obligatoria.");
        }
        if (form.getEstado() == null) {
            throw new IllegalArgumentException("El estado es obligatorio.");
        }
    }

    private void validarEvidencia(EvidenciaIncidenciaForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de evidencia es obligatorio.");
        }
        if (form.getIncidenciaId() == null) {
            throw new IllegalArgumentException("La incidencia es obligatoria.");
        }
        if (form.getUrlArchivo() == null || form.getUrlArchivo().isBlank()) {
            throw new IllegalArgumentException("La URL del archivo es obligatoria.");
        }
    }

    private IncidenciaResponse convertirIncidenciaResponse(Incidencia incidencia) {
        return new IncidenciaResponse(incidencia.getId(),
                incidencia.getAreaComun() != null ? incidencia.getAreaComun().getId() : null,
                incidencia.getUnidad() != null ? incidencia.getUnidad().getId() : null,
                incidencia.getDescripcion(),
                incidencia.getGravedad(),
                incidencia.getCausa(),
                incidencia.getEstado(),
                incidencia.getResponsableAtencion(),
                incidencia.getFechaReporte(),
                incidencia.getFechaActualizacion());
    }

    private EvidenciaIncidenciaResponse convertirEvidenciaResponse(EvidenciaIncidencia evidencia) {
        return new EvidenciaIncidenciaResponse(evidencia.getId(),
                evidencia.getIncidencia() != null ? evidencia.getIncidencia().getId() : null,
                evidencia.getUrlArchivo(),
                evidencia.getFechaRegistro());
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}

