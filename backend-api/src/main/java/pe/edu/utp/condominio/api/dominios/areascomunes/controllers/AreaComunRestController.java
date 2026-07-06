package pe.edu.utp.condominio.api.dominios.areascomunes.controllers;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.compartido.dto.RespuestaPaginada;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.AreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.response.AreaComunResponse;
import pe.edu.utp.condominio.api.dominios.areascomunes.services.AreaComunService;

@RestController
@RequestMapping("/api/areas-comunes")
public class AreaComunRestController {

    private final AreaComunService areaComunService;

    public AreaComunRestController(AreaComunService areaComunService) {
        this.areaComunService = areaComunService;
    }

    @GetMapping
    public ResponseEntity<RespuestaPaginada<AreaComunResponse>> listarAreasComunes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano,
            @RequestParam(required = false) Long condominioId) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("nombre").ascending());
        Page<AreaComunResponse> paginaAreas;

        if (condominioId != null) {
            paginaAreas = areaComunService.listarPorCondominioPaginado(condominioId, pageable);
        } else {
            paginaAreas = areaComunService.obtenerTodasLasAreasComunesPaginado(pageable);
        }

        return ResponseEntity.ok(new RespuestaPaginada<>(paginaAreas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerAreaComun(@PathVariable Long id) {
        try {
            AreaComunForm formulario = areaComunService.obtenerFormularioArea(id);
            return ResponseEntity.ok(formulario);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> registrarAreaComun(@Valid @RequestBody AreaComunForm peticion) {
        try {
            AreaComunResponse respuesta = areaComunService.registrarOActualizarArea(peticion);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al registrar area comun"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAreaComun(@PathVariable Long id, @Valid @RequestBody AreaComunForm peticion) {
        try {
            peticion.setId(id);
            AreaComunResponse respuesta = areaComunService.registrarOActualizarArea(peticion);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al actualizar area comun"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAreaComun(@PathVariable Long id) {
        try {
            areaComunService.eliminarArea(id);
            return ResponseEntity.ok(Map.of("mensaje", "Area comun eliminada correctamente."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al eliminar area comun."));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos.");
        return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
    }
}
