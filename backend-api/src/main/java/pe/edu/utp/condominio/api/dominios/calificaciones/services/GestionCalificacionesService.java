package pe.edu.utp.condominio.api.dominios.calificaciones.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.request.CalificacionForm;
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.response.CalificacionResponse;
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.response.EstadoAreaResponse;
import pe.edu.utp.condominio.api.dominios.calificaciones.models.CalificacionArea;
import pe.edu.utp.condominio.api.dominios.calificaciones.models.EstadoArea;
import pe.edu.utp.condominio.api.dominios.calificaciones.repositories.CalificacionAreaRepository;
import pe.edu.utp.condominio.api.dominios.calificaciones.repositories.EstadoAreaRepository;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.IncidenciaRepository;
import pe.edu.utp.condominio.api.dominios.saludambiental.enums.ResultadoChecklist;
import pe.edu.utp.condominio.api.dominios.saludambiental.repositories.EvaluacionChecklistRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class GestionCalificacionesService {

    private final CalificacionAreaRepository calificacionRepository;
    private final EstadoAreaRepository estadoRepository;
    private final AreaComunRepository areaComunRepository;
    private final UnidadRepository unidadRepository;
    private final IncidenciaRepository incidenciaRepository;
    private final EvaluacionChecklistRepository evaluacionRepository;

    public GestionCalificacionesService(
            CalificacionAreaRepository calificacionRepository,
            EstadoAreaRepository estadoRepository,
            AreaComunRepository areaComunRepository,
            UnidadRepository unidadRepository,
            IncidenciaRepository incidenciaRepository,
            EvaluacionChecklistRepository evaluacionRepository) {
        this.calificacionRepository = calificacionRepository;
        this.estadoRepository = estadoRepository;
        this.areaComunRepository = areaComunRepository;
        this.unidadRepository = unidadRepository;
        this.incidenciaRepository = incidenciaRepository;
        this.evaluacionRepository = evaluacionRepository;
    }

    @Transactional
    public CalificacionResponse registrarCalificacion(CalificacionForm formulario) {
        AreaComun area = areaComunRepository.findById(formulario.getAreaComunId())
                .orElseThrow(() -> new RuntimeException("Área común no encontrada"));

        Unidad unidad = unidadRepository.findById(formulario.getUnidadId())
                .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));

        CalificacionArea calificacion = new CalificacionArea();
        calificacion.setAreaComun(area);
        calificacion.setUnidad(unidad);
        calificacion.setPuntaje(formulario.getPuntaje());
        calificacion.setComentario(formulario.getComentario());

        CalificacionArea guardada = calificacionRepository.save(calificacion);
        
        actualizarEstadoAutomatico(area.getId());

        return mapearCalificacionAResponse(guardada);
    }

    @Transactional
    public EstadoAreaResponse actualizarEstadoAutomatico(Long areaId) {
        AreaComun area = areaComunRepository.findById(areaId)
                .orElseThrow(() -> new RuntimeException("Área común no encontrada"));

        Double promedio = calificacionRepository.obtenerPromedioPorArea(areaId);
        long totalIncidencias = incidenciaRepository.contarPorArea(areaId);
        long totalNoAprobados = evaluacionRepository.contarPorAreaYResultado(areaId, ResultadoChecklist.NO_PASA);

        EstadoArea estado = estadoRepository.listarPorArea(areaId).stream().findFirst().orElse(new EstadoArea());
        estado.setAreaComun(area);
        estado.setCalificacionPromedio(promedio != null ? promedio : 0.0);
        estado.setTotalIncidencias((int) totalIncidencias);
        estado.setTotalChecklistsNoAprobados((int) totalNoAprobados);

        EstadoArea guardado = estadoRepository.save(estado);
        return mapearEstadoAResponse(guardado);
    }

    public List<CalificacionResponse> listarCalificacionesPorArea(Long areaId) {
        return calificacionRepository.listarPorArea(areaId).stream()
                .map(this::mapearCalificacionAResponse)
                .collect(Collectors.toList());
    }

    public EstadoAreaResponse obtenerEstadoActual(Long areaId) {
        EstadoArea estado = estadoRepository.listarPorArea(areaId).stream().findFirst()
                .orElseGet(() -> {
                    return new EstadoArea();
                });
        
        if (estado.getId() == null) {
            return actualizarEstadoAutomatico(areaId);
        }
        
        return mapearEstadoAResponse(estado);
    }

    private CalificacionResponse mapearCalificacionAResponse(CalificacionArea entidad) {
        String identificadorUnidad = "Unidad " + entidad.getUnidad().getNumeroUnidad() + " - Torre " + entidad.getUnidad().getTorre();
        return new CalificacionResponse(
                entidad.getId(),
                entidad.getAreaComun().getNombre(),
                identificadorUnidad,
                entidad.getPuntaje(),
                entidad.getComentario(),
                entidad.getFechaRegistro()
        );
    }

    private EstadoAreaResponse mapearEstadoAResponse(EstadoArea entidad) {
        return new EstadoAreaResponse(
                entidad.getId(),
                entidad.getAreaComun() != null ? entidad.getAreaComun().getNombre() : "N/A",
                entidad.getCalificacionPromedio(),
                entidad.getTotalIncidencias(),
                entidad.getTotalChecklistsNoAprobados(),
                entidad.getFechaCalculo()
        );
    }
}

