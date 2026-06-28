package pe.edu.utp.condominio.api.dominios.saludambiental.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.ChecklistForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.ChecklistResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.ItemChecklistResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.ChecklistSaludAmbiente;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.ItemChecklist;
import pe.edu.utp.condominio.api.dominios.saludambiental.repositories.ChecklistSaludAmbienteRepository;

@Service
public class ChecklistService {

    private final ChecklistSaludAmbienteRepository checklistRepository;
    private final AreaComunRepository areaComunRepository;

    public ChecklistService(ChecklistSaludAmbienteRepository checklistRepository,
            AreaComunRepository areaComunRepository) {
        this.checklistRepository = checklistRepository;
        this.areaComunRepository = areaComunRepository;
    }

    @Transactional
    public ChecklistResponse crearChecklist(ChecklistForm formulario) {
        AreaComun area = areaComunRepository.findById(formulario.getAreaComunId())
                .orElseThrow(() -> new IllegalArgumentException("Area comun no encontrada"));

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

    @Transactional(readOnly = true)
    public List<ChecklistResponse> listarChecklistsPorArea(Long areaId) {
        return checklistRepository.listarPorArea(areaId).stream()
                .map(this::mapearChecklistAResponse)
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
}
