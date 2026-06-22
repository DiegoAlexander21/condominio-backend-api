package pe.edu.utp.condominio.api.compartido.excepciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("GlobalExceptionHandler capturó IllegalArgumentException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage(), "error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex) {
        log.error("GlobalExceptionHandler capturó un error: ", ex);
        return ResponseEntity.internalServerError().body(Map.of("message", "Error interno: " + ex.getMessage(), "error", "Error interno: " + ex.getMessage()));
    }
}
