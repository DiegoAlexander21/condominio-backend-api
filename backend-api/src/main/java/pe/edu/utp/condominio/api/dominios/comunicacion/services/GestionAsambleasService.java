package pe.edu.utp.condominio.api.dominios.comunicacion.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.AsambleaForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.VotoAsambleaForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.AsambleaResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.OpcionResultadoResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.OpcionVotacionResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.ResultadoAsambleaResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.EstadoAsamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.Asamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.OpcionVotacion;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.VotoAsamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.AsambleaRepository;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.OpcionVotacionRepository;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.VotoAsambleaRepository;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class GestionAsambleasService {

    private final AsambleaRepository asambleaRepository;
    private final OpcionVotacionRepository opcionVotacionRepository;
    private final VotoAsambleaRepository votoAsambleaRepository;
    private final CondominioRepository condominioRepository;
    private final UnidadRepository unidadRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public GestionAsambleasService(AsambleaRepository asambleaRepository,
            OpcionVotacionRepository opcionVotacionRepository,
            VotoAsambleaRepository votoAsambleaRepository,
            CondominioRepository condominioRepository,
            UnidadRepository unidadRepository,
            SimpMessagingTemplate messagingTemplate) {
        this.asambleaRepository = asambleaRepository;
        this.opcionVotacionRepository = opcionVotacionRepository;
        this.votoAsambleaRepository = votoAsambleaRepository;
        this.condominioRepository = condominioRepository;
        this.unidadRepository = unidadRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public synchronized AsambleaResponse registrarAsamblea(AsambleaForm form) {
        validarAsamblea(form);

        Condominio condominio = condominioRepository.findById(form.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("El condominio no existe."));

        Asamblea asamblea = new Asamblea();
        asamblea.setCondominio(condominio);
        asamblea.setTitulo(form.getTitulo().trim());
        asamblea.setDescripcion(form.getDescripcion().trim());
        asamblea.setFechaInicio(form.getFechaInicio());
        asamblea.setFechaFin(form.getFechaFin());
        asamblea.setEstado(EstadoAsamblea.ABIERTA);

        Asamblea guardada = asambleaRepository.save(asamblea);
        List<OpcionVotacion> opciones = registrarOpciones(guardada, form.getOpciones());
        guardada.setOpciones(opciones);

        return convertirAsambleaResponse(guardada, opciones);
    }

    public synchronized List<AsambleaResponse> listarPorCondominio(Long condominioId) {
        if (condominioId == null) {
            throw new IllegalArgumentException("Debe seleccionar un condominio valido.");
        }
        return asambleaRepository.listarPorCondominio(condominioId).stream()
                .map(asamblea -> convertirAsambleaResponse(asamblea,
                        opcionVotacionRepository.listarPorAsamblea(asamblea.getId())))
                .collect(Collectors.toList());
    }

    public synchronized List<AsambleaResponse> listarPorEstado(EstadoAsamblea estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Debe seleccionar un estado.");
        }
        return asambleaRepository.listarPorEstado(estado).stream()
                .map(asamblea -> convertirAsambleaResponse(asamblea,
                        opcionVotacionRepository.listarPorAsamblea(asamblea.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public synchronized ResultadoAsambleaResponse registrarVoto(VotoAsambleaForm form) {
        validarVoto(form);

        Asamblea asamblea = asambleaRepository.findById(form.getAsambleaId())
                .orElseThrow(() -> new IllegalArgumentException("La asamblea no existe."));

        if (asamblea.getEstado() != EstadoAsamblea.ABIERTA) {
            throw new IllegalArgumentException("La asamblea no esta abierta.");
        }

        if (votoAsambleaRepository.existePorAsambleaYUnidad(asamblea.getId(), form.getUnidadId())) {
            throw new IllegalArgumentException("La unidad ya emitio su voto.");
        }

        OpcionVotacion opcion = opcionVotacionRepository.findById(form.getOpcionId())
                .orElseThrow(() -> new IllegalArgumentException("La opcion no existe."));

        if (!opcion.getAsamblea().getId().equals(asamblea.getId())) {
            throw new IllegalArgumentException("La opcion no pertenece a la asamblea.");
        }

        Unidad unidad = unidadRepository.findById(form.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        VotoAsamblea voto = new VotoAsamblea();
        voto.setAsamblea(asamblea);
        voto.setOpcion(opcion);
        voto.setUnidad(unidad);

        votoAsambleaRepository.save(voto);

        ResultadoAsambleaResponse resultado = obtenerResultados(asamblea.getId());
        messagingTemplate.convertAndSend("/topic/asambleas/" + asamblea.getId(), resultado);
        return resultado;
    }

    public synchronized ResultadoAsambleaResponse obtenerResultados(Long asambleaId) {
        if (asambleaId == null) {
            throw new IllegalArgumentException("La asamblea es obligatoria.");
        }

        List<OpcionVotacion> opciones = opcionVotacionRepository.listarPorAsamblea(asambleaId);
        Map<Long, Long> conteo = new HashMap<>();

        for (VotoAsamblea voto : votoAsambleaRepository.listarPorAsamblea(asambleaId)) {
            Long opcionId = voto.getOpcion().getId();
            conteo.put(opcionId, conteo.getOrDefault(opcionId, 0L) + 1);
        }

        List<OpcionResultadoResponse> resultados = new ArrayList<>();
        long total = 0;
        for (OpcionVotacion opcion : opciones) {
            long votos = conteo.getOrDefault(opcion.getId(), 0L);
            total += votos;
            resultados.add(new OpcionResultadoResponse(opcion.getId(), opcion.getTexto(), votos));
        }

        return new ResultadoAsambleaResponse(asambleaId, total, resultados);
    }

    private List<OpcionVotacion> registrarOpciones(Asamblea asamblea, List<String> opcionesTexto) {
        List<OpcionVotacion> opciones = new ArrayList<>();
        for (String texto : opcionesTexto) {
            OpcionVotacion opcion = new OpcionVotacion();
            opcion.setAsamblea(asamblea);
            opcion.setTexto(texto.trim());
            opciones.add(opcion);
        }
        return opcionVotacionRepository.saveAll(opciones);
    }

    private void validarAsamblea(AsambleaForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de asamblea es obligatorio.");
        }
        if (form.getCondominioId() == null) {
            throw new IllegalArgumentException("El condominio es obligatorio.");
        }
        if (form.getTitulo() == null || form.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio.");
        }
        if (form.getDescripcion() == null || form.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        if (form.getFechaInicio() == null || form.getFechaFin() == null) {
            throw new IllegalArgumentException("Debe registrar fecha de inicio y fin.");
        }
        if (!form.getFechaInicio().isBefore(form.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser menor que la de fin.");
        }
        if (form.getOpciones() == null || form.getOpciones().isEmpty()) {
            throw new IllegalArgumentException("Debe registrar opciones de votacion.");
        }
    }

    private void validarVoto(VotoAsambleaForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de voto es obligatorio.");
        }
        if (form.getAsambleaId() == null) {
            throw new IllegalArgumentException("La asamblea es obligatoria.");
        }
        if (form.getOpcionId() == null) {
            throw new IllegalArgumentException("La opcion es obligatoria.");
        }
        if (form.getUnidadId() == null) {
            throw new IllegalArgumentException("La unidad es obligatoria.");
        }
    }

    private AsambleaResponse convertirAsambleaResponse(Asamblea asamblea, List<OpcionVotacion> opciones) {
        List<OpcionVotacionResponse> opcionesResponse = opciones.stream()
                .map(opcion -> new OpcionVotacionResponse(opcion.getId(), opcion.getTexto()))
                .collect(Collectors.toList());

        return new AsambleaResponse(asamblea.getId(),
                asamblea.getCondominio() != null ? asamblea.getCondominio().getId() : null,
                asamblea.getTitulo(),
                asamblea.getDescripcion(),
                asamblea.getFechaInicio(),
                asamblea.getFechaFin(),
                asamblea.getEstado(),
                opcionesResponse);
    }
}

