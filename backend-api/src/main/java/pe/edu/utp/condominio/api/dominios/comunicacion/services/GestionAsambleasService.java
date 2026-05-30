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
    private final SimpMessagingTemplate plantillaMensajeria;

    public GestionAsambleasService(AsambleaRepository asambleaRepository,
            OpcionVotacionRepository opcionVotacionRepository,
            VotoAsambleaRepository votoAsambleaRepository,
            CondominioRepository condominioRepository,
            UnidadRepository unidadRepository,
            SimpMessagingTemplate plantillaMensajeria) {
        this.asambleaRepository = asambleaRepository;
        this.opcionVotacionRepository = opcionVotacionRepository;
        this.votoAsambleaRepository = votoAsambleaRepository;
        this.condominioRepository = condominioRepository;
        this.unidadRepository = unidadRepository;
        this.plantillaMensajeria = plantillaMensajeria;
    }

    @Transactional
    public synchronized AsambleaResponse registrarAsamblea(AsambleaForm formulario) {
        validarAsamblea(formulario);

        Condominio condominio = condominioRepository.findById(formulario.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("El condominio no existe."));

        Asamblea asamblea = new Asamblea();
        asamblea.setCondominio(condominio);
        asamblea.setTitulo(formulario.getTitulo().trim());
        asamblea.setDescripcion(formulario.getDescripcion().trim());
        asamblea.setFechaInicio(formulario.getFechaInicio());
        asamblea.setFechaFin(formulario.getFechaFin());
        asamblea.setEstado(EstadoAsamblea.ABIERTA);

        Asamblea guardada = asambleaRepository.save(asamblea);
        List<OpcionVotacion> opciones = registrarOpciones(guardada, formulario.getOpciones());
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
    public synchronized ResultadoAsambleaResponse registrarVoto(VotoAsambleaForm formulario) {
        validarVoto(formulario);

        Asamblea asamblea = asambleaRepository.findById(formulario.getAsambleaId())
                .orElseThrow(() -> new IllegalArgumentException("La asamblea no existe."));

        if (asamblea.getEstado() != EstadoAsamblea.ABIERTA) {
            throw new IllegalArgumentException("La asamblea no esta abierta.");
        }

        if (votoAsambleaRepository.existePorAsambleaYUnidad(asamblea.getId(), formulario.getUnidadId())) {
            throw new IllegalArgumentException("La unidad ya emitio su voto.");
        }

        OpcionVotacion opcion = opcionVotacionRepository.findById(formulario.getOpcionId())
                .orElseThrow(() -> new IllegalArgumentException("La opcion no existe."));

        if (!opcion.getAsamblea().getId().equals(asamblea.getId())) {
            throw new IllegalArgumentException("La opcion no pertenece a la asamblea.");
        }

        Unidad unidad = unidadRepository.findById(formulario.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        VotoAsamblea voto = new VotoAsamblea();
        voto.setAsamblea(asamblea);
        voto.setOpcion(opcion);
        voto.setUnidad(unidad);

        votoAsambleaRepository.save(voto);

        ResultadoAsambleaResponse resultado = obtenerResultados(asamblea.getId());
        plantillaMensajeria.convertAndSend("/topic/asambleas/" + asamblea.getId(), resultado);
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

    private void validarAsamblea(AsambleaForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de asamblea es obligatorio.");
        }
        if (formulario.getCondominioId() == null) {
            throw new IllegalArgumentException("El condominio es obligatorio.");
        }
        if (formulario.getTitulo() == null || formulario.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio.");
        }
        if (formulario.getDescripcion() == null || formulario.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        if (formulario.getFechaInicio() == null || formulario.getFechaFin() == null) {
            throw new IllegalArgumentException("Debe registrar fecha de inicio y fin.");
        }
        if (!formulario.getFechaInicio().isBefore(formulario.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser menor que la de fin.");
        }
        if (formulario.getOpciones() == null || formulario.getOpciones().isEmpty()) {
            throw new IllegalArgumentException("Debe registrar opciones de votacion.");
        }
    }

    private void validarVoto(VotoAsambleaForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de voto es obligatorio.");
        }
        if (formulario.getAsambleaId() == null) {
            throw new IllegalArgumentException("La asamblea es obligatoria.");
        }
        if (formulario.getOpcionId() == null) {
            throw new IllegalArgumentException("La opcion es obligatoria.");
        }
        if (formulario.getUnidadId() == null) {
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

