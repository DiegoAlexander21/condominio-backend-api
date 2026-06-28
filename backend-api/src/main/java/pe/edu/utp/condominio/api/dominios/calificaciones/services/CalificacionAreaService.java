package pe.edu.utp.condominio.api.dominios.calificaciones.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.request.CalificacionForm;
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.response.CalificacionResponse;
import pe.edu.utp.condominio.api.dominios.calificaciones.models.CalificacionArea;
import pe.edu.utp.condominio.api.dominios.calificaciones.repositories.CalificacionAreaRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class CalificacionAreaService {

    private final CalificacionAreaRepository calificacionRepository;
    private final AreaComunRepository areaComunRepository;
    private final UnidadRepository unidadRepository;

    public CalificacionAreaService(
            CalificacionAreaRepository calificacionRepository,
            AreaComunRepository areaComunRepository,
            UnidadRepository unidadRepository) {
        this.calificacionRepository = calificacionRepository;
        this.areaComunRepository = areaComunRepository;
        this.unidadRepository = unidadRepository;
    }

    @Transactional
    public CalificacionResponse registrarCalificacion(CalificacionForm formulario) {
        AreaComun area = areaComunRepository.findById(formulario.getAreaComunId())
                .orElseThrow(() -> new IllegalArgumentException("Área común no encontrada"));

        Unidad unidad = unidadRepository.findById(formulario.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("Unidad no encontrada"));

        CalificacionArea calificacion = new CalificacionArea();
        calificacion.setAreaComun(area);
        calificacion.setUnidad(unidad);
        calificacion.setPuntaje(formulario.getPuntaje());
        calificacion.setComentario(formulario.getComentario());

        CalificacionArea guardada = calificacionRepository.save(calificacion);

        return mapearCalificacionAResponse(guardada);
    }

    public List<CalificacionResponse> listarCalificacionesPorArea(Long areaId) {
        return calificacionRepository.listarPorArea(areaId).stream()
                .map(this::mapearCalificacionAResponse)
                .collect(Collectors.toList());
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
}
