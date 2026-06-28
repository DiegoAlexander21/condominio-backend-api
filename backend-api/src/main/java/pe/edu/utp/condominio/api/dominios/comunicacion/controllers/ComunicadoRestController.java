package pe.edu.utp.condominio.api.dominios.comunicacion.controllers;

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
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.ComunicadoForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.ComunicadoIAForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.ComunicadoResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.services.ComunicadoService;

@RestController
@RequestMapping("/api/comunicados")
public class ComunicadoRestController {

    private final ComunicadoService comunicadoService;

    public ComunicadoRestController(ComunicadoService comunicadoService) {
        this.comunicadoService = comunicadoService;
    }

    @GetMapping("/condominio/{condominioId}")
    public ResponseEntity<List<ComunicadoResponse>> listarPorCondominio(@PathVariable Long condominioId) {
        return ResponseEntity.ok(comunicadoService.listarPorCondominio(condominioId));
    }

    @PostMapping
    public ResponseEntity<?> registrarComunicado(@Valid @RequestBody ComunicadoForm formulario) {
        try {
            ComunicadoResponse respuesta = comunicadoService.registrarComunicado(formulario);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al registrar el comunicado"));
        }
    }

    @PostMapping("/generar-ia")
    public ResponseEntity<?> generarConIA(@Valid @RequestBody ComunicadoIAForm formulario) {
        try {
            ComunicadoResponse respuesta = comunicadoService.generarConIA(formulario);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al generar el comunicado con IA"));
        }
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
