package pe.edu.utp.condominio.api.dominios.finanzas.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.DistribucionGastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.EstadoCuentaForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.GastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.PagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.DetalleGastoUnidadResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.EstadoCuentaResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.GastoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.PagoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.MetodoDistribucion;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;
import pe.edu.utp.condominio.api.dominios.finanzas.models.DetalleGastoUnidad;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EstadoCuenta;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Gasto;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Pago;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.DetalleGastoUnidadRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.EstadoCuentaRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.GastoRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.PagoRepository;
import pe.edu.utp.condominio.api.dominios.incidencias.models.Incidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.IncidenciaRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class GestionFinanzasService {

    private final GastoRepository gastoRepository;
    private final DetalleGastoUnidadRepository detalleGastoUnidadRepository;
    private final EstadoCuentaRepository estadoCuentaRepository;
    private final PagoRepository pagoRepository;
    private final UnidadRepository unidadRepository;
    private final IncidenciaRepository incidenciaRepository;

    public GestionFinanzasService(GastoRepository gastoRepository,
            DetalleGastoUnidadRepository detalleGastoUnidadRepository,
            EstadoCuentaRepository estadoCuentaRepository,
            PagoRepository pagoRepository,
            UnidadRepository unidadRepository,
            IncidenciaRepository incidenciaRepository) {
        this.gastoRepository = gastoRepository;
        this.detalleGastoUnidadRepository = detalleGastoUnidadRepository;
        this.estadoCuentaRepository = estadoCuentaRepository;
        this.pagoRepository = pagoRepository;
        this.unidadRepository = unidadRepository;
        this.incidenciaRepository = incidenciaRepository;
    }

    @Transactional
    public synchronized GastoResponse registrarGasto(GastoForm form) {
        validarGasto(form);

        Gasto gasto = new Gasto();
        gasto.setDescripcion(form.getDescripcion().trim());
        gasto.setTipoGasto(form.getTipoGasto());
        gasto.setMetodoDistribucion(form.getMetodoDistribucion());
        gasto.setMontoTotal(form.getMontoTotal());

        if (form.getIncidenciaId() != null) {
            Incidencia incidencia = incidenciaRepository.findById(form.getIncidenciaId())
                    .orElseThrow(() -> new IllegalArgumentException("La incidencia no existe."));
            gasto.setIncidencia(incidencia);
        }

        Gasto guardado = gastoRepository.save(gasto);
        return convertirGastoResponse(guardado);
    }

    public synchronized List<GastoResponse> listarGastosPorTipo(TipoGasto tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de gasto.");
        }
        return gastoRepository.listarPorTipo(tipo).stream()
                .map(this::convertirGastoResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public synchronized List<DetalleGastoUnidadResponse> distribuirGasto(DistribucionGastoForm form) {
        validarDistribucion(form);

        Gasto gasto = gastoRepository.findById(form.getGastoId())
                .orElseThrow(() -> new IllegalArgumentException("El gasto no existe."));

        if (!detalleGastoUnidadRepository.listarPorGasto(gasto.getId()).isEmpty()) {
            throw new IllegalArgumentException("El gasto ya tiene distribucion registrada.");
        }

        List<DetalleGastoUnidad> detalles = generarDetalles(gasto, form.getUnidadId());
        List<DetalleGastoUnidad> guardados = detalleGastoUnidadRepository.saveAll(detalles);

        return guardados.stream()
                .map(this::convertirDetalleResponse)
                .collect(Collectors.toList());
    }

    public synchronized List<DetalleGastoUnidadResponse> listarDetallesPorGasto(Long gastoId) {
        if (gastoId == null) {
            throw new IllegalArgumentException("Debe seleccionar un gasto valido.");
        }
        return detalleGastoUnidadRepository.listarPorGasto(gastoId).stream()
                .map(this::convertirDetalleResponse)
                .collect(Collectors.toList());
    }

    public synchronized List<DetalleGastoUnidadResponse> listarDetallesPorUnidad(Long unidadId) {
        if (unidadId == null) {
            throw new IllegalArgumentException("Debe seleccionar una unidad valida.");
        }
        return detalleGastoUnidadRepository.listarPorUnidad(unidadId).stream()
                .map(this::convertirDetalleResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public synchronized EstadoCuentaResponse generarEstadoCuenta(EstadoCuentaForm form) {
        validarEstadoCuenta(form);

        Unidad unidad = unidadRepository.findById(form.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        LocalDate periodo = normalizarPeriodo(form.getPeriodo());

        List<DetalleGastoUnidad> detalles = detalleGastoUnidadRepository.listarPorUnidad(unidad.getId());
        double totalCuotas = 0;
        double totalExtraordinarios = 0;

        for (DetalleGastoUnidad detalle : detalles) {
            if (!esMismoPeriodo(detalle.getFechaRegistro().toLocalDate(), periodo)) {
                continue;
            }
            if (detalle.getGasto().getTipoGasto() == TipoGasto.FIJO) {
                totalCuotas += detalle.getMontoAsignado();
            } else {
                totalExtraordinarios += detalle.getMontoAsignado();
            }
        }

        double totalPagado = calcularPagosPeriodo(unidad.getId(), periodo);
        double saldo = totalCuotas + totalExtraordinarios - totalPagado;

        EstadoCuenta estadoCuenta = estadoCuentaRepository
                .buscarPorUnidadYPeriodo(unidad.getId(), periodo)
                .orElseGet(EstadoCuenta::new);

        estadoCuenta.setUnidad(unidad);
        estadoCuenta.setPeriodo(periodo);
        estadoCuenta.setTotalCuotas(totalCuotas);
        estadoCuenta.setTotalExtraordinarios(totalExtraordinarios);
        estadoCuenta.setTotalPagado(totalPagado);
        estadoCuenta.setSaldo(saldo);

        EstadoCuenta guardado = estadoCuentaRepository.save(estadoCuenta);
        return convertirEstadoCuentaResponse(guardado);
    }

    @Transactional
    public synchronized PagoResponse registrarPago(PagoForm form) {
        validarPago(form);

        Unidad unidad = unidadRepository.findById(form.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        Pago pago = new Pago();
        pago.setUnidad(unidad);
        pago.setMonto(form.getMonto());
        pago.setObservacion(normalizarTexto(form.getObservacion()));

        if (form.getEstadoCuentaId() != null) {
            EstadoCuenta estadoCuenta = estadoCuentaRepository.findById(form.getEstadoCuentaId())
                    .orElseThrow(() -> new IllegalArgumentException("El estado de cuenta no existe."));
            pago.setEstadoCuenta(estadoCuenta);

            estadoCuenta.setTotalPagado(estadoCuenta.getTotalPagado() + form.getMonto());
            estadoCuenta.setSaldo(estadoCuenta.getTotalCuotas() + estadoCuenta.getTotalExtraordinarios()
                    - estadoCuenta.getTotalPagado());
            estadoCuentaRepository.save(estadoCuenta);
        }

        Pago guardado = pagoRepository.save(pago);
        return convertirPagoResponse(guardado);
    }

    public synchronized List<PagoResponse> listarPagosPorUnidad(Long unidadId) {
        if (unidadId == null) {
            throw new IllegalArgumentException("Debe seleccionar una unidad valida.");
        }
        return pagoRepository.listarPorUnidad(unidadId).stream()
                .map(this::convertirPagoResponse)
                .collect(Collectors.toList());
    }

    private List<DetalleGastoUnidad> generarDetalles(Gasto gasto, Long unidadId) {
        List<Unidad> unidades = unidadRepository.findAll();
        if (unidades.isEmpty()) {
            throw new IllegalArgumentException("No existen unidades registradas.");
        }

        if (gasto.getMetodoDistribucion() == MetodoDistribucion.COBRO_DIRECTO) {
            if (unidadId == null) {
                throw new IllegalArgumentException("Debe seleccionar la unidad para cobro directo.");
            }
            Unidad unidad = unidadRepository.findById(unidadId)
                    .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));
            return List.of(crearDetalle(gasto, unidad, gasto.getMontoTotal()));
        }

        if (gasto.getMetodoDistribucion() == MetodoDistribucion.PARTES_IGUALES) {
            double monto = gasto.getMontoTotal() / unidades.size();
            List<DetalleGastoUnidad> detalles = new ArrayList<>();
            for (Unidad unidad : unidades) {
                detalles.add(crearDetalle(gasto, unidad, monto));
            }
            return detalles;
        }

        double totalArea = unidades.stream().mapToDouble(Unidad::getArea).sum();
        if (totalArea <= 0) {
            throw new IllegalArgumentException("El area total de las unidades es invalida.");
        }

        List<DetalleGastoUnidad> detalles = new ArrayList<>();
        for (Unidad unidad : unidades) {
            double proporcion = unidad.getArea() / totalArea;
            double monto = gasto.getMontoTotal() * proporcion;
            detalles.add(crearDetalle(gasto, unidad, monto));
        }

        return detalles;
    }

    private DetalleGastoUnidad crearDetalle(Gasto gasto, Unidad unidad, double montoAsignado) {
        DetalleGastoUnidad detalle = new DetalleGastoUnidad();
        detalle.setGasto(gasto);
        detalle.setUnidad(unidad);
        detalle.setMontoAsignado(montoAsignado);
        return detalle;
    }

    private double calcularPagosPeriodo(Long unidadId, LocalDate periodo) {
        return pagoRepository.listarPorUnidad(unidadId).stream()
                .filter(pago -> esMismoPeriodo(pago.getFechaPago().toLocalDate(), periodo))
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    private boolean esMismoPeriodo(LocalDate fecha, LocalDate periodo) {
        return fecha.getYear() == periodo.getYear() && fecha.getMonth() == periodo.getMonth();
    }

    private LocalDate normalizarPeriodo(LocalDate periodo) {
        return periodo.withDayOfMonth(1);
    }

    private void validarGasto(GastoForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario del gasto es obligatorio.");
        }
        if (form.getDescripcion() == null || form.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        if (form.getTipoGasto() == null) {
            throw new IllegalArgumentException("El tipo de gasto es obligatorio.");
        }
        if (form.getMetodoDistribucion() == null) {
            throw new IllegalArgumentException("El metodo de distribucion es obligatorio.");
        }
        if (form.getMontoTotal() <= 0) {
            throw new IllegalArgumentException("El monto total debe ser mayor a cero.");
        }

        if (form.getTipoGasto() == TipoGasto.EXTRAORDINARIO && form.getIncidenciaId() == null) {
            throw new IllegalArgumentException("Debe asociar la incidencia al gasto extraordinario.");
        }

        if (form.getTipoGasto() == TipoGasto.FIJO && form.getIncidenciaId() != null) {
            throw new IllegalArgumentException("El gasto fijo no debe asociarse a incidencias.");
        }
    }

    private void validarDistribucion(DistribucionGastoForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de distribucion es obligatorio.");
        }
        if (form.getGastoId() == null) {
            throw new IllegalArgumentException("El gasto es obligatorio.");
        }
    }

    private void validarEstadoCuenta(EstadoCuentaForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario del estado de cuenta es obligatorio.");
        }
        if (form.getUnidadId() == null) {
            throw new IllegalArgumentException("La unidad es obligatoria.");
        }
        if (form.getPeriodo() == null) {
            throw new IllegalArgumentException("El periodo es obligatorio.");
        }
    }

    private void validarPago(PagoForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario del pago es obligatorio.");
        }
        if (form.getUnidadId() == null) {
            throw new IllegalArgumentException("La unidad es obligatoria.");
        }
        if (form.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
    }

    private GastoResponse convertirGastoResponse(Gasto gasto) {
        return new GastoResponse(gasto.getId(), gasto.getDescripcion(), gasto.getTipoGasto(),
                gasto.getMetodoDistribucion(), gasto.getMontoTotal(),
                gasto.getIncidencia() != null ? gasto.getIncidencia().getId() : null,
                gasto.getFechaRegistro());
    }

    private DetalleGastoUnidadResponse convertirDetalleResponse(DetalleGastoUnidad detalle) {
        return new DetalleGastoUnidadResponse(detalle.getId(),
                detalle.getGasto() != null ? detalle.getGasto().getId() : null,
                detalle.getUnidad() != null ? detalle.getUnidad().getId() : null,
                detalle.getMontoAsignado(),
                detalle.getFechaRegistro());
    }

    private EstadoCuentaResponse convertirEstadoCuentaResponse(EstadoCuenta estadoCuenta) {
        return new EstadoCuentaResponse(estadoCuenta.getId(),
                estadoCuenta.getUnidad() != null ? estadoCuenta.getUnidad().getId() : null,
                estadoCuenta.getPeriodo(),
                estadoCuenta.getTotalCuotas(),
                estadoCuenta.getTotalExtraordinarios(),
                estadoCuenta.getTotalPagado(),
                estadoCuenta.getSaldo(),
                estadoCuenta.getFechaGeneracion());
    }

    private PagoResponse convertirPagoResponse(Pago pago) {
        return new PagoResponse(pago.getId(),
                pago.getUnidad() != null ? pago.getUnidad().getId() : null,
                pago.getEstadoCuenta() != null ? pago.getEstadoCuenta().getId() : null,
                pago.getMonto(),
                pago.getFechaPago(),
                pago.getObservacion());
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}

