package pe.edu.utp.condominio.api.dominios.comunicacion.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.VotoAsambleaForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.OpcionResultadoResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.ResultadoAsambleaResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.EstadoAsamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.Asamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.OpcionVotacion;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.VotoAsamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.AsambleaRepository;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.OpcionVotacionRepository;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.VotoAsambleaRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class VotacionAsambleaService {

    private final AsambleaRepository asambleaRepository;
    private final OpcionVotacionRepository opcionVotacionRepository;
    private final VotoAsambleaRepository votoAsambleaRepository;
    private final UnidadRepository unidadRepository;
    private final SimpMessagingTemplate plantillaMensajeria;

    public VotacionAsambleaService(AsambleaRepository asambleaRepository,
                                   OpcionVotacionRepository opcionVotacionRepository,
                                   VotoAsambleaRepository votoAsambleaRepository,
                                   UnidadRepository unidadRepository,
                                   SimpMessagingTemplate plantillaMensajeria) {
        this.asambleaRepository = asambleaRepository;
        this.opcionVotacionRepository = opcionVotacionRepository;
        this.votoAsambleaRepository = votoAsambleaRepository;
        this.unidadRepository = unidadRepository;
        this.plantillaMensajeria = plantillaMensajeria;
    }

    @Transactional
    public synchronized ResultadoAsambleaResponse registrarVoto(VotoAsambleaForm formulario) {
        validarVoto(formulario);

        Asamblea asamblea = asambleaRepository.findById(formulario.getAsambleaId())
                .orElseThrow(() -> new IllegalArgumentException("La asamblea no existe."));

        if (asamblea.getEstado() != EstadoAsamblea.ABIERTA) {
            throw new IllegalArgumentException("La asamblea no esta abierta.");
        }

        OpcionVotacion opcion = opcionVotacionRepository.findById(formulario.getOpcionId())
                .orElseThrow(() -> new IllegalArgumentException("La opcion no existe."));

        if (!opcion.getAsamblea().getId().equals(asamblea.getId())) {
            throw new IllegalArgumentException("La opcion no pertenece a la asamblea.");
        }

        Unidad unidad = unidadRepository.findById(formulario.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));

        VotoAsamblea voto = votoAsambleaRepository.obtenerPorAsambleaYUnidad(asamblea.getId(), formulario.getUnidadId())
                .orElseGet(() -> {
                    VotoAsamblea nuevoVoto = new VotoAsamblea();
                    nuevoVoto.setAsamblea(asamblea);
                    nuevoVoto.setUnidad(unidad);
                    return nuevoVoto;
                });
                
        voto.setOpcion(opcion);
        votoAsambleaRepository.save(voto);

        ResultadoAsambleaResponse resultado = obtenerResultados(asamblea.getId());
        plantillaMensajeria.convertAndSend("/topic/asambleas/" + asamblea.getId(), resultado);
        return resultado;
    }

    public synchronized ResultadoAsambleaResponse obtenerResultados(Long asambleaId) {
        if (asambleaId == null) {
            throw new IllegalArgumentException("La asamblea es obligatoria.");
        }

        Asamblea asamblea = asambleaRepository.findById(asambleaId)
                .orElseThrow(() -> new IllegalArgumentException("La asamblea no existe."));

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

        return new ResultadoAsambleaResponse(asambleaId, total, asamblea.getEstado().name(), resultados);
    }

    public void notificarResultados(Long asambleaId) {
        ResultadoAsambleaResponse resultado = obtenerResultados(asambleaId);
        plantillaMensajeria.convertAndSend("/topic/asambleas/" + asambleaId, resultado);
    }

    public synchronized boolean tieneVotoRegistrado(Long asambleaId, Long unidadId) {
        if (asambleaId == null || unidadId == null) {
            throw new IllegalArgumentException("Asamblea y Unidad son obligatorios.");
        }
        return votoAsambleaRepository.existePorAsambleaYUnidad(asambleaId, unidadId);
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
}
