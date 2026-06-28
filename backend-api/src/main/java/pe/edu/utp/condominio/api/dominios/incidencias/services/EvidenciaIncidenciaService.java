package pe.edu.utp.condominio.api.dominios.incidencias.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.EvidenciaIncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.response.EvidenciaIncidenciaResponse;
import pe.edu.utp.condominio.api.dominios.incidencias.models.EvidenciaIncidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.models.Incidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.EvidenciaIncidenciaRepository;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.IncidenciaRepository;

@Service
public class EvidenciaIncidenciaService {

    private final EvidenciaIncidenciaRepository evidenciaIncidenciaRepository;
    private final IncidenciaRepository incidenciaRepository;

    public EvidenciaIncidenciaService(EvidenciaIncidenciaRepository evidenciaIncidenciaRepository,
            IncidenciaRepository incidenciaRepository) {
        this.evidenciaIncidenciaRepository = evidenciaIncidenciaRepository;
        this.incidenciaRepository = incidenciaRepository;
    }

    @Transactional
    public synchronized EvidenciaIncidenciaResponse registrarEvidencia(EvidenciaIncidenciaForm formulario) {
        validarEvidencia(formulario);

        Incidencia incidencia = incidenciaRepository.findById(formulario.getIncidenciaId())
                .orElseThrow(() -> new IllegalArgumentException("La incidencia no existe."));

        EvidenciaIncidencia evidencia = new EvidenciaIncidencia();
        evidencia.setIncidencia(incidencia);
        evidencia.setUrlArchivo(formulario.getUrlArchivo().trim());

        EvidenciaIncidencia guardada = evidenciaIncidenciaRepository.save(evidencia);
        return convertirEvidenciaResponse(guardada);
    }

    @Transactional(readOnly = true)
    public synchronized List<EvidenciaIncidenciaResponse> listarEvidencias(Long incidenciaId) {
        if (incidenciaId == null) {
            throw new IllegalArgumentException("Debe seleccionar una incidencia valida.");
        }
        return evidenciaIncidenciaRepository.listarPorIncidencia(incidenciaId).stream()
                .map(this::convertirEvidenciaResponse)
                .collect(Collectors.toList());
    }

    private void validarEvidencia(EvidenciaIncidenciaForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de evidencia es obligatorio.");
        }
        if (formulario.getIncidenciaId() == null) {
            throw new IllegalArgumentException("La incidencia es obligatoria.");
        }
        if (formulario.getUrlArchivo() == null || formulario.getUrlArchivo().isBlank()) {
            throw new IllegalArgumentException("La URL del archivo es obligatoria.");
        }
    }

    private EvidenciaIncidenciaResponse convertirEvidenciaResponse(EvidenciaIncidencia evidencia) {
        return new EvidenciaIncidenciaResponse(evidencia.getId(),
                evidencia.getIncidencia() != null ? evidencia.getIncidencia().getId() : null,
                evidencia.getUrlArchivo(),
                evidencia.getFechaRegistro());
    }
}
