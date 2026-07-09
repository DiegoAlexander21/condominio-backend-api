package pe.edu.utp.condominio.api.dominios.calificaciones.services;

import java.util.List;
import java.util.stream.Collectors;

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

        double ratingBase = 5.0;
        ratingBase -= (totalIncidencias * 0.5);
        ratingBase -= (totalNoAprobados * 1.0);
        ratingBase = Math.max(1.0, ratingBase);

        double calificacionFinal = (promedio != null && promedio > 0) ? (ratingBase + promedio) / 2.0 : ratingBase;
        calificacionFinal = Math.round(calificacionFinal * 10.0) / 10.0;

        EstadoArea estado = estadoRepository.listarPorArea(areaId).stream().findFirst().orElse(new EstadoArea());
        estado.setAreaComun(area);
        estado.setCalificacionPromedio(calificacionFinal);
        estado.setTotalIncidencias((int) totalIncidencias);
        estado.setTotalChecklistsNoAprobados((int) totalNoAprobados);

        EstadoArea guardado = estadoRepository.save(estado);
        return mapearEstadoAResponse(guardado);
    }

    @Transactional
    public EstadoAreaResponse obtenerEstadoActual(Long areaId) {
        return actualizarEstadoAutomatico(areaId);
    }

    private EstadoAreaResponse mapearEstadoAResponse(EstadoArea entidad) {
        Long areaId = entidad.getAreaComun() != null ? entidad.getAreaComun().getId() : null;
        Long condominioId = (entidad.getAreaComun() != null && entidad.getAreaComun().getCondominio() != null) ? entidad.getAreaComun().getCondominio().getId() : null;
        String condominioNombre = (entidad.getAreaComun() != null && entidad.getAreaComun().getCondominio() != null) ? entidad.getAreaComun().getCondominio().getNombre() : "N/A";
        
        return new EstadoAreaResponse(
                entidad.getId(),
                areaId,
                condominioId,
                condominioNombre,
                entidad.getAreaComun() != null ? entidad.getAreaComun().getNombre() : "N/A",
                entidad.getCalificacionPromedio(),
                entidad.getTotalIncidencias(),
                entidad.getTotalChecklistsNoAprobados(),
                entidad.getFechaCalculo()
        );
    }

    @Transactional
    public List<EstadoAreaResponse> obtenerRankingAreas() {
        return areaComunRepository.findAll().stream()
                .map(area -> obtenerEstadoActual(area.getId()))
                .sorted((a, b) -> Double.compare(a.getCalificacionPromedio(), b.getCalificacionPromedio()))
                .collect(Collectors.toList());
    }
}
