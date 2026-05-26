package pe.edu.utp.condominio.api.dominios.finanzas.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
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
import pe.edu.utp.condominio.api.dominios.incidencias.models.IncidenciaUnidad;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.IncidenciaRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;

import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.EvidenciaPagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.EvidenciaPagoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EvidenciaPago;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.EvidenciaPagoRepository;

@Service
public class GestionFinanzasService {

    private final GastoRepository gastoRepository;
    private final DetalleGastoUnidadRepository detalleGastoUnidadRepository;
    private final EstadoCuentaRepository estadoCuentaRepository;
    private final PagoRepository pagoRepository;
    private final EvidenciaPagoRepository evidenciaPagoRepository;
    private final UnidadRepository unidadRepository;
    private final IncidenciaRepository incidenciaRepository;
    private final CondominioRepository condominioRepository;

    public GestionFinanzasService(GastoRepository gastoRepository,
            DetalleGastoUnidadRepository detalleGastoUnidadRepository,
            EstadoCuentaRepository estadoCuentaRepository,
            PagoRepository pagoRepository,
            EvidenciaPagoRepository evidenciaPagoRepository,
            UnidadRepository unidadRepository,
            IncidenciaRepository incidenciaRepository,
            CondominioRepository condominioRepository) {
        this.gastoRepository = gastoRepository;
        this.detalleGastoUnidadRepository = detalleGastoUnidadRepository;
        this.estadoCuentaRepository = estadoCuentaRepository;
        this.pagoRepository = pagoRepository;
        this.evidenciaPagoRepository = evidenciaPagoRepository;
        this.unidadRepository = unidadRepository;
        this.incidenciaRepository = incidenciaRepository;
        this.condominioRepository = condominioRepository;
    }

    @Transactional
    public synchronized GastoResponse registrarGasto(GastoForm form) {
        validarGasto(form);

        Gasto gasto = new Gasto();
        gasto.setDescripcion(form.getDescripcion().trim());
        gasto.setTipoGasto(form.getTipoGasto());
        gasto.setMetodoDistribucion(form.getMetodoDistribucion());
        gasto.setMontoTotal(form.getMontoTotal());
        gasto.setFechaLimite(form.getFechaLimite());

        Condominio condominio = condominioRepository.findById(form.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("El condominio no existe."));
        gasto.setCondominio(condominio);
        gasto.getTorres().clear();
        if (form.getTorre() != null && !form.getTorre().isBlank()) {
            gasto.getTorres().add(form.getTorre().trim());
        }

        if (form.getIncidenciaId() != null) {
            Incidencia incidencia = incidenciaRepository.findById(form.getIncidenciaId())
                    .orElseThrow(() -> new IllegalArgumentException("La incidencia no existe."));
            gasto.setIncidencia(incidencia);
        }

        Gasto guardado = gastoRepository.save(gasto);
        return convertirGastoResponse(guardado);
    }

    public synchronized GastoForm obtenerGastoParaEdicion(Long id) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El gasto no existe."));
        GastoForm form = new GastoForm();
        form.setId(gasto.getId());
        form.setDescripcion(gasto.getDescripcion());
        form.setTipoGasto(gasto.getTipoGasto());
        form.setMetodoDistribucion(gasto.getMetodoDistribucion());
        form.setMontoTotal(gasto.getMontoTotal());
        form.setFechaLimite(gasto.getFechaLimite());
        if (gasto.getIncidencia() != null) {
            form.setIncidenciaId(gasto.getIncidencia().getId());
        }
        if (gasto.getCondominio() != null) {
            form.setCondominioId(gasto.getCondominio().getId());
        }
        if (!gasto.getTorres().isEmpty()) {
            form.setTorre(gasto.getTorres().get(0));
        }
        return form;
    }

    @Transactional
    public synchronized GastoResponse actualizarGasto(Long id, GastoForm form) {
        validarGasto(form);

        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El gasto no existe."));

        gasto.setDescripcion(form.getDescripcion().trim());
        gasto.setTipoGasto(form.getTipoGasto());
        gasto.setMetodoDistribucion(form.getMetodoDistribucion());
        gasto.setMontoTotal(form.getMontoTotal());
        gasto.setFechaLimite(form.getFechaLimite());

        Condominio condominio = condominioRepository.findById(form.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("El condominio no existe."));
        gasto.setCondominio(condominio);
        gasto.getTorres().clear();
        if (form.getTorre() != null && !form.getTorre().isBlank()) {
            gasto.getTorres().add(form.getTorre().trim());
        }

        if (form.getIncidenciaId() != null) {
            Incidencia incidencia = incidenciaRepository.findById(form.getIncidenciaId())
                    .orElseThrow(() -> new IllegalArgumentException("La incidencia no existe."));
            gasto.setIncidencia(incidencia);
        } else {
            gasto.setIncidencia(null);
        }

        List<DetalleGastoUnidad> detalles = detalleGastoUnidadRepository.listarPorGasto(id);
        if (!detalles.isEmpty()) {
            detalleGastoUnidadRepository.deleteAll(detalles);
        }

        Gasto guardado = gastoRepository.save(gasto);
        return convertirGastoResponse(guardado);
    }
    @Transactional(readOnly = true)
    public synchronized List<GastoResponse> listarGastosPorTipo(TipoGasto tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de gasto.");
        }
        return gastoRepository.listarPorTipo(tipo).stream()
                .map(this::convertirGastoResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public synchronized void eliminarGasto(Long gastoId) {
        Gasto gasto = gastoRepository.findById(gastoId)
                .orElseThrow(() -> new IllegalArgumentException("El gasto no existe."));
        List<DetalleGastoUnidad> detalles = detalleGastoUnidadRepository.listarPorGasto(gastoId);
        detalleGastoUnidadRepository.deleteAll(detalles);
        gastoRepository.delete(gasto);
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

        LocalDate periodoInput = LocalDate.parse(form.getPeriodo() + "-01");
        LocalDate periodo = normalizarPeriodo(periodoInput);

        List<DetalleGastoUnidad> detalles = detalleGastoUnidadRepository.listarPorUnidad(unidad.getId());
        double totalCuotas = 0;
        double totalExtraordinarios = 0;

        LocalDate maxFechaVencimiento = null;

        for (DetalleGastoUnidad detalle : detalles) {
            LocalDate fechaRegistroGasto = normalizarPeriodo(detalle.getFechaRegistro().toLocalDate());
            
            boolean esValido = false;
            if (detalle.getGasto().getTipoGasto() == TipoGasto.FIJO) {
                if (!periodo.isBefore(fechaRegistroGasto)) {
                    totalCuotas += detalle.getMontoAsignado();
                    esValido = true;
                }
            } else {
                if (esMismoPeriodo(detalle.getFechaRegistro().toLocalDate(), periodo)) {
                    totalExtraordinarios += detalle.getMontoAsignado();
                    esValido = true;
                }
            }

            if (esValido) {
                LocalDate limite = detalle.getGasto().getFechaLimite();
                if (limite != null) {
                    if (maxFechaVencimiento == null || limite.isAfter(maxFechaVencimiento)) {
                        maxFechaVencimiento = limite;
                    }
                }
            }
        }

        if (maxFechaVencimiento == null) {
            maxFechaVencimiento = periodo.withDayOfMonth(periodo.lengthOfMonth());
        }

        double totalPagado = calcularPagosPeriodo(unidad.getId(), periodo);
        double saldo = totalCuotas + totalExtraordinarios - totalPagado;

        totalCuotas = Math.round(totalCuotas * 100.0) / 100.0;
        totalExtraordinarios = Math.round(totalExtraordinarios * 100.0) / 100.0;
        totalPagado = Math.round(totalPagado * 100.0) / 100.0;
        saldo = Math.round(saldo * 100.0) / 100.0;

        EstadoCuenta estadoCuenta = estadoCuentaRepository
                .buscarPorUnidadYPeriodo(unidad.getId(), periodo)
                .orElseGet(EstadoCuenta::new);

        estadoCuenta.setUnidad(unidad);
        estadoCuenta.setPeriodo(periodo);
        estadoCuenta.setTotalCuotas(totalCuotas);
        estadoCuenta.setTotalExtraordinarios(totalExtraordinarios);
        estadoCuenta.setTotalPagado(totalPagado);
        estadoCuenta.setSaldo(saldo);
        estadoCuenta.setFechaVencimiento(maxFechaVencimiento);

        EstadoCuenta guardado = estadoCuentaRepository.save(estadoCuenta);
        return convertirEstadoCuentaResponse(guardado);
    }
    @Transactional(readOnly = true)
    public synchronized EstadoCuentaResponse obtenerEstadoCuentaResponse(Long id) {
        EstadoCuenta estadoCuenta = estadoCuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estado de cuenta no existe."));
        return convertirEstadoCuentaResponse(estadoCuenta);
    }

    @Transactional(readOnly = true)
    public synchronized List<DetalleGastoUnidadResponse> listarDesgloseEstadoCuenta(Long id) {
        EstadoCuenta estadoCuenta = estadoCuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estado de cuenta no existe."));

        List<DetalleGastoUnidad> detalles = detalleGastoUnidadRepository.listarPorUnidad(estadoCuenta.getUnidad().getId());
        List<DetalleGastoUnidad> aplicables = new ArrayList<>();

        for (DetalleGastoUnidad detalle : detalles) {
            LocalDate fechaRegistroGasto = normalizarPeriodo(detalle.getFechaRegistro().toLocalDate());
            
            if (detalle.getGasto().getTipoGasto() == TipoGasto.FIJO) {
                if (!estadoCuenta.getPeriodo().isBefore(fechaRegistroGasto)) {
                    aplicables.add(detalle);
                }
            } else {
                if (esMismoPeriodo(detalle.getFechaRegistro().toLocalDate(), estadoCuenta.getPeriodo())) {
                    aplicables.add(detalle);
                }
            }
        }

        return aplicables.stream()
                .map(this::convertirDetalleResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public synchronized void eliminarEstadoCuenta(Long id) {
        if (!estadoCuentaRepository.existsById(id)) {
            throw new IllegalArgumentException("El estado de cuenta no existe.");
        }
        estadoCuentaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public synchronized List<EstadoCuentaResponse> listarTodosEstadosCuenta() {
        return estadoCuentaRepository.findAll().stream()
                .map(this::convertirEstadoCuentaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public synchronized List<EstadoCuentaResponse> listarEstadosCuentaPorUnidad(Long unidadId) {
        if (unidadId == null) {
            throw new IllegalArgumentException("Debe seleccionar una unidad valida.");
        }
        return estadoCuentaRepository.findByUnidadId(unidadId).stream()
                .map(this::convertirEstadoCuentaResponse)
                .collect(Collectors.toList());
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

            double saldoPendiente = estadoCuenta.getSaldo();
            if (form.getMonto() > saldoPendiente) {
                throw new IllegalArgumentException(String.format("El monto del pago (S/ %.2f) no puede ser mayor al saldo pendiente (S/ %.2f).", form.getMonto(), saldoPendiente));
            }

            pago.setEstadoCuenta(estadoCuenta);

            estadoCuenta.setTotalPagado(estadoCuenta.getTotalPagado() + form.getMonto());
            estadoCuenta.setSaldo(estadoCuenta.getTotalCuotas() + estadoCuenta.getTotalExtraordinarios()
                    - estadoCuenta.getTotalPagado());
            estadoCuentaRepository.save(estadoCuenta);
        }

        Pago guardado = pagoRepository.save(pago);
        return convertirPagoResponse(guardado);
    }

    @Transactional
    public synchronized EvidenciaPagoResponse registrarEvidenciaPago(EvidenciaPagoForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de evidencia es obligatorio.");
        }
        if (form.getPagoId() == null) {
            throw new IllegalArgumentException("El ID del pago es obligatorio.");
        }
        if (form.getUrlArchivo() == null || form.getUrlArchivo().isBlank()) {
            throw new IllegalArgumentException("La URL del archivo es obligatoria.");
        }

        Pago pago = pagoRepository.findById(form.getPagoId())
                .orElseThrow(() -> new IllegalArgumentException("El pago no existe."));

        EvidenciaPago evidencia = new EvidenciaPago();
        evidencia.setPago(pago);
        evidencia.setUrlArchivo(form.getUrlArchivo().trim());

        EvidenciaPago guardada = evidenciaPagoRepository.save(evidencia);
        return convertirEvidenciaPagoResponse(guardada);
    }

    @Transactional(readOnly = true)
    public synchronized List<EvidenciaPagoResponse> listarEvidenciasPago(Long pagoId) {
        if (pagoId == null) {
            throw new IllegalArgumentException("Debe seleccionar un pago válido.");
        }
        return evidenciaPagoRepository.listarPorPago(pagoId).stream()
                .map(this::convertirEvidenciaPagoResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public synchronized List<PagoResponse> listarPagosPorUnidad(Long unidadId) {
        if (unidadId == null) {
            throw new IllegalArgumentException("Debe seleccionar una unidad valida.");
        }
        return pagoRepository.listarPorUnidad(unidadId).stream()
                .map(this::convertirPagoResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public synchronized List<PagoResponse> listarPagosPorEstadoCuenta(Long estadoCuentaId) {
        if (estadoCuentaId == null) {
            throw new IllegalArgumentException("Debe seleccionar un estado de cuenta valido.");
        }
        return pagoRepository.listarPorEstadoCuenta(estadoCuentaId).stream()
                .map(this::convertirPagoResponse)
                .collect(Collectors.toList());
    }

    private List<DetalleGastoUnidad> generarDetalles(Gasto gasto, Long unidadId) {
        if (gasto.getCondominio() == null) {
            throw new IllegalArgumentException("El gasto debe estar asociado a un condominio.");
        }

        List<Unidad> unidades;
        if (!gasto.getTorres().isEmpty()) {
            unidades = unidadRepository.listarPorCondominioYTorre(gasto.getCondominio().getId(), gasto.getTorres().get(0));
            if (unidades.isEmpty()) {
                throw new IllegalArgumentException("No existen unidades registradas en la torre especificada para este condominio.");
            }
        } else {
            unidades = unidadRepository.listarPorCondominio(gasto.getCondominio().getId());
            if (unidades.isEmpty()) {
                throw new IllegalArgumentException("No existen unidades registradas en este condominio.");
            }
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
            monto = Math.round(monto * 100.0) / 100.0;
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
            monto = Math.round(monto * 100.0) / 100.0;
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
        GastoResponse response = new GastoResponse(gasto.getId(), gasto.getDescripcion(), gasto.getTipoGasto(),
                gasto.getMetodoDistribucion(),
                gasto.getIncidencia() != null ? gasto.getIncidencia().getId() : null,
                gasto.getMontoTotal(),
                gasto.getFechaRegistro(),
                gasto.getFechaLimite(),
                gasto.getCondominio() != null ? gasto.getCondominio().getId() : null,
                gasto.getCondominio() != null ? gasto.getCondominio().getNombre() : null,
                gasto.getTorres().isEmpty() ? null : gasto.getTorres().get(0));
        
        if (gasto.getIncidencia() != null) {
            Incidencia realIncidencia = (Incidencia) Hibernate.unproxy(gasto.getIncidencia());
            if (realIncidencia instanceof IncidenciaUnidad) {
                IncidenciaUnidad iu = (IncidenciaUnidad) realIncidencia;
                response.setUnidadIdCausante(iu.getUnidad().getId());
            }
        }
        
        boolean distribuido = !detalleGastoUnidadRepository.listarPorGasto(gasto.getId()).isEmpty();
        response.setDistribuido(distribuido);
        
        return response;
    }

    private DetalleGastoUnidadResponse convertirDetalleResponse(DetalleGastoUnidad detalle) {
        return new DetalleGastoUnidadResponse(detalle.getId(),
                detalle.getGasto() != null ? detalle.getGasto().getId() : null,
                detalle.getUnidad() != null ? detalle.getUnidad().getId() : null,
                detalle.getGasto() != null ? detalle.getGasto().getDescripcion() : "Desconocido",
                detalle.getGasto() != null ? detalle.getGasto().getTipoGasto().name() : "-",
                detalle.getMontoAsignado(),
                detalle.getFechaRegistro());
    }

    private EstadoCuentaResponse convertirEstadoCuentaResponse(EstadoCuenta estadoCuenta) {
        String unidadDetalles = null;
        if (estadoCuenta.getUnidad() != null) {
            unidadDetalles = estadoCuenta.getUnidad().getCondominio().getNombre() + " - " +
                             estadoCuenta.getUnidad().getTorre() + " - Piso " +
                             estadoCuenta.getUnidad().getPiso() + " - Unidad " +
                             estadoCuenta.getUnidad().getNumeroUnidad();
        }

        return new EstadoCuentaResponse(estadoCuenta.getId(),
                estadoCuenta.getUnidad() != null ? estadoCuenta.getUnidad().getId() : null,
                unidadDetalles,
                estadoCuenta.getPeriodo(),
                estadoCuenta.getTotalCuotas(),
                estadoCuenta.getTotalExtraordinarios(),
                estadoCuenta.getTotalPagado(),
                estadoCuenta.getSaldo(),
                estadoCuenta.getFechaVencimiento(),
                estadoCuenta.getFechaGeneracion());
    }

    private PagoResponse convertirPagoResponse(Pago pago) {
        List<EvidenciaPagoResponse> evidenciasResponse = pago.getEvidencias().stream()
                .map(this::convertirEvidenciaPagoResponse)
                .collect(Collectors.toList());

        return new PagoResponse(
                pago.getId(),
                pago.getUnidad().getId(),
                "Unidad N° " + pago.getUnidad().getNumeroUnidad(),
                pago.getEstadoCuenta() != null ? pago.getEstadoCuenta().getId() : null,
                pago.getMonto(),
                pago.getFechaPago(),
                pago.getObservacion(),
                evidenciasResponse);
    }

    private EvidenciaPagoResponse convertirEvidenciaPagoResponse(EvidenciaPago evidencia) {
        return new EvidenciaPagoResponse(
                evidencia.getId(),
                evidencia.getPago() != null ? evidencia.getPago().getId() : null,
                evidencia.getUrlArchivo(),
                evidencia.getFechaRegistro()
        );
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}

