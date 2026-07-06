package pe.edu.utp.condominio.api.dominios.areascomunes.controllers;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.compartido.dto.RespuestaPaginada;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.ReservaAreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.response.ReservaAreaComunResponse;
import pe.edu.utp.condominio.api.dominios.areascomunes.services.ReservaAreaComunService;

@RestController
@RequestMapping("/api/areas-comunes/reservas")
public class ReservaAreaComunRestController {

    private final ReservaAreaComunService reservaAreaComunService;

    public ReservaAreaComunRestController(ReservaAreaComunService reservaAreaComunService) {
        this.reservaAreaComunService = reservaAreaComunService;
    }

    @GetMapping
    public ResponseEntity<RespuestaPaginada<ReservaAreaComunResponse>> listarReservas(
            @RequestParam(required = true) Long areaComunId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Long unidadId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("fechaReserva").descending().and(Sort.by("horaInicio").descending()));
        Page<ReservaAreaComunResponse> paginaReservas = reservaAreaComunService.listarReservasPaginado(areaComunId, fecha, unidadId, pageable);
        
        return ResponseEntity.ok(new RespuestaPaginada<>(paginaReservas));
    }

    @PostMapping
    public ResponseEntity<?> registrarReserva(@Valid @RequestBody ReservaAreaComunForm peticion) {
        try {
            ReservaAreaComunResponse respuesta = reservaAreaComunService.registrarReserva(peticion);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al registrar la reserva"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
        try {
            reservaAreaComunService.cancelarReserva(id);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada correctamente."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al cancelar la reserva."));
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
