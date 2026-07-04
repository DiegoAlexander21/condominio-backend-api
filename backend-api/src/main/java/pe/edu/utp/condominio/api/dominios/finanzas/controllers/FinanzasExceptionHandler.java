package pe.edu.utp.condominio.api.dominios.finanzas.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "pe.edu.utp.condominio.api.dominios.finanzas.controllers")
public class FinanzasExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionesDeValidacion(MethodArgumentNotValidException ex) {
        String mensajeError = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos.");
        return ResponseEntity.badRequest().body(Map.of("error", mensajeError));
    }
}
