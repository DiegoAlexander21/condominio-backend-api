package pe.edu.utp.condominio.api.dominios.comunicacion.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.ComunicadoForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.ComunicadoIAForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.ComunicadoResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.services.GestionComunicadosService;

@RestController
@RequestMapping("/api/comunicados")
public class ComunicadoRestController {

    private final GestionComunicadosService gestionComunicadosService;

    public ComunicadoRestController(GestionComunicadosService gestionComunicadosService) {
        this.gestionComunicadosService = gestionComunicadosService;
    }

    @GetMapping
    public ResponseEntity<List<ComunicadoResponse>> listarComunicados(@RequestParam("condominioId") Long condominioId) {
        return ResponseEntity.ok(gestionComunicadosService.listarPorCondominio(condominioId));
    }

    @PostMapping
    public ResponseEntity<?> registrarComunicado(@Valid @RequestBody ComunicadoForm formulario) {
        try {
            gestionComunicadosService.registrarComunicado(formulario);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Comunicado registrado correctamente.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/ia")
    public ResponseEntity<?> generarConIA(@Valid @RequestBody ComunicadoIAForm formulario) {
        try {
            ComunicadoResponse comunicadoGenerado = gestionComunicadosService.generarConIA(formulario);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("comunicadoGenerado", comunicadoGenerado);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
