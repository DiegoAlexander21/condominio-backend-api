package pe.edu.utp.condominio.api.dominios.areascomunes.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.AreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.ReservaAreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.response.AreaComunResponse;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.response.ReservaAreaComunResponse;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.ReservaAreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.ReservaAreaComunRepository;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class GestionAreasComunesService {

    private final AreaComunRepository areaComunRepository;
    private final ReservaAreaComunRepository reservaAreaComunRepository;
    private final CondominioRepository condominioRepository;
    private final UnidadRepository unidadRepository;

    public GestionAreasComunesService(AreaComunRepository areaComunRepository,
            ReservaAreaComunRepository reservaAreaComunRepository,
            CondominioRepository condominioRepository,
            UnidadRepository unidadRepository) {
        this.areaComunRepository = areaComunRepository;
        this.reservaAreaComunRepository = reservaAreaComunRepository;
        this.condominioRepository = condominioRepository;
        this.unidadRepository = unidadRepository;
    }

    @Transactional
    public synchronized AreaComunResponse registrarOActualizarArea(AreaComunForm form) {
        validarArea(form);

        Condominio condominio = condominioRepository.findById(form.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("Condominio no encontrado."));

        AreaComun areaComun = obtenerAreaParaRegistro(form, condominio.getId());
        areaComun.setCondominio(condominio);
        areaComun.setNombre(form.getNombre().trim());
        areaComun.setCapacidad(form.getCapacidad());
        areaComun.setHoraInicio(form.getHoraInicio());
        areaComun.setHoraFin(form.getHoraFin());
        areaComun.setNormasUso(normalizarTexto(form.getNormasUso()));

        AreaComun guardada = areaComunRepository.save(areaComun);
        return convertirAreaResponse(guardada);
    }

    public synchronized List<AreaComunResponse> listarPorCondominio(Long condominioId) {
        if (condominioId == null) {
            throw new IllegalArgumentException("Debe seleccionar un condominio valido.");
        }
        return areaComunRepository.listarPorCondominio(condominioId).stream()
                .map(this::convertirAreaResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public synchronized ReservaAreaComunResponse registrarReserva(ReservaAreaComunForm form) {
        validarReserva(form);

        AreaComun areaComun = areaComunRepository.findById(form.getAreaComunId())
                .orElseThrow(() -> new IllegalArgumentException("El area comun no existe."));
        Unidad unidad = unidadRepository.findById(form.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        validarHorarioDisponible(areaComun, form.getFechaReserva(),
                form.getHoraInicio(), form.getHoraFin());

        ReservaAreaComun reserva = new ReservaAreaComun();
        reserva.setAreaComun(areaComun);
        reserva.setUnidad(unidad);
        reserva.setFechaReserva(form.getFechaReserva());
        reserva.setHoraInicio(form.getHoraInicio());
        reserva.setHoraFin(form.getHoraFin());
        reserva.setResponsableNombre(form.getResponsableNombre().trim());

        ReservaAreaComun guardada = reservaAreaComunRepository.save(reserva);
        return convertirReservaResponse(guardada);
    }

    public synchronized List<ReservaAreaComunResponse> listarReservas(Long areaComunId, LocalDate fecha) {
        if (areaComunId == null) {
            throw new IllegalArgumentException("Debe seleccionar un area comun valida.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("Debe indicar la fecha de reserva.");
        }
        return reservaAreaComunRepository.listarPorAreaYFecha(areaComunId, fecha).stream()
                .map(this::convertirReservaResponse)
                .collect(Collectors.toList());
    }

    private AreaComun obtenerAreaParaRegistro(AreaComunForm form, Long condominioId) {
        if (form.getId() != null) {
            return areaComunRepository.findById(form.getId())
                    .orElseThrow(() -> new IllegalArgumentException("El area comun no existe."));
        }

        AreaComun existente = areaComunRepository.buscarPorNombre(condominioId, form.getNombre().trim());
        return existente != null ? existente : new AreaComun();
    }

    private void validarHorarioDisponible(AreaComun areaComun, LocalDate fecha, LocalTime horaInicio,
            LocalTime horaFin) {
        if (horaInicio.isBefore(areaComun.getHoraInicio()) || horaFin.isAfter(areaComun.getHoraFin())) {
            throw new IllegalArgumentException("La reserva debe estar dentro del horario permitido.");
        }

        List<ReservaAreaComun> reservas = reservaAreaComunRepository
                .listarPorAreaYFecha(areaComun.getId(), fecha);

        for (ReservaAreaComun reserva : reservas) {
            if (existeCruceHorario(horaInicio, horaFin, reserva.getHoraInicio(), reserva.getHoraFin())) {
                throw new IllegalArgumentException("El horario ya se encuentra reservado.");
            }
        }
    }

    private boolean existeCruceHorario(LocalTime inicioNueva, LocalTime finNueva,
            LocalTime inicioExistente, LocalTime finExistente) {
        return inicioNueva.isBefore(finExistente) && finNueva.isAfter(inicioExistente);
    }

    private AreaComunResponse convertirAreaResponse(AreaComun areaComun) {
        return new AreaComunResponse(areaComun.getId(),
                areaComun.getCondominio() != null ? areaComun.getCondominio().getId() : null,
                areaComun.getNombre(),
                areaComun.getCapacidad(),
                areaComun.getHoraInicio(),
                areaComun.getHoraFin(),
                areaComun.getNormasUso());
    }

    private ReservaAreaComunResponse convertirReservaResponse(ReservaAreaComun reserva) {
        return new ReservaAreaComunResponse(reserva.getId(),
                reserva.getAreaComun() != null ? reserva.getAreaComun().getId() : null,
                reserva.getUnidad() != null ? reserva.getUnidad().getId() : null,
                reserva.getFechaReserva(),
                reserva.getHoraInicio(),
                reserva.getHoraFin(),
                reserva.getResponsableNombre(),
                reserva.getFechaRegistro());
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private void validarArea(AreaComunForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario del area comun es obligatorio.");
        }
        if (form.getCondominioId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un condominio.");
        }
        if (form.getNombre() == null || form.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del area comun es obligatorio.");
        }
        if (form.getCapacidad() == null || form.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
        }
        if (form.getHoraInicio() == null || form.getHoraFin() == null) {
            throw new IllegalArgumentException("Debe indicar el horario disponible.");
        }
        if (!form.getHoraInicio().isBefore(form.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin.");
        }
    }

    private void validarReserva(ReservaAreaComunForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de reserva es obligatorio.");
        }
        if (form.getAreaComunId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un area comun.");
        }
        if (form.getUnidadId() == null) {
            throw new IllegalArgumentException("Debe seleccionar una unidad.");
        }
        if (form.getFechaReserva() == null) {
            throw new IllegalArgumentException("La fecha de reserva es obligatoria.");
        }
        if (form.getHoraInicio() == null || form.getHoraFin() == null) {
            throw new IllegalArgumentException("Debe indicar el horario de la reserva.");
        }
        if (!form.getHoraInicio().isBefore(form.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin.");
        }
        if (form.getResponsableNombre() == null || form.getResponsableNombre().isBlank()) {
            throw new IllegalArgumentException("El responsable es obligatorio.");
        }
    }
}

