package pe.edu.utp.condominio.api.dominios.comunicacion.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.comunicacion.dto.ComunicadoTorreDto;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.ComunicadoForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.ComunicadoIAForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.ComunicadoResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.enums.AlcanceComunicado;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.Comunicado;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.ComunicadoTorre;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.ComunicadoRepository;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class ComunicadoService {

    private final ComunicadoRepository comunicadoRepository;
    private final CondominioRepository condominioRepository;
    private final UnidadRepository unidadRepository;
    private final IAComunicadosService servicioIAComunicados;

    public ComunicadoService(ComunicadoRepository comunicadoRepository,
            CondominioRepository condominioRepository,
            UnidadRepository unidadRepository,
            IAComunicadosService servicioIAComunicados) {
        this.comunicadoRepository = comunicadoRepository;
        this.condominioRepository = condominioRepository;
        this.unidadRepository = unidadRepository;
        this.servicioIAComunicados = servicioIAComunicados;
    }

    @Transactional
    public synchronized ComunicadoResponse registrarComunicado(ComunicadoForm formulario) {
        validarComunicado(formulario);

        Comunicado comunicado = new Comunicado();
        mapearDestinos(comunicado, formulario.getAlcance(), formulario.getCondominioIds(), formulario.getTorres(),
                formulario.getUnidadIds());

        comunicado.setTitulo(formulario.getTitulo().trim());
        comunicado.setContenido(formulario.getContenido().trim());

        Comunicado guardado = comunicadoRepository.save(comunicado);
        return convertirComunicadoResponse(guardado);
    }

    public synchronized ComunicadoResponse generarConIA(ComunicadoIAForm formulario) {
        validarComunicadoIA(formulario);

        String contenido = servicioIAComunicados.generarComunicado(formulario.getTitulo(), formulario.getBorrador());

        Comunicado comunicado = new Comunicado();
        mapearDestinos(comunicado, formulario.getAlcance(), formulario.getCondominioIds(), formulario.getTorres(),
                formulario.getUnidadIds());

        comunicado.setTitulo(formulario.getTitulo().trim());
        comunicado.setContenido(contenido);

        return convertirComunicadoResponse(comunicado);
    }

    @Transactional(readOnly = true)
    public synchronized List<ComunicadoResponse> listarPorCondominio(Long condominioId) {
        if (condominioId == null) {
            throw new IllegalArgumentException("Debe proporcionar un condominio.");
        }
        return comunicadoRepository.findAll().stream()
                .map(this::convertirComunicadoResponse)
                .collect(Collectors.toList());
    }

    private void mapearDestinos(Comunicado comunicado, AlcanceComunicado alcance, List<Long> condIds,
            List<ComunicadoTorreDto> torresDto, List<Long> unidadIds) {
        comunicado.setAlcance(alcance);

        List<Condominio> condominios = condominioRepository.findAllById(condIds);
        if (condominios.isEmpty()) {
            throw new IllegalArgumentException("Los condominios proporcionados no son validos.");
        }
        comunicado.setCondominiosDestino(condominios);

        if (alcance == AlcanceComunicado.TORRE || alcance == AlcanceComunicado.UNIDAD) {
            if (torresDto == null || torresDto.isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar al menos una torre.");
            }
            List<ComunicadoTorre> torres = torresDto.stream()
                    .map(dto -> new ComunicadoTorre(dto.getCondominioId(), dto.getTorre()))
                    .collect(Collectors.toList());
            comunicado.setTorresDestino(torres);
        }

        if (alcance == AlcanceComunicado.UNIDAD) {
            if (unidadIds == null || unidadIds.isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar al menos una unidad.");
            }
            List<Unidad> unidades = unidadRepository.findAllById(unidadIds);
            if (unidades.isEmpty()) {
                throw new IllegalArgumentException("Las unidades proporcionadas no son validas.");
            }
            comunicado.setUnidadesDestino(unidades);
        }
    }

    private void validarComunicado(ComunicadoForm formulario) {
        if (formulario == null)
            throw new IllegalArgumentException("El formulario del comunicado es obligatorio.");
        if (formulario.getAlcance() == null)
            throw new IllegalArgumentException("El alcance es obligatorio.");
        if (formulario.getCondominioIds() == null || formulario.getCondominioIds().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un condominio.");
        }
        if (formulario.getTitulo() == null || formulario.getTitulo().isBlank())
            throw new IllegalArgumentException("El titulo es obligatorio.");
        if (formulario.getContenido() == null || formulario.getContenido().isBlank())
            throw new IllegalArgumentException("El contenido es obligatorio.");
    }

    private void validarComunicadoIA(ComunicadoIAForm formulario) {
        if (formulario == null)
            throw new IllegalArgumentException("El formulario del comunicado IA es obligatorio.");
        if (formulario.getAlcance() == null)
            throw new IllegalArgumentException("El alcance es obligatorio.");
        if (formulario.getCondominioIds() == null || formulario.getCondominioIds().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un condominio.");
        }
        if (formulario.getTitulo() == null || formulario.getTitulo().isBlank())
            throw new IllegalArgumentException("El titulo es obligatorio.");
        if (formulario.getBorrador() == null || formulario.getBorrador().isBlank())
            throw new IllegalArgumentException("El borrador es obligatorio.");
    }

    private ComunicadoResponse convertirComunicadoResponse(Comunicado comunicado) {
        ComunicadoResponse resp = new ComunicadoResponse();
        resp.setId(comunicado.getId());
        resp.setAlcance(comunicado.getAlcance());
        resp.setCondominioIds(
                comunicado.getCondominiosDestino().stream().map(Condominio::getId).collect(Collectors.toList()));

        List<ComunicadoTorreDto> torresDto = comunicado.getTorresDestino().stream()
                .map(t -> new ComunicadoTorreDto(t.getCondominioId(), t.getTorre()))
                .collect(Collectors.toList());
        resp.setTorres(torresDto);

        resp.setUnidadIds(comunicado.getUnidadesDestino().stream().map(Unidad::getId).collect(Collectors.toList()));
        resp.setTitulo(comunicado.getTitulo());
        resp.setContenido(comunicado.getContenido());
        resp.setFechaPublicacion(comunicado.getFechaPublicacion());
        return resp;
    }
}
