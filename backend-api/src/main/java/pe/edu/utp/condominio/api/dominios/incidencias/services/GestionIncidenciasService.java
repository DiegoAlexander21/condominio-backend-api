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
import pe.edu.utp.condominio.api.dominios.incidencias.models.IncidenciaAreaComun;
import pe.edu.utp.condominio.api.dominios.incidencias.models.IncidenciaUnidad;
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
    public synchronized IncidenciaResponse registrarIncidencia(IncidenciaForm formulario) {
        validarIncidencia(formulario);

        Incidencia incidencia;

        if (formulario.getAreaComunId() != null) {
            AreaComun areaComun = areaComunRepository.findById(formulario.getAreaComunId())
                    .orElseThrow(() -> new IllegalArgumentException("El area comun no existe."));
            IncidenciaAreaComun incidenciaArea = new IncidenciaAreaComun();
            incidenciaArea.setAreaComun(areaComun);
            incidencia = incidenciaArea;
        } else if (formulario.getUnidadId() != null) {
            Unidad unidad = unidadRepository.findById(formulario.getUnidadId())
                    .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));
            IncidenciaUnidad incidenciaUnidad = new IncidenciaUnidad();
            incidenciaUnidad.setUnidad(unidad);
            incidencia = incidenciaUnidad;
        } else {
            throw new IllegalArgumentException("Debe indicar el area afectada o la unidad.");
        }

        incidencia.setDescripcion(formulario.getDescripcion().trim());
        incidencia.setGravedad(formulario.getGravedad());
        incidencia.setCausa(formulario.getCausa());
        incidencia.setEstado(EstadoIncidencia.REGISTRADO);
        incidencia.setResponsableAtencion("Sin asignar");

        Incidencia guardada = incidenciaRepository.save(incidencia);
        return convertirIncidenciaResponse(guardada);
    }

    @Transactional
    public synchronized IncidenciaResponse actualizarEstado(ActualizacionIncidenciaForm formulario) {
        validarActualizacion(formulario);

        Incidencia incidencia = incidenciaRepository.findById(formulario.getIncidenciaId())
                .orElseThrow(() -> new IllegalArgumentException("La incidencia no existe."));

        incidencia.setEstado(formulario.getEstado());
        incidencia.setResponsableAtencion(normalizarTexto(formulario.getResponsableAtencion()));

        Incidencia actualizada = incidenciaRepository.save(incidencia);
        return convertirIncidenciaResponse(actualizada);
    }

    @Transactional(readOnly = true)
    public synchronized List<IncidenciaResponse> listarPorEstado(EstadoIncidencia estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Debe seleccionar un estado.");
        }
        return incidenciaRepository.listarPorEstado(estado).stream()
                .map(this::convertirIncidenciaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public synchronized List<IncidenciaResponse> listarTodas() {
        return incidenciaRepository.findAll().stream()
                .map(this::convertirIncidenciaResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public synchronized EvidenciaIncidenciaResponse registrarEvidencia(EvidenciaIncidenciaForm formulario) {
        validarEvidencia(formulario);

        Incidencia incidencia = incidenciaRepository.findById(formulario.getIncidenciaId())
                .orElseThrow(() -> new IllegalArgumentException("La incidencia no existe."));

        EvidenciaIncidencia evidencia = new EvidenciaIncidencia();
        evidencia.setIncidencia(incidencia);
        evidencia.setUrlArchivo(formulario.getUrlArchivo().trim());

        EvidenciaIncidencia guardada = evidenciaIncidenciaRepository.save(evidencia);
        return convertirEvidenciaResponse(guardada);
    }

    @Transactional(readOnly = true)
    public synchronized List<EvidenciaIncidenciaResponse> listarEvidencias(Long incidenciaId) {
        if (incidenciaId == null) {
            throw new IllegalArgumentException("Debe seleccionar una incidencia valida.");
        }
        return evidenciaIncidenciaRepository.listarPorIncidencia(incidenciaId).stream()
                .map(this::convertirEvidenciaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public synchronized Incidencia obtenerPorId(Long incidenciaId) {
        return incidenciaRepository.findById(incidenciaId).orElse(null);
    }

    private void validarIncidencia(IncidenciaForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de incidencia es obligatorio.");
        }
        if (formulario.getAreaComunId() == null && formulario.getUnidadId() == null) {
            throw new IllegalArgumentException("Debe indicar el area afectada o la unidad.");
        }
        if (formulario.getDescripcion() == null || formulario.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        if (formulario.getGravedad() == null) {
            throw new IllegalArgumentException("La gravedad es obligatoria.");
        }
        if (formulario.getCausa() == null) {
            throw new IllegalArgumentException("La causa es obligatoria.");
        }
    }

    private void validarActualizacion(ActualizacionIncidenciaForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de actualizacion es obligatorio.");
        }
        if (formulario.getIncidenciaId() == null) {
            throw new IllegalArgumentException("La incidencia es obligatoria.");
        }
        if (formulario.getEstado() == null) {
            throw new IllegalArgumentException("El estado es obligatorio.");
        }
    }

    private void validarEvidencia(EvidenciaIncidenciaForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de evidencia es obligatorio.");
        }
        if (formulario.getIncidenciaId() == null) {
            throw new IllegalArgumentException("La incidencia es obligatoria.");
        }
        if (formulario.getUrlArchivo() == null || formulario.getUrlArchivo().isBlank()) {
            throw new IllegalArgumentException("La URL del archivo es obligatoria.");
        }
    }

    private IncidenciaResponse convertirIncidenciaResponse(Incidencia incidencia) {
        Long areaComunId = null;
        Long unidadId = null;
        String lugarAfectado = null;
        Long condominioId = null;
        String torre = null;

        if (incidencia instanceof IncidenciaAreaComun area) {
            areaComunId = area.getAreaComun() != null ? area.getAreaComun().getId() : null;
            lugarAfectado = area.getAreaComun() != null ? "Área Común: " + area.getAreaComun().getNombre() : "Área Común";
            condominioId = area.getAreaComun() != null && area.getAreaComun().getCondominio() != null 
                            ? area.getAreaComun().getCondominio().getId() : null;
        } else if (incidencia instanceof IncidenciaUnidad unidad) {
            unidadId = unidad.getUnidad() != null ? unidad.getUnidad().getId() : null;
            if (unidad.getUnidad() != null) {
                String condNombre = (unidad.getUnidad().getCondominio() != null) ? unidad.getUnidad().getCondominio().getNombre() : "-";
                lugarAfectado = condNombre + " - " + unidad.getUnidad().getTorre() + " - Piso " + unidad.getUnidad().getPiso() + " - Unidad " + unidad.getUnidad().getNumeroUnidad();
            } else {
                lugarAfectado = "Unidad";
            }
            condominioId = unidad.getUnidad() != null && unidad.getUnidad().getCondominio() != null 
                            ? unidad.getUnidad().getCondominio().getId() : null;
            torre = unidad.getUnidad() != null ? unidad.getUnidad().getTorre() : null;
        }

        return new IncidenciaResponse(incidencia.getId(),
                areaComunId,
                unidadId,
                incidencia.getDescripcion(),
                incidencia.getGravedad(),
                incidencia.getCausa(),
                incidencia.getEstado(),
                incidencia.getResponsableAtencion(),
                incidencia.getFechaReporte(),
                incidencia.getFechaActualizacion(),
                lugarAfectado,
                condominioId,
                torre);
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

