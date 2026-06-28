package pe.edu.utp.condominio.api.dominios.saludambiental.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.EvaluacionForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.EvaluacionResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.enums.ResultadoChecklist;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.ChecklistSaludAmbiente;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.EvaluacionChecklist;
import pe.edu.utp.condominio.api.dominios.saludambiental.repositories.ChecklistSaludAmbienteRepository;
import pe.edu.utp.condominio.api.dominios.saludambiental.repositories.EvaluacionChecklistRepository;

@Service
public class EvaluacionChecklistService {

    private final EvaluacionChecklistRepository evaluacionRepository;
    private final ChecklistSaludAmbienteRepository checklistRepository;

    public EvaluacionChecklistService(EvaluacionChecklistRepository evaluacionRepository,
            ChecklistSaludAmbienteRepository checklistRepository) {
        this.evaluacionRepository = evaluacionRepository;
        this.checklistRepository = checklistRepository;
    }

    @Transactional
    public EvaluacionResponse evaluarChecklist(EvaluacionForm formulario) {
        ChecklistSaludAmbiente checklist = checklistRepository.findById(formulario.getChecklistId())
                .orElseThrow(() -> new IllegalArgumentException("Checklist no encontrado"));

        EvaluacionChecklist evaluacion = new EvaluacionChecklist();
        evaluacion.setChecklist(checklist);
        evaluacion.setResultado(formulario.getResultado());
        evaluacion.setObservacion(formulario.getObservacion());
        evaluacion.setAlertaGenerada(formulario.getResultado() == ResultadoChecklist.NO_PASA);

        EvaluacionChecklist guardado = evaluacionRepository.save(evaluacion);
        return mapearEvaluacionAResponse(guardado);
    }

    private EvaluacionResponse mapearEvaluacionAResponse(EvaluacionChecklist entidad) {
        return new EvaluacionResponse(
                entidad.getId(),
                entidad.getChecklist().getNombre(),
                entidad.getChecklist().getAreaComun().getNombre(),
                entidad.getFechaEvaluacion(),
                entidad.getResultado(),
                entidad.getObservacion(),
                entidad.isAlertaGenerada());
    }
}
