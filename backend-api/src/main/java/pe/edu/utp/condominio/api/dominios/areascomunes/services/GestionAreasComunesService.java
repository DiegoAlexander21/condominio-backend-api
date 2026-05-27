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
    public synchronized AreaComunResponse registrarOActualizarArea(AreaComunForm formulario) {
        validarArea(formulario);

        Condominio condominio = condominioRepository.findById(formulario.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("Condominio no encontrado."));

        AreaComun existente = areaComunRepository.buscarPorNombre(condominio.getId(), formulario.getNombre().trim());
        if (existente != null && !existente.getId().equals(formulario.getId())) {
            throw new IllegalArgumentException(
                    "Ya existe un área común con el nombre '" + formulario.getNombre() + "' en este condominio.");
        }

        AreaComun areaComun = obtenerAreaParaRegistro(formulario, condominio.getId());
        areaComun.setCondominio(condominio);
        areaComun.setNombre(formulario.getNombre().trim());
        areaComun.setCapacidad(formulario.getCapacidad());
        areaComun.setHoraInicio(formulario.getHoraInicio());
        areaComun.setHoraFin(formulario.getHoraFin());
        areaComun.setNormasUso(normalizarTexto(formulario.getNormasUso()));

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

    public synchronized List<AreaComunResponse> obtenerTodasLasAreasComunes() {
        return areaComunRepository.listarTodosConCondominio().stream()
                .map(this::convertirAreaResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public synchronized void eliminarArea(Long id) {
        if (!areaComunRepository.existsById(id)) {
            throw new IllegalArgumentException("El área común no existe.");
        }
        areaComunRepository.deleteById(id);
    }

    public synchronized AreaComunForm obtenerFormularioArea(Long id) {
        AreaComun area = areaComunRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El área común no existe."));

        AreaComunForm formulario = new AreaComunForm();
        formulario.setId(area.getId());
        formulario.setCondominioId(area.getCondominio().getId());
        formulario.setNombre(area.getNombre());
        formulario.setCapacidad(area.getCapacidad());
        formulario.setHoraInicio(area.getHoraInicio());
        formulario.setHoraFin(area.getHoraFin());
        formulario.setNormasUso(area.getNormasUso());
        return formulario;
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

    private AreaComun obtenerAreaParaRegistro(AreaComunForm form, Long condominioId) {
        if (form.getId() != null) {
            return areaComunRepository.findById(form.getId())
                    .orElseThrow(() -> new IllegalArgumentException("El área común no existe."));
        }
        return new AreaComun();
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
        ReservaAreaComunResponse respuesta = new ReservaAreaComunResponse(reserva.getId(),
                reserva.getAreaComun() != null ? reserva.getAreaComun().getId() : null,
                reserva.getUnidad() != null ? reserva.getUnidad().getId() : null,
                reserva.getFechaReserva(),
                reserva.getHoraInicio(),
                reserva.getHoraFin(),
                reserva.getResponsableNombre(),
                reserva.getFechaRegistro());
        if (reserva.getUnidad() != null) {
            respuesta.setUnidadNumero(reserva.getUnidad().getNumeroUnidad());
        }
        return respuesta;
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private void validarArea(AreaComunForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario del area comun es obligatorio.");
        }
        if (formulario.getCondominioId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un condominio.");
        }
        if (formulario.getNombre() == null || formulario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del area comun es obligatorio.");
        }
        if (formulario.getCapacidad() == null || formulario.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
        }
        if (formulario.getHoraInicio() == null || formulario.getHoraFin() == null) {
            throw new IllegalArgumentException("Debe indicar el horario disponible.");
        }
        if (!formulario.getHoraInicio().isBefore(formulario.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin.");
        }
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
        if (!formulario.getHoraInicio().isBefore(formulario.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin.");
        }
        if (formulario.getResponsableNombre() == null || formulario.getResponsableNombre().isBlank()) {
            throw new IllegalArgumentException("El responsable es obligatorio.");
        }
    }
}
