package pe.edu.utp.condominio.api.dominios.saludambiental.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.ChecklistForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.EvaluacionForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.MantenimientoAmbientalForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.ChecklistResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.EvaluacionResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.ItemChecklistResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.MantenimientoAmbientalResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.enums.ResultadoChecklist;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.ChecklistSaludAmbiente;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.EvaluacionChecklist;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.ItemChecklist;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.RegistroMantenimientoAmbiental;
import pe.edu.utp.condominio.api.dominios.saludambiental.repositories.ChecklistSaludAmbienteRepository;
import pe.edu.utp.condominio.api.dominios.saludambiental.repositories.EvaluacionChecklistRepository;
import pe.edu.utp.condominio.api.dominios.saludambiental.repositories.RegistroMantenimientoAmbientalRepository;

@Service
public class GestionSaludAmbientalService {

    private final ChecklistSaludAmbienteRepository checklistRepository;
    private final EvaluacionChecklistRepository evaluacionRepository;
    private final RegistroMantenimientoAmbientalRepository mantenimientoRepository;
    private final AreaComunRepository areaComunRepository;

    public GestionSaludAmbientalService(
            ChecklistSaludAmbienteRepository checklistRepository,
            EvaluacionChecklistRepository evaluacionRepository,
            RegistroMantenimientoAmbientalRepository mantenimientoRepository,
            AreaComunRepository areaComunRepository) {
        this.checklistRepository = checklistRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.mantenimientoRepository = mantenimientoRepository;
        this.areaComunRepository = areaComunRepository;
    }

    @Transactional
    public ChecklistResponse crearChecklist(ChecklistForm formulario) {
        AreaComun area = areaComunRepository.findById(formulario.getAreaComunId())
                .orElseThrow(() -> new RuntimeException("Ãrea comÃºn no encontrada"));

        ChecklistSaludAmbiente checklist = new ChecklistSaludAmbiente();
        checklist.setNombre(formulario.getNombre());
        checklist.setAreaComun(area);
        checklist.setActivo(true);

        List<ItemChecklist> items = formulario.getItems().stream().map(formularioItem -> {
            ItemChecklist item = new ItemChecklist();
            item.setDescripcion(formularioItem.getDescripcion());
            item.setOrden(formularioItem.getOrden());
            item.setChecklist(checklist);
            return item;
        }).collect(Collectors.toList());

        checklist.setItems(items);
        ChecklistSaludAmbiente guardado = checklistRepository.save(checklist);
        return mapearChecklistAResponse(guardado);
    }

    @Transactional
    public EvaluacionResponse evaluarChecklist(EvaluacionForm formulario) {
        ChecklistSaludAmbiente checklist = checklistRepository.findById(formulario.getChecklistId())
                .orElseThrow(() -> new RuntimeException("Checklist no encontrado"));

        EvaluacionChecklist evaluacion = new EvaluacionChecklist();
        evaluacion.setChecklist(checklist);
        evaluacion.setResultado(formulario.getResultado());
        evaluacion.setObservacion(formulario.getObservacion());
        evaluacion.setAlertaGenerada(formulario.getResultado() == ResultadoChecklist.NO_PASA);

        EvaluacionChecklist guardado = evaluacionRepository.save(evaluacion);
        return mapearEvaluacionAResponse(guardado);
    }

    @Transactional
    public MantenimientoAmbientalResponse registrarMantenimiento(MantenimientoAmbientalForm formulario) {
        AreaComun area = areaComunRepository.findById(formulario.getAreaComunId())
                .orElseThrow(() -> new RuntimeException("Ãrea comÃºn no encontrada"));

        RegistroMantenimientoAmbiental registro = new RegistroMantenimientoAmbiental();
        registro.setAreaComun(area);
        registro.setDescripcion(formulario.getDescripcion());
        registro.setResponsable(formulario.getResponsable());

        RegistroMantenimientoAmbiental guardado = mantenimientoRepository.save(registro);
        return mapearMantenimientoAResponse(guardado);
    }

    public List<ChecklistResponse> listarChecklistsPorArea(Long areaId) {
        return checklistRepository.listarPorArea(areaId).stream()
                .map(this::mapearChecklistAResponse)
                .collect(Collectors.toList());
    }

    public List<MantenimientoAmbientalResponse> obtenerHistorialMantenimiento(Long areaId) {
        return mantenimientoRepository.listarPorArea(areaId).stream()
                .map(this::mapearMantenimientoAResponse)
                .collect(Collectors.toList());
    }

    private ChecklistResponse mapearChecklistAResponse(ChecklistSaludAmbiente entidad) {
        List<ItemChecklistResponse> items = entidad.getItems().stream()
                .map(item -> new ItemChecklistResponse(item.getId(), item.getDescripcion(), item.getOrden()))
                .collect(Collectors.toList());

        return new ChecklistResponse(
                entidad.getId(),
                entidad.getAreaComun().getId(),
                entidad.getAreaComun().getNombre(),
                entidad.getNombre(),
                entidad.isActivo(),
                entidad.getFechaRegistro(),
                items);
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

    private MantenimientoAmbientalResponse mapearMantenimientoAResponse(RegistroMantenimientoAmbiental entidad) {
        return new MantenimientoAmbientalResponse(
                entidad.getId(),
                entidad.getAreaComun().getNombre(),
                entidad.getDescripcion(),
                entidad.getFechaRegistro(),
                entidad.getResponsable());
    }
}

