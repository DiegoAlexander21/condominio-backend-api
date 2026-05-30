package pe.edu.utp.condominio.api.dominios.reportes.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    public ReporteDashboardResponse generarReporte(int limite) {
        int limiteSeguro = limite > 0 ? limite : 5;
        List<IncidenciaFrecuenteResponse> incidenciasFrecuentes = obtenerIncidenciasFrecuentes(limiteSeguro);
        List<AreaGastoResponse> areasConMayorGasto = obtenerAreasConMayorGasto(limiteSeguro);
        List<UnidadMorosaResponse> unidadesMorosas = obtenerUnidadesMorosas(limiteSeguro);
        List<UnidadMorosaResponse> unidadesConMayorDeuda = obtenerUnidadesConMayorDeuda(limiteSeguro);

        return new ReporteDashboardResponse(incidenciasFrecuentes, areasConMayorGasto, unidadesMorosas, unidadesConMayorDeuda);
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
                .map(entrada -> {
                    AreaComun area = areas.get(entrada.getKey());
                    String nombre = area != null ? area.getNombre() : "Area sin nombre";
                    return new IncidenciaFrecuenteResponse(entrada.getKey(), nombre, entrada.getValue());
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
                .map(entrada -> {
                    AreaComun area = areas.get(entrada.getKey());
                    String nombre = area != null ? area.getNombre() : "Area sin nombre";
                    return new AreaGastoResponse(entrada.getKey(), nombre, entrada.getValue());
                })
                .sorted(Comparator.comparingDouble(AreaGastoResponse::getMontoTotal).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    private List<UnidadMorosaResponse> obtenerUnidadesMorosas(int limite) {
        LocalDate hoy = LocalDate.now();
        Map<Long, List<EstadoCuenta>> estadosPorUnidad = estadoCuentaRepository.findAll().stream()
                .filter(estado -> estado.getUnidad() != null && estado.getSaldo() > 0 &&
                        (estado.getFechaVencimiento() == null || !estado.getFechaVencimiento().isAfter(hoy)))
                .collect(Collectors.groupingBy(estado -> estado.getUnidad().getId()));

        return estadosPorUnidad.entrySet().stream()
                .map(entrada -> {
                    List<EstadoCuenta> estados = entrada.getValue();
                    Unidad unidad = estados.get(0).getUnidad();
                    String nombre = unidad != null ? formatearUnidad(unidad) : "Unidad sin nombre";
                    
                    double totalSaldo = estados.stream().mapToDouble(EstadoCuenta::getSaldo).sum();
                    LocalDate maxPeriodo = estados.stream()
                            .map(EstadoCuenta::getPeriodo)
                            .max(LocalDate::compareTo)
                            .orElse(null);

                    return new UnidadMorosaResponse(entrada.getKey(), nombre, totalSaldo, maxPeriodo);
                })
                .sorted(Comparator.comparingDouble(UnidadMorosaResponse::getSaldo).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    private List<UnidadMorosaResponse> obtenerUnidadesConMayorDeuda(int limite) {
        Map<Long, List<EstadoCuenta>> estadosPorUnidad = estadoCuentaRepository.findAll().stream()
                .filter(estado -> estado.getUnidad() != null && estado.getSaldo() > 0)
                .collect(Collectors.groupingBy(estado -> estado.getUnidad().getId()));

        return estadosPorUnidad.entrySet().stream()
                .map(entrada -> {
                    List<EstadoCuenta> estados = entrada.getValue();
                    Unidad unidad = estados.get(0).getUnidad();
                    String nombre = unidad != null ? formatearUnidad(unidad) : "Unidad sin nombre";
                    
                    double totalSaldo = estados.stream().mapToDouble(EstadoCuenta::getSaldo).sum();
                    LocalDate maxPeriodo = estados.stream()
                            .map(EstadoCuenta::getPeriodo)
                            .max(LocalDate::compareTo)
                            .orElse(null);

                    return new UnidadMorosaResponse(entrada.getKey(), nombre, totalSaldo, maxPeriodo);
                })
                .sorted(Comparator.comparingDouble(UnidadMorosaResponse::getSaldo).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    private String formatearUnidad(Unidad unidad) {
        String torre = unidad.getTorre() != null ? unidad.getTorre().trim() : "";
        String numero = unidad.getNumeroUnidad() != null ? unidad.getNumeroUnidad().trim() : "";
        
        String nombreTorre = "";
        if (!torre.isEmpty()) {
            nombreTorre = torre.toLowerCase().startsWith("torre") ? torre : "Torre " + torre;
        }
        
        if (nombreTorre.isEmpty()) {
            return numero.isEmpty() ? "Unidad" : "Unidad " + numero;
        }
        if (numero.isEmpty()) {
            return nombreTorre;
        }
        return nombreTorre + " - " + numero;
    }
}

