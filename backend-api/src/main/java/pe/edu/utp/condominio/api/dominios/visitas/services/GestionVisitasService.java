package pe.edu.utp.condominio.api.dominios.visitas.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;
import pe.edu.utp.condominio.api.dominios.visitas.dto.request.RegistroIngresoVisitaForm;
import pe.edu.utp.condominio.api.dominios.visitas.dto.request.RegistroSalidaVisitaForm;
import pe.edu.utp.condominio.api.dominios.visitas.dto.request.VisitaForm;
import pe.edu.utp.condominio.api.dominios.visitas.dto.response.VisitaResponse;
import pe.edu.utp.condominio.api.dominios.visitas.enums.EstadoVisita;
import pe.edu.utp.condominio.api.dominios.visitas.models.Visita;
import pe.edu.utp.condominio.api.dominios.visitas.repositories.VisitaRepository;

@Service
public class GestionVisitasService {

    private final VisitaRepository visitaRepository;
    private final UnidadRepository unidadRepository;

    public GestionVisitasService(VisitaRepository visitaRepository, UnidadRepository unidadRepository) {
        this.visitaRepository = visitaRepository;
        this.unidadRepository = unidadRepository;
    }

    @Transactional
    public synchronized VisitaResponse registrarVisita(VisitaForm form) {
        validarVisita(form);

        Unidad unidad = unidadRepository.findById(form.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        Visita visita = new Visita();
        visita.setUnidad(unidad);
        visita.setNombreVisitante(form.getNombreVisitante().trim());
        visita.setDocumentoVisitante(form.getDocumentoVisitante().trim());
        visita.setFechaVisitaProgramada(form.getFechaVisitaProgramada());
        visita.setEstado(EstadoVisita.PRE_REGISTRADA);

        Visita guardada = visitaRepository.save(visita);
        return convertirVisitaResponse(guardada);
    }

    @Transactional
    public synchronized VisitaResponse registrarIngreso(RegistroIngresoVisitaForm form) {
        validarIngreso(form);

        Visita visita = visitaRepository.findById(form.getVisitaId())
                .orElseThrow(() -> new IllegalArgumentException("La visita no existe."));

        if (visita.getEstado() != EstadoVisita.PRE_REGISTRADA) {
            throw new IllegalArgumentException("La visita no esta en estado pre-registrado.");
        }

        LocalDateTime ingreso = form.getFechaIngreso() != null ? form.getFechaIngreso() : LocalDateTime.now();
        visita.setFechaIngreso(ingreso);
        visita.setEstado(EstadoVisita.INGRESO_REGISTRADO);

        Visita actualizada = visitaRepository.save(visita);
        return convertirVisitaResponse(actualizada);
    }

    @Transactional
    public synchronized VisitaResponse registrarSalida(RegistroSalidaVisitaForm form) {
        validarSalida(form);

        Visita visita = visitaRepository.findById(form.getVisitaId())
                .orElseThrow(() -> new IllegalArgumentException("La visita no existe."));

        if (visita.getEstado() != EstadoVisita.INGRESO_REGISTRADO) {
            throw new IllegalArgumentException("La visita no tiene ingreso registrado.");
        }

        LocalDateTime salida = form.getFechaSalida() != null ? form.getFechaSalida() : LocalDateTime.now();
        visita.setFechaSalida(salida);
        visita.setEstado(EstadoVisita.SALIDA_REGISTRADA);

        Visita actualizada = visitaRepository.save(visita);
        return convertirVisitaResponse(actualizada);
    }

    public synchronized List<VisitaResponse> listarPorUnidad(Long unidadId) {
        if (unidadId == null) {
            throw new IllegalArgumentException("Debe seleccionar una unidad valida.");
        }
        return visitaRepository.listarPorUnidad(unidadId).stream()
                .map(this::convertirVisitaResponse)
                .collect(Collectors.toList());
    }

    public synchronized List<VisitaResponse> listarPorEstado(EstadoVisita estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Debe seleccionar un estado.");
        }
        return visitaRepository.listarPorEstado(estado).stream()
                .map(this::convertirVisitaResponse)
                .collect(Collectors.toList());
    }

    public synchronized List<VisitaResponse> listarPorRango(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Debe indicar el rango de fechas.");
        }
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior al fin.");
        }
        return visitaRepository.listarPorRango(inicio, fin).stream()
                .map(this::convertirVisitaResponse)
                .collect(Collectors.toList());
    }

    private void validarVisita(VisitaForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de visita es obligatorio.");
        }
        if (form.getUnidadId() == null) {
            throw new IllegalArgumentException("La unidad es obligatoria.");
        }
        if (form.getNombreVisitante() == null || form.getNombreVisitante().isBlank()) {
            throw new IllegalArgumentException("El nombre del visitante es obligatorio.");
        }
        if (form.getDocumentoVisitante() == null || form.getDocumentoVisitante().isBlank()) {
            throw new IllegalArgumentException("El documento del visitante es obligatorio.");
        }
        if (form.getFechaVisitaProgramada() == null) {
            throw new IllegalArgumentException("La fecha programada es obligatoria.");
        }
    }

    private void validarIngreso(RegistroIngresoVisitaForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de ingreso es obligatorio.");
        }
        if (form.getVisitaId() == null) {
            throw new IllegalArgumentException("La visita es obligatoria.");
        }
    }

    private void validarSalida(RegistroSalidaVisitaForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de salida es obligatorio.");
        }
        if (form.getVisitaId() == null) {
            throw new IllegalArgumentException("La visita es obligatoria.");
        }
    }

    private VisitaResponse convertirVisitaResponse(Visita visita) {
        return new VisitaResponse(visita.getId(),
                visita.getUnidad() != null ? visita.getUnidad().getId() : null,
                visita.getNombreVisitante(),
                visita.getDocumentoVisitante(),
                visita.getFechaVisitaProgramada(),
                visita.getFechaIngreso(),
                visita.getFechaSalida(),
                visita.getEstado(),
                visita.getFechaRegistro());
    }
}

