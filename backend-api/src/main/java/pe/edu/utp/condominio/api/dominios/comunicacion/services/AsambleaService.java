package pe.edu.utp.condominio.api.dominios.comunicacion.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.AsambleaForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.AsambleaResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.OpcionVotacionResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.ComunicadoTorreDto;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.AlcanceComunicado;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.EstadoAsamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.Asamblea;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.AsambleaTorre;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.OpcionVotacion;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.AsambleaRepository;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.OpcionVotacionRepository;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

import org.springframework.context.annotation.Lazy;

@Service
public class AsambleaService {

    private final AsambleaRepository asambleaRepository;
    private final OpcionVotacionRepository opcionVotacionRepository;
    private final CondominioRepository condominioRepository;
    private final UnidadRepository unidadRepository;
    private final VotacionAsambleaService votacionAsambleaService;

    public AsambleaService(AsambleaRepository asambleaRepository,
                           OpcionVotacionRepository opcionVotacionRepository,
                           CondominioRepository condominioRepository,
                           UnidadRepository unidadRepository,
                           @Lazy VotacionAsambleaService votacionAsambleaService) {
        this.asambleaRepository = asambleaRepository;
        this.opcionVotacionRepository = opcionVotacionRepository;
        this.condominioRepository = condominioRepository;
        this.unidadRepository = unidadRepository;
        this.votacionAsambleaService = votacionAsambleaService;
    }

    @Transactional
    public synchronized AsambleaResponse registrarAsamblea(AsambleaForm formulario) {
        validarAsamblea(formulario);

        Asamblea asamblea = new Asamblea();
        mapearDestinos(asamblea, formulario.getAlcance(), formulario.getCondominioIds(),
                       formulario.getTorres(), formulario.getUnidadIds());
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

    @Transactional(readOnly = true)
    public synchronized List<AsambleaResponse> listarPorCondominio(Long condominioId) {
        if (condominioId == null) {
            throw new IllegalArgumentException("Debe seleccionar un condominio valido.");
        }
        return asambleaRepository.listarPorCondominio(condominioId).stream()
                .map(asamblea -> convertirAsambleaResponse(asamblea,
                        opcionVotacionRepository.listarPorAsamblea(asamblea.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public synchronized AsambleaResponse obtenerPorId(Long id) {
        return asambleaRepository.findById(id)
                .map(asamblea -> convertirAsambleaResponse(asamblea,
                        opcionVotacionRepository.listarPorAsamblea(asamblea.getId())))
                .orElseThrow(() -> new IllegalArgumentException("Asamblea no encontrada."));
    }

    @Transactional
    public synchronized AsambleaResponse terminarAsamblea(Long id) {
        Asamblea asamblea = asambleaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asamblea no encontrada."));
        
        if (asamblea.getEstado() == EstadoAsamblea.CERRADA) {
            throw new IllegalArgumentException("La asamblea ya se encuentra terminada.");
        }
        
        asamblea.setEstado(EstadoAsamblea.CERRADA);
        Asamblea guardada = asambleaRepository.save(asamblea);
        
        votacionAsambleaService.notificarResultados(guardada.getId());
        
        return convertirAsambleaResponse(guardada, opcionVotacionRepository.listarPorAsamblea(guardada.getId()));
    }

    @Transactional(readOnly = true)
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

    private void mapearDestinos(Asamblea asamblea, AlcanceComunicado alcance, List<Long> condIds,
                                List<ComunicadoTorreDto> torresDto, List<Long> unidadIds) {
        asamblea.setAlcance(alcance);

        List<Condominio> condominios = condominioRepository.findAllById(condIds);
        if (condominios.isEmpty()) {
            throw new IllegalArgumentException("Los condominios proporcionados no son validos.");
        }
        asamblea.setCondominiosDestino(condominios);

        if (alcance == AlcanceComunicado.TORRE || alcance == AlcanceComunicado.UNIDAD) {
            if (torresDto == null || torresDto.isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar al menos una torre.");
            }
            List<AsambleaTorre> torres = torresDto.stream()
                    .map(dto -> new AsambleaTorre(dto.getCondominioId(), dto.getTorre()))
                    .collect(Collectors.toList());
            asamblea.setTorresDestino(torres);
        }

        if (alcance == AlcanceComunicado.UNIDAD) {
            if (unidadIds == null || unidadIds.isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar al menos una unidad.");
            }
            List<Unidad> unidades = unidadRepository.findAllById(unidadIds);
            if (unidades.isEmpty()) {
                throw new IllegalArgumentException("Las unidades proporcionadas no son validas.");
            }
            asamblea.setUnidadesDestino(unidades);
        }
    }

    private void validarAsamblea(AsambleaForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de asamblea es obligatorio.");
        }
        if (formulario.getAlcance() == null) {
            throw new IllegalArgumentException("El alcance es obligatorio.");
        }
        if (formulario.getCondominioIds() == null || formulario.getCondominioIds().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un condominio.");
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

        List<Long> condominioIds = asamblea.getCondominiosDestino().stream()
                .map(Condominio::getId).collect(Collectors.toList());
        List<ComunicadoTorreDto> torresDto = asamblea.getTorresDestino().stream()
                .map(t -> new ComunicadoTorreDto(t.getCondominioId(), t.getTorre()))
                .collect(Collectors.toList());
        List<Long> unidadIds = asamblea.getUnidadesDestino().stream()
                .map(Unidad::getId).collect(Collectors.toList());

        return new AsambleaResponse(asamblea.getId(),
                condominioIds,
                asamblea.getAlcance(),
                torresDto,
                unidadIds,
                asamblea.getTitulo(),
                asamblea.getDescripcion(),
                asamblea.getFechaInicio(),
                asamblea.getFechaFin(),
                asamblea.getEstado(),
                opcionesResponse);
    }
}
