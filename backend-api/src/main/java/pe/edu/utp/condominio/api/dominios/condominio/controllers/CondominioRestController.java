package pe.edu.utp.condominio.api.dominios.condominio.controllers;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.compartido.dto.RespuestaPaginada;
import pe.edu.utp.condominio.api.dominios.condominio.dto.request.CondominioForm;
import pe.edu.utp.condominio.api.dominios.condominio.dto.response.CondominioResponse;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.services.GestionCondominioService;

@RestController
@RequestMapping("/api/condominios")
public class CondominioRestController {

    private final GestionCondominioService gestionCondominioService;

    public CondominioRestController(GestionCondominioService gestionCondominioService) {
        this.gestionCondominioService = gestionCondominioService;
    }

    @GetMapping
    public ResponseEntity<RespuestaPaginada<CondominioResponse>> listarCondominios(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("id").descending());
        Page<Condominio> paginaCondominios = gestionCondominioService.obtenerCondominiosPaginados(pageable);

        Page<CondominioResponse> paginaRespuesta = paginaCondominios.map(this::mapearAcondominioResponse);
        return ResponseEntity.ok(new RespuestaPaginada<>(paginaRespuesta));
    }

    @PostMapping
    public ResponseEntity<?> registrarCondominio(@Valid @RequestBody CondominioForm peticion) {
        try {
            Condominio condominioGuardado = gestionCondominioService.registrarOActualizarCondominio(peticion);
            CondominioResponse respuesta = mapearAcondominioResponse(condominioGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al registrar el condominio"));
        }
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<?> actualizarCondominio(@PathVariable Long id, @Valid @RequestBody CondominioForm peticion) {
        try {
            peticion.setId(id);
            Condominio condominioActualizado = gestionCondominioService.registrarOActualizarCondominio(peticion);
            CondominioResponse respuesta = mapearAcondominioResponse(condominioActualizado);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al actualizar el condominio"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCondominio(@PathVariable Long id) {
        CondominioForm formulario = gestionCondominioService.obtenerFormCondominio(id);
        if (formulario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Condominio no encontrado."));
        }
        return ResponseEntity.ok(formulario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCondominio(@PathVariable Long id) {
        try {
            gestionCondominioService.eliminarCondominio(id);
            return ResponseEntity.ok(Map.of("mensaje", "Condominio eliminado correctamente."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al eliminar el condominio."));
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        return ResponseEntity.ok(Map.of(
                "totalTorres", gestionCondominioService.obtenerTotalTorres(),
                "totalPisos", gestionCondominioService.obtenerTotalPisos()));
    }

    private CondominioResponse mapearAcondominioResponse(Condominio condominio) {
        CondominioResponse response = new CondominioResponse();
        response.setId(condominio.getId());
        response.setNombre(condominio.getNombre());
        response.setTorres(condominio.getTorres());
        response.setPisosPorTorre(condominio.getPisosPorTorre());
        response.setFechaRegistro(condominio.getFechaRegistro());
        return response;
    }
}
