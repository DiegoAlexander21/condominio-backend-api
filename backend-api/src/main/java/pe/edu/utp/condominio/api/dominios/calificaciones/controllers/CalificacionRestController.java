package pe.edu.utp.condominio.api.dominios.calificaciones.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.request.CalificacionForm;
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.response.CalificacionResponse;
import pe.edu.utp.condominio.api.dominios.calificaciones.dto.response.EstadoAreaResponse;
import pe.edu.utp.condominio.api.dominios.calificaciones.services.CalificacionAreaService;
import pe.edu.utp.condominio.api.dominios.calificaciones.services.EstadoAreaService;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionRestController {

    private final CalificacionAreaService calificacionAreaService;
    private final EstadoAreaService estadoAreaService;

    public CalificacionRestController(CalificacionAreaService calificacionAreaService, EstadoAreaService estadoAreaService) {
        this.calificacionAreaService = calificacionAreaService;
        this.estadoAreaService = estadoAreaService;
    }

    @GetMapping("/area/{areaId}")
    public ResponseEntity<Map<String, Object>> mostrarCalificacionesArea(@PathVariable Long areaId) {
        List<CalificacionResponse> calificaciones = calificacionAreaService.listarCalificacionesPorArea(areaId);
        EstadoAreaResponse estadoActual = estadoAreaService.obtenerEstadoActual(areaId);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("calificaciones", calificaciones);
        respuesta.put("estadoActual", estadoActual);
        
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCalificacion(@Valid @RequestBody CalificacionForm formulario) {
        try {
            calificacionAreaService.registrarCalificacion(formulario);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "¡Gracias por tu calificación!");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/area/{areaId}/actualizar-estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long areaId) {
        try {
            estadoAreaService.actualizarEstadoAutomatico(areaId);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Estado del área actualizado manualmente.");
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<EstadoAreaResponse>> mostrarRankingAreas() {
        return ResponseEntity.ok(estadoAreaService.obtenerRankingAreas());
    }
}
