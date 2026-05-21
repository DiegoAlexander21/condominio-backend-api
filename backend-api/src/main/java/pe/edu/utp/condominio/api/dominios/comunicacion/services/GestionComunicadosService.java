package pe.edu.utp.condominio.api.dominios.comunicacion.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.ComunicadoForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.ComunicadoIAForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.ComunicadoResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.models.Comunicado;
import pe.edu.utp.condominio.api.dominios.comunicacion.repositories.ComunicadoRepository;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;

@Service
public class GestionComunicadosService {

    private final ComunicadoRepository comunicadoRepository;
    private final CondominioRepository condominioRepository;
    private final IAComunicadosService servicioIAComunicados;

    public GestionComunicadosService(ComunicadoRepository comunicadoRepository,
            CondominioRepository condominioRepository,
            IAComunicadosService servicioIAComunicados) {
        this.comunicadoRepository = comunicadoRepository;
        this.condominioRepository = condominioRepository;
        this.servicioIAComunicados = servicioIAComunicados;
    }

    @Transactional
    public synchronized ComunicadoResponse registrarComunicado(ComunicadoForm form) {
        validarComunicado(form);

        Condominio condominio = condominioRepository.findById(form.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("El condominio no existe."));

        Comunicado comunicado = new Comunicado();
        comunicado.setCondominio(condominio);
        comunicado.setTitulo(form.getTitulo().trim());
        comunicado.setContenido(form.getContenido().trim());

        Comunicado guardado = comunicadoRepository.save(comunicado);
        return convertirComunicadoResponse(guardado);
    }

    @Transactional
    public synchronized ComunicadoResponse generarConIA(ComunicadoIAForm form) {
        validarComunicadoIA(form);

        Condominio condominio = condominioRepository.findById(form.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("El condominio no existe."));

        String contenido = servicioIAComunicados.generarComunicado(form.getTitulo(), form.getBorrador());

        Comunicado comunicado = new Comunicado();
        comunicado.setCondominio(condominio);
        comunicado.setTitulo(form.getTitulo().trim());
        comunicado.setContenido(contenido);

        Comunicado guardado = comunicadoRepository.save(comunicado);
        return convertirComunicadoResponse(guardado);
    }

    public synchronized List<ComunicadoResponse> listarPorCondominio(Long condominioId) {
        if (condominioId == null) {
            throw new IllegalArgumentException("Debe seleccionar un condominio valido.");
        }
        return comunicadoRepository.listarPorCondominio(condominioId).stream()
                .map(this::convertirComunicadoResponse)
                .collect(Collectors.toList());
    }

    private void validarComunicado(ComunicadoForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario del comunicado es obligatorio.");
        }
        if (form.getCondominioId() == null) {
            throw new IllegalArgumentException("El condominio es obligatorio.");
        }
        if (form.getTitulo() == null || form.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio.");
        }
        if (form.getContenido() == null || form.getContenido().isBlank()) {
            throw new IllegalArgumentException("El contenido es obligatorio.");
        }
    }

    private void validarComunicadoIA(ComunicadoIAForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario del comunicado IA es obligatorio.");
        }
        if (form.getCondominioId() == null) {
            throw new IllegalArgumentException("El condominio es obligatorio.");
        }
        if (form.getTitulo() == null || form.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio.");
        }
        if (form.getBorrador() == null || form.getBorrador().isBlank()) {
            throw new IllegalArgumentException("El borrador es obligatorio.");
        }
    }

    private ComunicadoResponse convertirComunicadoResponse(Comunicado comunicado) {
        return new ComunicadoResponse(comunicado.getId(),
                comunicado.getCondominio() != null ? comunicado.getCondominio().getId() : null,
                comunicado.getTitulo(),
                comunicado.getContenido(),
                comunicado.getFechaPublicacion());
    }
}

