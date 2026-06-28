package pe.edu.utp.condominio.api.dominios.saludambiental.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.ChecklistForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.EvaluacionForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.MantenimientoAmbientalForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.ChecklistResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.EvaluacionResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.MantenimientoAmbientalResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.services.ChecklistService;
import pe.edu.utp.condominio.api.dominios.saludambiental.services.EvaluacionChecklistService;
import pe.edu.utp.condominio.api.dominios.saludambiental.services.MantenimientoAmbientalService;

@RestController
@RequestMapping("/api/salud-ambiental")
public class SaludAmbientalRestController {

    private final ChecklistService checklistService;
    private final EvaluacionChecklistService evaluacionChecklistService;
    private final MantenimientoAmbientalService mantenimientoAmbientalService;

    public SaludAmbientalRestController(
            ChecklistService checklistService,
            EvaluacionChecklistService evaluacionChecklistService,
            MantenimientoAmbientalService mantenimientoAmbientalService) {
        this.checklistService = checklistService;
        this.evaluacionChecklistService = evaluacionChecklistService;
        this.mantenimientoAmbientalService = mantenimientoAmbientalService;
    }

    @PostMapping("/checklists")
    public ResponseEntity<ChecklistResponse> crearChecklist(@Valid @RequestBody ChecklistForm formulario) {
        ChecklistResponse respuesta = checklistService.crearChecklist(formulario);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping("/checklists/area/{areaId}")
    public ResponseEntity<List<ChecklistResponse>> listarChecklistsPorArea(@PathVariable Long areaId) {
        return ResponseEntity.ok(checklistService.listarChecklistsPorArea(areaId));
    }

    @PostMapping("/evaluaciones")
    public ResponseEntity<EvaluacionResponse> evaluarChecklist(@Valid @RequestBody EvaluacionForm formulario) {
        EvaluacionResponse respuesta = evaluacionChecklistService.evaluarChecklist(formulario);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/mantenimientos")
    public ResponseEntity<MantenimientoAmbientalResponse> registrarMantenimiento(
            @Valid @RequestBody MantenimientoAmbientalForm formulario) {
        MantenimientoAmbientalResponse respuesta = mantenimientoAmbientalService.registrarMantenimiento(formulario);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping("/mantenimientos/area/{areaId}")
    public ResponseEntity<List<MantenimientoAmbientalResponse>> obtenerHistorialMantenimiento(@PathVariable Long areaId) {
        return ResponseEntity.ok(mantenimientoAmbientalService.obtenerHistorialMantenimiento(areaId));
    }
}
