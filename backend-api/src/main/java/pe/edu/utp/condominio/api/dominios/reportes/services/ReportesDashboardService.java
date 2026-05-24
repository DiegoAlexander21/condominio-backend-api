package pe.edu.utp.condominio.api.dominios.reportes.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Gasto;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EstadoCuenta;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.EstadoCuentaRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.GastoRepository;
import pe.edu.utp.condominio.api.dominios.incidencias.models.Incidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.models.IncidenciaAreaComun;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.IncidenciaRepository;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.AreaGastoResponse;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.IncidenciaFrecuenteResponse;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.ReporteDashboardResponse;
import pe.edu.utp.condominio.api.dominios.reportes.dto.response.UnidadMorosaResponse;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;

@Service
public class ReportesDashboardService {

    private final IncidenciaRepository incidenciaRepository;
    private final AreaComunRepository areaComunRepository;
    private final GastoRepository gastoRepository;
    private final EstadoCuentaRepository estadoCuentaRepository;

    public ReportesDashboardService(IncidenciaRepository incidenciaRepository,
            AreaComunRepository areaComunRepository,
            GastoRepository gastoRepository,
            EstadoCuentaRepository estadoCuentaRepository) {
        this.incidenciaRepository = incidenciaRepository;
        this.areaComunRepository = areaComunRepository;
        this.gastoRepository = gastoRepository;
        this.estadoCuentaRepository = estadoCuentaRepository;
    }

    public ReporteDashboardResponse generarReporte(int limite) {
        int limiteSeguro = limite > 0 ? limite : 5;
        List<IncidenciaFrecuenteResponse> incidenciasFrecuentes = obtenerIncidenciasFrecuentes(limiteSeguro);
        List<AreaGastoResponse> areasConMayorGasto = obtenerAreasConMayorGasto(limiteSeguro);
        List<UnidadMorosaResponse> unidadesMorosas = obtenerUnidadesMorosas(limiteSeguro);

        return new ReporteDashboardResponse(incidenciasFrecuentes, areasConMayorGasto, unidadesMorosas);
    }

    private List<IncidenciaFrecuenteResponse> obtenerIncidenciasFrecuentes(int limite) {
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
                .map(entry -> {
                    AreaComun area = areas.get(entry.getKey());
                    String nombre = area != null ? area.getNombre() : "Area sin nombre";
                    return new IncidenciaFrecuenteResponse(entry.getKey(), nombre, entry.getValue());
                })
                .sorted(Comparator.comparingLong(IncidenciaFrecuenteResponse::getTotalIncidencias).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    private List<AreaGastoResponse> obtenerAreasConMayorGasto(int limite) {
        Map<Long, AreaComun> areas = areaComunRepository.findAll().stream()
                .collect(Collectors.toMap(AreaComun::getId, area -> area));

        Map<Long, Double> totales = new HashMap<>();
        for (Gasto gasto : gastoRepository.findAll()) {
            if (gasto.getIncidencia() == null) {
                continue;
            }
            if (!(gasto.getIncidencia() instanceof IncidenciaAreaComun incidenciaArea)) {
                continue;
            }
            Long areaId = incidenciaArea.getAreaComun().getId();
            double acumulado = totales.getOrDefault(areaId, 0.0);
            totales.put(areaId, acumulado + gasto.getMontoTotal());
        }

        return totales.entrySet().stream()
                .map(entry -> {
                    AreaComun area = areas.get(entry.getKey());
                    String nombre = area != null ? area.getNombre() : "Area sin nombre";
                    return new AreaGastoResponse(entry.getKey(), nombre, entry.getValue());
                })
                .sorted(Comparator.comparingDouble(AreaGastoResponse::getMontoTotal).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    private List<UnidadMorosaResponse> obtenerUnidadesMorosas(int limite) {
        Map<Long, EstadoCuenta> ultimoEstado = new HashMap<>();
        for (EstadoCuenta estado : estadoCuentaRepository.findAll()) {
            if (estado.getUnidad() == null) {
                continue;
            }
            Long unidadId = estado.getUnidad().getId();
            EstadoCuenta actual = ultimoEstado.get(unidadId);
            if (actual == null || esPeriodoPosterior(estado.getPeriodo(), actual.getPeriodo())) {
                ultimoEstado.put(unidadId, estado);
            }
        }

        return ultimoEstado.values().stream()
                .filter(estado -> estado.getSaldo() > 0)
                .map(estado -> {
                    Unidad unidad = estado.getUnidad();
                    String nombre = unidad != null ? formatearUnidad(unidad) : "Unidad sin nombre";
                    return new UnidadMorosaResponse(unidad != null ? unidad.getId() : null,
                            nombre, estado.getSaldo(), estado.getPeriodo());
                })
                .sorted(Comparator.comparingDouble(UnidadMorosaResponse::getSaldo).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    private boolean esPeriodoPosterior(LocalDate candidato, LocalDate actual) {
        if (candidato == null) {
            return false;
        }
        if (actual == null) {
            return true;
        }
        return candidato.isAfter(actual);
    }

    private String formatearUnidad(Unidad unidad) {
        String torre = unidad.getTorre() != null ? unidad.getTorre().trim() : "";
        String numero = unidad.getNumeroUnidad() != null ? unidad.getNumeroUnidad().trim() : "";
        if (torre.isEmpty()) {
            return numero.isEmpty() ? "Unidad" : "Unidad " + numero;
        }
        if (numero.isEmpty()) {
            return "Torre " + torre;
        }
        return "Torre " + torre + " - Unidad " + numero;
    }
}

