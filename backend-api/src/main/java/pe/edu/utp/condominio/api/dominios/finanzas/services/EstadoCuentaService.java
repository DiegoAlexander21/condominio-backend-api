package pe.edu.utp.condominio.api.dominios.finanzas.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.DetalleGastoUnidadResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.EstadoCuentaResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;
import pe.edu.utp.condominio.api.dominios.finanzas.models.DetalleGastoUnidad;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EstadoCuenta;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Pago;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.DetalleGastoUnidadRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.EstadoCuentaRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.PagoRepository;

@Service
public class EstadoCuentaService {

    private final EstadoCuentaRepository estadoCuentaRepository;
    private final DetalleGastoUnidadRepository detalleGastoUnidadRepository;
    private final PagoRepository pagoRepository;

    public EstadoCuentaService(EstadoCuentaRepository estadoCuentaRepository,
            DetalleGastoUnidadRepository detalleGastoUnidadRepository,
            PagoRepository pagoRepository) {
        this.estadoCuentaRepository = estadoCuentaRepository;
        this.detalleGastoUnidadRepository = detalleGastoUnidadRepository;
        this.pagoRepository = pagoRepository;
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

        List<DetalleGastoUnidad> detalles = detalleGastoUnidadRepository
                .listarPorUnidad(estadoCuenta.getUnidad().getId());
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
        List<Pago> pagos = pagoRepository.listarPorEstadoCuenta(id);
        pagoRepository.deleteAll(pagos);
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

    private boolean esMismoPeriodo(LocalDate fecha, LocalDate periodo) {
        return fecha.getYear() == periodo.getYear() && fecha.getMonth() == periodo.getMonth();
    }

    private LocalDate normalizarPeriodo(LocalDate periodo) {
        return periodo.withDayOfMonth(1);
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

    private DetalleGastoUnidadResponse convertirDetalleResponse(DetalleGastoUnidad detalle) {
        return new DetalleGastoUnidadResponse(detalle.getId(),
                detalle.getGasto() != null ? detalle.getGasto().getId() : null,
                detalle.getUnidad() != null ? detalle.getUnidad().getId() : null,
                detalle.getGasto() != null ? detalle.getGasto().getDescripcion() : "Desconocido",
                detalle.getGasto() != null ? detalle.getGasto().getTipoGasto().name() : "-",
                detalle.getMontoAsignado(),
                detalle.getFechaRegistro(),
                detalle.getGasto() != null ? detalle.getGasto().getFechaLimite() : null);
    }
}
