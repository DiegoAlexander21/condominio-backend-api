package pe.edu.utp.condominio.api.dominios.areascomunes.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.ReservaAreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.response.ReservaAreaComunResponse;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.ReservaAreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.ReservaAreaComunRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class ReservaAreaComunService {

    private final AreaComunRepository areaComunRepository;
    private final ReservaAreaComunRepository reservaAreaComunRepository;
    private final UnidadRepository unidadRepository;

    public ReservaAreaComunService(AreaComunRepository areaComunRepository,
            ReservaAreaComunRepository reservaAreaComunRepository,
            UnidadRepository unidadRepository) {
        this.areaComunRepository = areaComunRepository;
        this.reservaAreaComunRepository = reservaAreaComunRepository;
        this.unidadRepository = unidadRepository;
    }

    @Transactional
    public synchronized ReservaAreaComunResponse registrarReserva(ReservaAreaComunForm formulario) {
        validarReserva(formulario);

        AreaComun areaComun = areaComunRepository.findById(formulario.getAreaComunId())
                .orElseThrow(() -> new IllegalArgumentException("El area comun no existe."));
        Unidad unidad = unidadRepository.findById(formulario.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        validarHorarioDisponible(areaComun, formulario.getFechaReserva(),
                formulario.getHoraInicio(), formulario.getHoraFin());

        ReservaAreaComun reserva = new ReservaAreaComun();
        reserva.setAreaComun(areaComun);
        reserva.setUnidad(unidad);
        reserva.setFechaReserva(formulario.getFechaReserva());
        reserva.setHoraInicio(formulario.getHoraInicio());
        reserva.setHoraFin(formulario.getHoraFin());
        reserva.setResponsableNombre(formulario.getResponsableNombre().trim());

        ReservaAreaComun guardada = reservaAreaComunRepository.save(reserva);
        return convertirReservaResponse(guardada);
    }

    @Transactional
    public synchronized void cancelarReserva(Long id) {
        ReservaAreaComun reserva = reservaAreaComunRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe."));
        reserva.setEstado("CANCELADA");
        reservaAreaComunRepository.save(reserva);
    }

    @Transactional(readOnly = true)
    public synchronized Page<ReservaAreaComunResponse> listarReservasPaginado(Long areaComunId, LocalDate fecha, Long unidadId, Pageable paginacion) {
        if (areaComunId == null) {
            throw new IllegalArgumentException("Debe seleccionar un area comun valida.");
        }
        
        Page<ReservaAreaComun> pagina;
        if (fecha == null && unidadId == null) {
            pagina = reservaAreaComunRepository.listarPorAreaPaginado(areaComunId, paginacion);
        } else if (fecha != null && unidadId == null) {
            pagina = reservaAreaComunRepository.listarPorAreaYFechaPaginado(areaComunId, fecha, paginacion);
        } else if (fecha == null && unidadId != null) {
            pagina = reservaAreaComunRepository.listarPorAreaYUnidadPaginado(areaComunId, unidadId, paginacion);
        } else {
            pagina = reservaAreaComunRepository.listarPorAreaFechaYUnidadPaginado(areaComunId, fecha, unidadId, paginacion);
        }
        
        return pagina.map(this::convertirReservaResponse);
    }

    @Transactional(readOnly = true)
    public synchronized List<ReservaAreaComunResponse> listarReservas(Long areaComunId, LocalDate fecha) {
        if (areaComunId == null) {
            throw new IllegalArgumentException("Debe seleccionar un area comun valida.");
        }
        if (fecha == null) {
            return reservaAreaComunRepository.listarPorArea(areaComunId).stream()
                    .map(this::convertirReservaResponse)
                    .collect(Collectors.toList());
        }
        return reservaAreaComunRepository.listarPorAreaYFecha(areaComunId, fecha).stream()
                .map(this::convertirReservaResponse)
                .collect(Collectors.toList());
    }

    private void validarHorarioDisponible(AreaComun areaComun, LocalDate fecha, LocalTime horaInicio,
            LocalTime horaFin) {
        if (horaInicio.isBefore(areaComun.getHoraInicio()) || horaFin.isAfter(areaComun.getHoraFin())) {
            throw new IllegalArgumentException("La reserva debe estar dentro del horario permitido.");
        }

        List<ReservaAreaComun> reservas = reservaAreaComunRepository
                .listarPorAreaYFecha(areaComun.getId(), fecha);

        for (ReservaAreaComun reserva : reservas) {
            if (!reserva.getEstado().equals("CANCELADA") && existeCruceHorario(horaInicio, horaFin, reserva.getHoraInicio(), reserva.getHoraFin())) {
                throw new IllegalArgumentException("El horario ya se encuentra reservado.");
            }
        }
    }

    private boolean existeCruceHorario(LocalTime inicioNueva, LocalTime finNueva,
            LocalTime inicioExistente, LocalTime finExistente) {
        return inicioNueva.isBefore(finExistente) && finNueva.isAfter(inicioExistente);
    }

    private ReservaAreaComunResponse convertirReservaResponse(ReservaAreaComun reserva) {
        ReservaAreaComunResponse respuesta = new ReservaAreaComunResponse(reserva.getId(),
                reserva.getAreaComun() != null ? reserva.getAreaComun().getId() : null,
                reserva.getUnidad() != null ? reserva.getUnidad().getId() : null,
                reserva.getFechaReserva(),
                reserva.getHoraInicio(),
                reserva.getHoraFin(),
                reserva.getResponsableNombre(),
                reserva.getFechaRegistro(),
                reserva.getEstado());
        if (reserva.getUnidad() != null) {
            respuesta.setUnidadNumero(reserva.getUnidad().getNumeroUnidad());
        }
        return respuesta;
    }

    private void validarReserva(ReservaAreaComunForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de reserva es obligatorio.");
        }
        if (formulario.getAreaComunId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un area comun.");
        }
        if (formulario.getUnidadId() == null) {
            throw new IllegalArgumentException("Debe seleccionar una unidad.");
        }
        if (formulario.getFechaReserva() == null) {
            throw new IllegalArgumentException("La fecha de reserva es obligatoria.");
        }
        if (formulario.getHoraInicio() == null || formulario.getHoraFin() == null) {
            throw new IllegalArgumentException("Debe indicar el horario de la reserva.");
        }
        if (formulario.getResponsableNombre() == null || formulario.getResponsableNombre().isBlank()) {
            throw new IllegalArgumentException("El responsable es obligatorio.");
        }
    }
}
