package pe.edu.utp.condominio.api.dominios.calificaciones.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.response.EstadoAreaResponse;
import pe.edu.utp.condominio.api.dominios.calificaciones.models.EstadoArea;
import pe.edu.utp.condominio.api.dominios.calificaciones.repositories.CalificacionAreaRepository;
import pe.edu.utp.condominio.api.dominios.calificaciones.repositories.EstadoAreaRepository;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.IncidenciaRepository;
import pe.edu.utp.condominio.api.dominios.saludambiental.enums.ResultadoChecklist;
import pe.edu.utp.condominio.api.dominios.saludambiental.repositories.EvaluacionChecklistRepository;

@Service
public class EstadoAreaService {

    private final EstadoAreaRepository estadoRepository;
    private final CalificacionAreaRepository calificacionRepository;
    private final AreaComunRepository areaComunRepository;
    private final IncidenciaRepository incidenciaRepository;
    private final EvaluacionChecklistRepository evaluacionRepository;

    public EstadoAreaService(
            EstadoAreaRepository estadoRepository,
            CalificacionAreaRepository calificacionRepository,
            AreaComunRepository areaComunRepository,
            IncidenciaRepository incidenciaRepository,
            EvaluacionChecklistRepository evaluacionRepository) {
        this.estadoRepository = estadoRepository;
        this.calificacionRepository = calificacionRepository;
        this.areaComunRepository = areaComunRepository;
        this.incidenciaRepository = incidenciaRepository;
        this.evaluacionRepository = evaluacionRepository;
    }

    @Transactional
    public EstadoAreaResponse actualizarEstadoAutomatico(Long areaId) {
        AreaComun area = areaComunRepository.findById(areaId)
                .orElseThrow(() -> new IllegalArgumentException("Área común no encontrada"));

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
