package pe.edu.utp.condominio.api.dominios.comunicacion.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.AsambleaForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.AsambleaResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.OpcionVotacionResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.EstadoAsamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.Asamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.OpcionVotacion;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.AsambleaRepository;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.OpcionVotacionRepository;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;

@Service
public class AsambleaService {

    private final AsambleaRepository asambleaRepository;
    private final OpcionVotacionRepository opcionVotacionRepository;
    private final CondominioRepository condominioRepository;

    public AsambleaService(AsambleaRepository asambleaRepository,
                           OpcionVotacionRepository opcionVotacionRepository,
                           CondominioRepository condominioRepository) {
        this.asambleaRepository = asambleaRepository;
        this.opcionVotacionRepository = opcionVotacionRepository;
        this.condominioRepository = condominioRepository;
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
