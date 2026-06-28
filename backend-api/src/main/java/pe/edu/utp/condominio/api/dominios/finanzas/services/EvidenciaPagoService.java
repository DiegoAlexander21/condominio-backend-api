package pe.edu.utp.condominio.api.dominios.finanzas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.EvidenciaPagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.EvidenciaPagoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.models.EvidenciaPago;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Pago;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.EvidenciaPagoRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.PagoRepository;

@Service
public class EvidenciaPagoService {

    private final EvidenciaPagoRepository evidenciaPagoRepository;
    private final PagoRepository pagoRepository;

    public EvidenciaPagoService(EvidenciaPagoRepository evidenciaPagoRepository, PagoRepository pagoRepository) {
        this.evidenciaPagoRepository = evidenciaPagoRepository;
        this.pagoRepository = pagoRepository;
    }

    @Transactional
    public synchronized EvidenciaPagoResponse registrarEvidenciaPago(EvidenciaPagoForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de evidencia es obligatorio.");
        }
        if (formulario.getPagoId() == null) {
            throw new IllegalArgumentException("El ID del pago es obligatorio.");
        }
        if (formulario.getUrlArchivo() == null || formulario.getUrlArchivo().isBlank()) {
            throw new IllegalArgumentException("La URL del archivo es obligatoria.");
        }

        Pago pago = pagoRepository.findById(formulario.getPagoId())
                .orElseThrow(() -> new IllegalArgumentException("El pago no existe."));

        EvidenciaPago evidencia = new EvidenciaPago();
        evidencia.setPago(pago);
        evidencia.setUrlArchivo(formulario.getUrlArchivo().trim());

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

    private EvidenciaPagoResponse convertirEvidenciaPagoResponse(EvidenciaPago evidencia) {
        return new EvidenciaPagoResponse(
                evidencia.getId(),
                evidencia.getPago() != null ? evidencia.getPago().getId() : null,
                evidencia.getUrlArchivo(),
                evidencia.getFechaRegistro());
    }
}
