package pe.edu.utp.condominio.api.dominios.finanzas.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.EstadoCuentaForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.EstadoCuentaResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.EstadoPago;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;
import pe.edu.utp.condominio.api.dominios.finanzas.models.DetalleGastoUnidad;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EstadoCuenta;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Pago;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.DetalleGastoUnidadRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.EstadoCuentaRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.PagoRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class GeneradorEstadoCuentaService {

    private final EstadoCuentaRepository estadoCuentaRepository;
    private final DetalleGastoUnidadRepository detalleGastoUnidadRepository;
    private final PagoRepository pagoRepository;
    private final UnidadRepository unidadRepository;

    public GeneradorEstadoCuentaService(EstadoCuentaRepository estadoCuentaRepository,
            DetalleGastoUnidadRepository detalleGastoUnidadRepository,
            PagoRepository pagoRepository,
            UnidadRepository unidadRepository) {
        this.estadoCuentaRepository = estadoCuentaRepository;
        this.detalleGastoUnidadRepository = detalleGastoUnidadRepository;
        this.pagoRepository = pagoRepository;
        this.unidadRepository = unidadRepository;
    }

    @Transactional
    public synchronized EstadoCuentaResponse generarEstadoCuenta(EstadoCuentaForm formulario) {
        validarEstadoCuenta(formulario);

        Unidad unidad = unidadRepository.findById(formulario.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        LocalDate periodoInput = LocalDate.parse(formulario.getPeriodo() + "-01");
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

    private double calcularPagosPeriodo(Long unidadId, LocalDate periodo) {
        return estadoCuentaRepository.buscarPorUnidadYPeriodo(unidadId, periodo)
                .map(ec -> pagoRepository.listarPorEstadoCuenta(ec.getId()).stream()
                        .filter(p -> p.getEstado() == EstadoPago.APROBADO)
                        .mapToDouble(Pago::getMonto)
                        .sum())
                .orElse(0.0);
    }

    private boolean esMismoPeriodo(LocalDate fecha, LocalDate periodo) {
        return fecha.getYear() == periodo.getYear() && fecha.getMonth() == periodo.getMonth();
    }

    private LocalDate normalizarPeriodo(LocalDate periodo) {
        return periodo.withDayOfMonth(1);
    }

    private void validarEstadoCuenta(EstadoCuentaForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario del estado de cuenta es obligatorio.");
        }
        if (formulario.getUnidadId() == null) {
            throw new IllegalArgumentException("La unidad es obligatoria.");
        }
        if (formulario.getPeriodo() == null) {
            throw new IllegalArgumentException("El periodo es obligatorio.");
        }
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
}
