package pe.edu.utp.condominio.api.dominios.reportes.services;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.incidencias.models.Incidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.models.IncidenciaAreaComun;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.IncidenciaRepository;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.IncidenciaFrecuenteResponse;

@Service
public class ReporteIncidenciasService {

    private final IncidenciaRepository incidenciaRepository;
    private final AreaComunRepository areaComunRepository;

    public ReporteIncidenciasService(IncidenciaRepository incidenciaRepository,
            AreaComunRepository areaComunRepository) {
        this.incidenciaRepository = incidenciaRepository;
        this.areaComunRepository = areaComunRepository;
    }

    @Transactional(readOnly = true)
    public List<IncidenciaFrecuenteResponse> obtenerIncidenciasFrecuentes(int limite) {
        Map<Long, AreaComun> areas = areaComunRepository.findAll().stream()
                .collect(Collectors.toMap(AreaComun::getId, area -> area));

        Map<Long, Long> conteo = new HashMap<>();
        for (Incidencia incidencia : incidenciaRepository.findAll()) {
            if (!(incidencia instanceof IncidenciaAreaComun incidenciaArea)) {
                continue;
            }
            Long areaId = incidenciaArea.getAreaComun().getId();
            conteo.put(areaId, conteo.getOrDefault(areaId, 0L) + 1);
        }

        return conteo.entrySet().stream()
                .map(entrada -> {
                    AreaComun area = areas.get(entrada.getKey());
                    String nombre = area != null ? area.getNombre() : "Area sin nombre";
                    return new IncidenciaFrecuenteResponse(entrada.getKey(), nombre, entrada.getValue());
                })
                .sorted(Comparator.comparingLong(IncidenciaFrecuenteResponse::getTotalIncidencias).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }
}
