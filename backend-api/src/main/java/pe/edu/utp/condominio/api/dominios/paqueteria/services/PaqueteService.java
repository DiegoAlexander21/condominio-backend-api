package pe.edu.utp.condominio.api.dominios.paqueteria.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.paqueteria.dto.request.PaqueteForm;
import pe.edu.utp.condominio.api.dominios.paqueteria.dto.request.RegistroEntregaPaqueteForm;
import pe.edu.utp.condominio.api.dominios.paqueteria.dto.response.PaqueteResponse;
import pe.edu.utp.condominio.api.dominios.paqueteria.enums.EstadoPaquete;
import pe.edu.utp.condominio.api.dominios.paqueteria.models.Paquete;
import pe.edu.utp.condominio.api.dominios.paqueteria.repositories.PaqueteRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;
    private final UnidadRepository unidadRepository;
    private final NotificacionPaqueteriaService notificacionPaqueteriaService;

    public PaqueteService(PaqueteRepository paqueteRepository,
            UnidadRepository unidadRepository,
            NotificacionPaqueteriaService notificacionPaqueteriaService) {
        this.paqueteRepository = paqueteRepository;
        this.unidadRepository = unidadRepository;
        this.notificacionPaqueteriaService = notificacionPaqueteriaService;
    }

    @Transactional
    public synchronized PaqueteResponse registrarRecepcion(PaqueteForm formulario) {
        validarPaquete(formulario);

        Unidad unidad = unidadRepository.findById(formulario.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        Paquete paquete = new Paquete();
        paquete.setUnidad(unidad);
        paquete.setRemitente(formulario.getRemitente().trim());
        paquete.setDestinatario(formulario.getDestinatario().trim());
        paquete.setEstado(EstadoPaquete.RECIBIDO);
        paquete.setObservacion(normalizarTexto(formulario.getObservacion()));

        Paquete guardado = paqueteRepository.save(paquete);

        try {
            notificacionPaqueteriaService.enviarNotificacion(guardado, unidad);
            guardado.setEstado(EstadoPaquete.NOTIFICADO);
            guardado = paqueteRepository.save(guardado);
        } catch (IllegalArgumentException ex) {
            guardado.setObservacion(unirObservacion(guardado.getObservacion(),
                    "Notificacion pendiente: " + ex.getMessage()));
            guardado = paqueteRepository.save(guardado);
        } catch (MailException ex) {
            guardado.setObservacion(unirObservacion(guardado.getObservacion(),
                    "Fallo al enviar correo: Verifique credenciales."));
            guardado = paqueteRepository.save(guardado);
        }

        return convertirPaqueteResponse(guardado);
    }

    @Transactional
    public synchronized PaqueteResponse registrarEntrega(RegistroEntregaPaqueteForm formulario) {
        validarEntrega(formulario);

        Paquete paquete = paqueteRepository.findById(formulario.getPaqueteId())
                .orElseThrow(() -> new IllegalArgumentException("El paquete no existe."));

        if (paquete.getEstado() == EstadoPaquete.ENTREGADO) {
            throw new IllegalArgumentException("El paquete ya fue entregado.");
        }

        LocalDateTime entrega = formulario.getFechaEntrega() != null ? formulario.getFechaEntrega() : LocalDateTime.now();
        paquete.setFechaEntrega(entrega);
        paquete.setEstado(EstadoPaquete.ENTREGADO);
        paquete.setObservacion(unirObservacion(paquete.getObservacion(),
                normalizarTexto(formulario.getObservacion())));

        Paquete actualizado = paqueteRepository.save(paquete);
        return convertirPaqueteResponse(actualizado);
    }

    @Transactional(readOnly = true)
    public synchronized List<PaqueteResponse> listarPorUnidad(Long unidadId) {
        if (unidadId == null) {
            throw new IllegalArgumentException("Debe seleccionar una unidad valida.");
        }
        return paqueteRepository.listarPorUnidad(unidadId).stream()
                .map(this::convertirPaqueteResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public synchronized List<PaqueteResponse> listarPorCondominioYEstado(Long condominioId, EstadoPaquete estado) {
        if (condominioId == null) {
            throw new IllegalArgumentException("Debe proporcionar el identificador del condominio.");
        }
        if (estado == null) {
            throw new IllegalArgumentException("Debe seleccionar un estado.");
        }
        return paqueteRepository.listarPorCondominioYEstado(condominioId, estado).stream()
                .map(this::convertirPaqueteResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public synchronized List<PaqueteResponse> listarPorCondominio(Long condominioId) {
        if (condominioId == null) {
            throw new IllegalArgumentException("Debe proporcionar el identificador del condominio.");
        }
        return paqueteRepository.listarPorCondominio(condominioId).stream()
                .map(this::convertirPaqueteResponse)
                .collect(Collectors.toList());
    }

    private void validarPaquete(PaqueteForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario del paquete es obligatorio.");
        }
        if (formulario.getUnidadId() == null) {
            throw new IllegalArgumentException("La unidad es obligatoria.");
        }
        if (formulario.getRemitente() == null || formulario.getRemitente().isBlank()) {
            throw new IllegalArgumentException("El remitente es obligatorio.");
        }
        if (formulario.getDestinatario() == null || formulario.getDestinatario().isBlank()) {
            throw new IllegalArgumentException("El destinatario es obligatorio.");
        }
    }

    private void validarEntrega(RegistroEntregaPaqueteForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de entrega es obligatorio.");
        }
        if (formulario.getPaqueteId() == null) {
            throw new IllegalArgumentException("El paquete es obligatorio.");
        }
    }

    private PaqueteResponse convertirPaqueteResponse(Paquete paquete) {
        return new PaqueteResponse(paquete.getId(),
                paquete.getUnidad() != null ? paquete.getUnidad().getId() : null,
                paquete.getUnidad() != null ? paquete.getUnidad().getNumeroUnidad() : null,
                paquete.getUnidad() != null ? paquete.getUnidad().getTorre() : null,
                paquete.getRemitente(),
                paquete.getDestinatario(),
                paquete.getEstado(),
                paquete.getFechaRecepcion(),
                paquete.getFechaEntrega(),
                paquete.getObservacion());
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private String unirObservacion(String base, String extra) {
        if (extra == null || extra.isBlank()) {
            return base;
        }
        if (base == null || base.isBlank()) {
            return extra;
        }
        return base + " | " + extra;
    }
}
