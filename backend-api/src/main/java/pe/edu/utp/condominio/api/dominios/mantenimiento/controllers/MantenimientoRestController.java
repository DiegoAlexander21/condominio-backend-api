package pe.edu.utp.condominio.api.dominios.mantenimiento.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request.InsumoForm;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request.TareaMantenimientoForm;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.response.InsumoResponse;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.response.TareaMantenimientoResponse;
import pe.edu.utp.condominio.api.dominios.mantenimiento.services.InsumoService;
import pe.edu.utp.condominio.api.dominios.mantenimiento.services.TareaMantenimientoService;

@RestController
@RequestMapping("/api/mantenimiento")
public class MantenimientoRestController {

    private final InsumoService insumoService;
    private final TareaMantenimientoService tareaMantenimientoService;

    public MantenimientoRestController(InsumoService insumoService,
            TareaMantenimientoService tareaMantenimientoService) {
        this.insumoService = insumoService;
        this.tareaMantenimientoService = tareaMantenimientoService;
    }

    @GetMapping("/insumos")
    public ResponseEntity<List<InsumoResponse>> listarInsumos() {
        return ResponseEntity.ok(insumoService.listarInsumos());
    }

    @GetMapping("/insumos/criticos")
    public ResponseEntity<List<InsumoResponse>> listarInsumosCriticos() {
        return ResponseEntity.ok(insumoService.listarInsumosCriticos());
    }

    @PostMapping("/insumos")
    public ResponseEntity<InsumoResponse> registrarInsumo(@Valid @RequestBody InsumoForm formulario) {
        InsumoResponse respuesta = insumoService.registrarInsumo(formulario);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/tareas")
    public ResponseEntity<TareaMantenimientoResponse> registrarTarea(
            @Valid @RequestBody TareaMantenimientoForm formulario) {
        TareaMantenimientoResponse respuesta = tareaMantenimientoService.registrarTareaConInsumos(formulario);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}
