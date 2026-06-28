package pe.edu.utp.condominio.api.dominios.finanzas.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.AprobacionPagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.EvidenciaPagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.PagoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.PagoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.EstadoPago;
import pe.edu.utp.condominio.api.dominios.finanzas.services.EvidenciaPagoService;
import pe.edu.utp.condominio.api.dominios.finanzas.services.PagoService;

@RestController
@RequestMapping("/api/finanzas/pagos")
public class PagoRestController {

    private final PagoService pagoService;
    private final EvidenciaPagoService evidenciaPagoService;

    public PagoRestController(PagoService pagoService, EvidenciaPagoService evidenciaPagoService) {
        this.pagoService = pagoService;
        this.evidenciaPagoService = evidenciaPagoService;
    }

    @GetMapping("/unidad/{unidadId}")
    public ResponseEntity<List<PagoResponse>> listarPagosPorUnidad(@PathVariable Long unidadId) {
        return ResponseEntity.ok(pagoService.listarPagosPorUnidad(unidadId));
    }

    @PostMapping
    public ResponseEntity<?> registrarPago(@Valid @RequestBody PagoForm formulario) {
        try {
            PagoResponse respuesta = pagoService.registrarPago(formulario);

            if (formulario.getEvidenciaUrl() != null && !formulario.getEvidenciaUrl().isBlank()) {
                String[] enlaces = formulario.getEvidenciaUrl().split(",");
                for (String enlace : enlaces) {
                    if (!enlace.isBlank()) {
                        EvidenciaPagoForm formularioEvidencia = new EvidenciaPagoForm();
                        formularioEvidencia.setPagoId(respuesta.getId());
                        formularioEvidencia.setUrlArchivo(enlace.trim());
                        evidenciaPagoService.registrarEvidenciaPago(formularioEvidencia);
                    }
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno al registrar el pago"));
        }
    }

    @GetMapping
    public ResponseEntity<Page<PagoResponse>> listarPagos(
            @RequestParam(value = "estado", required = false) EstadoPago estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaPago"));
        return ResponseEntity.ok(pagoService.listarPagos(estado, pageable));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<PagoResponse>> listarPagosPendientes() {
        return ResponseEntity.ok(pagoService.listarPagosPendientes());
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarPago(@PathVariable Long id, @Valid @RequestBody AprobacionPagoForm formulario) {
        try {
            PagoResponse respuesta = pagoService.aprobarPago(id, formulario.getAprobar(),
                    formulario.getObservacionAdmin());
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al aprobar/rechazar el pago"));
        }
    }
}
