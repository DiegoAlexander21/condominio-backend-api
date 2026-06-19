package pe.edu.utp.condominio.api.dominios.unidades.controllers;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.compartido.dto.RespuestaPaginada;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.AsignarOcupantesForm;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.UnidadForm;
import pe.edu.utp.condominio.api.dominios.unidades.dto.response.UnidadResponse;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.services.GestionUnidadesService;

@RestController
@RequestMapping("/api/unidades")
public class UnidadRestController {

    private final GestionUnidadesService gestionUnidadesService;

    public UnidadRestController(GestionUnidadesService gestionUnidadesService) {
        this.gestionUnidadesService = gestionUnidadesService;
    }

    @GetMapping
    public ResponseEntity<RespuestaPaginada<UnidadResponse>> listarUnidades(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("id").descending());
        Page<Unidad> paginaUnidades = gestionUnidadesService.obtenerUnidadesPaginadas(pageable);

        Page<UnidadResponse> paginaRespuesta = paginaUnidades.map(this::mapearAUnidadResponse);
        return ResponseEntity.ok(new RespuestaPaginada<>(paginaRespuesta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUnidad(@PathVariable Long id) {
        UnidadForm formulario = gestionUnidadesService.obtenerFormUnidad(id);
        if (formulario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Unidad no encontrada."));
        }
        return ResponseEntity.ok(formulario);
    }

    @PostMapping
    public ResponseEntity<?> registrarUnidad(@Valid @RequestBody UnidadForm peticion) {
        try {
            Unidad unidadGuardada = gestionUnidadesService.registrarOActualizarUnidad(peticion);
            UnidadResponse respuesta = mapearAUnidadResponse(unidadGuardada);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al registrar la unidad"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUnidad(@PathVariable Long id, @Valid @RequestBody UnidadForm peticion) {
        try {
            peticion.setId(id);
            Unidad unidadGuardada = gestionUnidadesService.registrarOActualizarUnidad(peticion);
            UnidadResponse respuesta = mapearAUnidadResponse(unidadGuardada);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al actualizar la unidad"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUnidad(@PathVariable Long id) {
        try {
            gestionUnidadesService.eliminarUnidad(id);
            return ResponseEntity.ok(Map.of("mensaje", "Unidad eliminada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al eliminar la unidad."));
        }
    }

    @GetMapping("/{id}/ocupantes")
    public ResponseEntity<?> obtenerOcupantes(@PathVariable Long id) {
        AsignarOcupantesForm formulario = gestionUnidadesService.obtenerFormOcupantes(id);
        if (formulario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Unidad no encontrada."));
        }
        return ResponseEntity.ok(formulario);
    }

    @PutMapping("/{id}/ocupantes")
    public ResponseEntity<?> asignarOcupantes(@PathVariable Long id,
            @Valid @RequestBody AsignarOcupantesForm peticion) {
        try {

            peticion.setId(id);
            Unidad unidadActualizada = gestionUnidadesService.asignarOcupantes(peticion);
            UnidadResponse respuesta = mapearAUnidadResponse(unidadActualizada);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al asignar ocupantes"));
        }
    }

    private UnidadResponse mapearAUnidadResponse(Unidad unidad) {
        UnidadResponse response = new UnidadResponse();
        response.setId(unidad.getId());
        response.setNombreCondominio(unidad.getCondominio() != null ? unidad.getCondominio().getNombre() : null);
        response.setNumeroUnidad(unidad.getNumeroUnidad());
        response.setTorre(unidad.getTorre());
        response.setPiso(unidad.getPiso());
        response.setArea(unidad.getArea());
        String estado = "Sin asignar";
        if (unidad.getResidente() != null && unidad.getResidente().isActivo()) {
            estado = "Ocupado (" + unidad.getResidente().getNombre() + ")";
        } else if (unidad.getPropietario() != null) {
            estado = "Propietario asignado";
        }
        response.setEstado(estado);

        return response;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos.");
        return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
    }
}
