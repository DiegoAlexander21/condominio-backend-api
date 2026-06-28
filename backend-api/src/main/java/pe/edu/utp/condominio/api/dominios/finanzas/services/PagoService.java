package pe.edu.utp.condominio.api.dominios.finanzas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.PagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.EvidenciaPagoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.PagoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.EstadoPago;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EstadoCuenta;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EvidenciaPago;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Pago;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.EstadoCuentaRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.PagoRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final EstadoCuentaRepository estadoCuentaRepository;
    private final UnidadRepository unidadRepository;

    public PagoService(PagoRepository pagoRepository,
            EstadoCuentaRepository estadoCuentaRepository,
            UnidadRepository unidadRepository) {
        this.pagoRepository = pagoRepository;
        this.estadoCuentaRepository = estadoCuentaRepository;
        this.unidadRepository = unidadRepository;
    }

    @Transactional
    public synchronized PagoResponse registrarPago(PagoForm formulario) {
        validarPago(formulario);

        Unidad unidad = unidadRepository.findById(formulario.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        Pago pago = new Pago();
        pago.setUnidad(unidad);
        pago.setMonto(formulario.getMonto());
        pago.setObservacion(normalizarTexto(formulario.getObservacion()));

        if (formulario.getEstadoCuentaId() != null) {
            EstadoCuenta estadoCuenta = estadoCuentaRepository.findById(formulario.getEstadoCuentaId())
                    .orElseThrow(() -> new IllegalArgumentException("El estado de cuenta no existe."));

            double saldoPendiente = estadoCuenta.getSaldo();
            if (formulario.getMonto() > saldoPendiente) {
                throw new IllegalArgumentException(
                        String.format("El monto del pago (S/ %.2f) no puede ser mayor al saldo pendiente (S/ %.2f).",
                                formulario.getMonto(), saldoPendiente));
            }

            pago.setEstadoCuenta(estadoCuenta);
        }

        Pago guardado = pagoRepository.save(pago);

        return convertirPagoResponse(guardado);
    }

    @Transactional
    public synchronized PagoResponse aprobarPago(Long pagoId, boolean aprobar, String observacionAdmin) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new IllegalArgumentException("El pago no existe."));

        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new IllegalArgumentException("El pago ya ha sido procesado (aprobado o rechazado).");
        }

        if (aprobar) {
            pago.setEstado(EstadoPago.APROBADO);
            if (pago.getEstadoCuenta() != null) {
                EstadoCuenta estadoCuenta = pago.getEstadoCuenta();
                estadoCuenta.setTotalPagado(estadoCuenta.getTotalPagado() + pago.getMonto());
                estadoCuenta.setSaldo(estadoCuenta.getTotalCuotas() + estadoCuenta.getTotalExtraordinarios()
                        - estadoCuenta.getTotalPagado());
                estadoCuentaRepository.save(estadoCuenta);
            }
        } else {
            pago.setEstado(EstadoPago.RECHAZADO);
        }

        if (observacionAdmin != null && !observacionAdmin.isBlank()) {
            pago.setObservacion(pago.getObservacion() + " | Admin: " + observacionAdmin);
        }

        Pago guardado = pagoRepository.save(pago);
        return convertirPagoResponse(guardado);
    }

    @Transactional(readOnly = true)
    public synchronized List<PagoResponse> listarPagosPendientes() {
        return pagoRepository.findByEstadoOrderByFechaPagoDesc(EstadoPago.PENDIENTE).stream()
                .map(this::convertirPagoResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public synchronized Page<PagoResponse> listarPagos(EstadoPago estado, Pageable pageable) {
        if (estado != null) {
            return pagoRepository.findByEstado(estado, pageable)
                    .map(this::convertirPagoResponse);
        }
        return pagoRepository.findAll(pageable)
                .map(this::convertirPagoResponse);
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

    private void validarPago(PagoForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario del pago es obligatorio.");
        }
        if (formulario.getUnidadId() == null) {
            throw new IllegalArgumentException("La unidad es obligatoria.");
        }
        if (formulario.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
    }

    private PagoResponse convertirPagoResponse(Pago pago) {
        List<EvidenciaPagoResponse> respuestasEvidencias = pago.getEvidencias().stream()
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
                pago.getEstado().name(),
                respuestasEvidencias);
    }

    private EvidenciaPagoResponse convertirEvidenciaPagoResponse(EvidenciaPago evidencia) {
        return new EvidenciaPagoResponse(
                evidencia.getId(),
                evidencia.getPago() != null ? evidencia.getPago().getId() : null,
                evidencia.getUrlArchivo(),
                evidencia.getFechaRegistro());
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}
