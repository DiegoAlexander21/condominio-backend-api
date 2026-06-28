package pe.edu.utp.condominio.api.dominios.incidencias.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pe.edu.utp.condominio.api.compartido.dto.RespuestaPaginada;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.ActualizacionIncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.EvidenciaIncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.request.IncidenciaForm;
import pe.edu.utp.condominio.api.dominios.incidencias.dto.response.IncidenciaResponse;
import pe.edu.utp.condominio.api.dominios.incidencias.enums.EstadoIncidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.services.GestionIncidenciasService;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaRestController {

    private final GestionIncidenciasService gestionIncidenciasService;

    public IncidenciaRestController(GestionIncidenciasService gestionIncidenciasService) {
        this.gestionIncidenciasService = gestionIncidenciasService;
    }

    @GetMapping
    public ResponseEntity<RespuestaPaginada<IncidenciaResponse>> listar(
            @RequestParam(value = "estado", required = false) EstadoIncidencia estado,
            @RequestParam(value = "unidadId", required = false) Long unidadId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("id").descending());

        Page<IncidenciaResponse> paginaRespuesta;
        if (unidadId != null) {
            paginaRespuesta = gestionIncidenciasService.listarPorUnidadYEstado(unidadId, estado, pageable);
        } else {
            if (estado == null) {
                paginaRespuesta = gestionIncidenciasService.listarTodas(pageable);
            } else {
                paginaRespuesta = gestionIncidenciasService.listarPorEstado(estado, pageable);
            }
        }

        return ResponseEntity.ok(new RespuestaPaginada<>(paginaRespuesta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerIncidencia(@PathVariable Long id) {
        IncidenciaResponse respuesta = gestionIncidenciasService.obtenerRespuestaPorId(id);
        if (respuesta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}/evidencias")
    public ResponseEntity<List<String>> obtenerEvidencias(@PathVariable Long id) {
        List<String> urls = gestionIncidenciasService.listarEvidencias(id).stream()
                .map(e -> e.getUrlArchivo())
                .collect(Collectors.toList());
        return ResponseEntity.ok(urls);
    }

    @PostMapping("/area")
    public ResponseEntity<?> registrarIncidenciaArea(@Valid @RequestBody IncidenciaForm peticion) {
        IncidenciaResponse respuesta = gestionIncidenciasService.registrarIncidencia(peticion);

        if (peticion.getEvidenciaUrl() != null && !peticion.getEvidenciaUrl().isBlank()) {
            String[] enlaces = peticion.getEvidenciaUrl().split(",");
            for (String enlace : enlaces) {
                if (!enlace.isBlank()) {
                    EvidenciaIncidenciaForm formularioEvidencia = new EvidenciaIncidenciaForm();
                    formularioEvidencia.setIncidenciaId(respuesta.getId());
                    formularioEvidencia.setUrlArchivo(enlace.trim());
                    gestionIncidenciasService.registrarEvidencia(formularioEvidencia);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/unidad")
    public ResponseEntity<?> registrarIncidenciaUnidad(@Valid @RequestBody IncidenciaForm peticion) {
        IncidenciaResponse respuesta = gestionIncidenciasService.registrarIncidencia(peticion);

        if (peticion.getEvidenciaUrl() != null && !peticion.getEvidenciaUrl().isBlank()) {
            String[] enlaces = peticion.getEvidenciaUrl().split(",");
            for (String enlace : enlaces) {
                if (!enlace.isBlank()) {
                    EvidenciaIncidenciaForm formularioEvidencia = new EvidenciaIncidenciaForm();
                    formularioEvidencia.setIncidenciaId(respuesta.getId());
                    formularioEvidencia.setUrlArchivo(enlace.trim());
                    gestionIncidenciasService.registrarEvidencia(formularioEvidencia);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id,
            @Valid @RequestBody ActualizacionIncidenciaForm peticion) {
        peticion.setIncidenciaId(id);
        gestionIncidenciasService.actualizarEstado(peticion);
        return ResponseEntity.ok(Map.of("mensaje", "Estado de la incidencia actualizado exitosamente"));
    }
}
