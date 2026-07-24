package pe.edu.utp.condominio.api.dominios.finanzas.controllers;

import java.util.List;
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
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.DistribucionGastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.GastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.DetalleGastoUnidadResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.GastoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;
import pe.edu.utp.condominio.api.compartido.dto.RespuestaPaginada;
import pe.edu.utp.condominio.api.dominios.finanzas.services.DistribucionGastoService;
import pe.edu.utp.condominio.api.dominios.finanzas.services.GastoService;

@RestController
@RequestMapping("/api/finanzas/gastos")
public class GastoRestController {

    private final GastoService gastoService;
    private final DistribucionGastoService distribucionGastoService;

    public GastoRestController(GastoService gastoService, DistribucionGastoService distribucionGastoService) {
        this.gastoService = gastoService;
        this.distribucionGastoService = distribucionGastoService;
    }

    @GetMapping
    public ResponseEntity<RespuestaPaginada<GastoResponse>> listarGastos(
            @RequestParam(value = "tipo", required = false) TipoGasto tipo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {
        Pageable paginacion = PageRequest.of(pagina, tamano, Sort.by(Sort.Direction.DESC, "id"));
        Page<GastoResponse> paginaGastos = gastoService.listarGastosPorTipo(tipo, paginacion);
        return ResponseEntity.ok(new RespuestaPaginada<>(paginaGastos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoForm> obtenerGasto(@PathVariable Long id) {
        return ResponseEntity.ok(gastoService.obtenerGastoParaEdicion(id));
    }

    @PostMapping
    public ResponseEntity<?> registrarGasto(@Valid @RequestBody GastoForm formulario) {
        try {
            GastoResponse respuesta = gastoService.registrarGasto(formulario);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno al registrar el gasto"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarGasto(@PathVariable Long id, @Valid @RequestBody GastoForm formulario) {
        try {
            GastoResponse respuesta = gastoService.actualizarGasto(id, formulario);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno al actualizar el gasto"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarGasto(@PathVariable Long id) {
        try {
            gastoService.eliminarGasto(id);
            return ResponseEntity.ok(Map.of("mensaje", "Gasto eliminado correctamente."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al eliminar el gasto."));
        }
    }

    @PostMapping("/distribuir")
    public ResponseEntity<?> distribuirGasto(@Valid @RequestBody DistribucionGastoForm formulario) {
        try {
            List<DetalleGastoUnidadResponse> respuesta = distribucionGastoService.distribuirGasto(formulario);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno al distribuir el gasto."));
        }
    }

    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetalleGastoUnidadResponse>> listarDetallesGasto(@PathVariable Long id) {
        return ResponseEntity.ok(gastoService.listarDetallesPorGasto(id));
    }
}
