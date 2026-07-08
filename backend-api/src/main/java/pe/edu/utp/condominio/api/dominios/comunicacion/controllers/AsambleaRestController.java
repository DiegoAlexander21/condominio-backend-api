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
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.AsambleaForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.request.VotoAsambleaForm;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.AsambleaResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.dto.response.ResultadoAsambleaResponse;
import pe.edu.utp.condominio.api.dominios.comunicacion.services.AsambleaService;
import pe.edu.utp.condominio.api.dominios.comunicacion.services.VotacionAsambleaService;

@RestController
@RequestMapping("/api/asambleas")
public class AsambleaRestController {

    private final AsambleaService asambleaService;
    private final VotacionAsambleaService votacionAsambleaService;

    public AsambleaRestController(AsambleaService asambleaService, VotacionAsambleaService votacionAsambleaService) {
        this.asambleaService = asambleaService;
        this.votacionAsambleaService = votacionAsambleaService;
    }

    @GetMapping
    public ResponseEntity<List<AsambleaResponse>> listarAsambleas(@RequestParam("condominioId") Long condominioId) {
        return ResponseEntity.ok(asambleaService.listarPorCondominio(condominioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsambleaResponse> obtenerAsambleaPorId(@org.springframework.web.bind.annotation.PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(asambleaService.obtenerPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/terminar")
    public ResponseEntity<?> terminarAsamblea(@org.springframework.web.bind.annotation.PathVariable("id") Long id) {
        try {
            asambleaService.terminarAsamblea(id);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Asamblea terminada correctamente.");
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> registrarAsamblea(@Valid @RequestBody AsambleaForm formulario) {
        try {
            asambleaService.registrarAsamblea(formulario);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Asamblea programada correctamente.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/votos")
    public ResponseEntity<?> registrarVoto(@Valid @RequestBody VotoAsambleaForm formulario) {
        try {
            votacionAsambleaService.registrarVoto(formulario);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Voto registrado correctamente.");
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/resultados")
    public ResponseEntity<ResultadoAsambleaResponse> obtenerResultados(@RequestParam("asambleaId") Long asambleaId) {
        return ResponseEntity.ok(votacionAsambleaService.obtenerResultados(asambleaId));
    }

    @GetMapping("/{id}/voto")
    public ResponseEntity<Map<String, Boolean>> verificarVoto(@org.springframework.web.bind.annotation.PathVariable("id") Long id, @RequestParam("unidadId") Long unidadId) {
        boolean tieneVoto = votacionAsambleaService.tieneVotoRegistrado(id, unidadId);
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("votoRegistrado", tieneVoto);
        return ResponseEntity.ok(respuesta);
    }
}
