package pe.edu.utp.condominio.api.dominios.unidades.controllers;

import java.util.Map;
import java.util.List;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.compartido.dto.RespuestaPaginada;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.UnidadForm;
import pe.edu.utp.condominio.api.dominios.unidades.dto.response.UnidadResponse;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.services.UnidadService;

@RestController
@RequestMapping("/api/unidades")
public class UnidadRestController {

    private final UnidadService unidadService;

    public UnidadRestController(UnidadService unidadService) {
        this.unidadService = unidadService;
    }

    @GetMapping
    public ResponseEntity<RespuestaPaginada<UnidadResponse>> listarUnidades(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("id").descending());
        Page<Unidad> paginaUnidades = unidadService.obtenerUnidadesPaginadas(pageable);

        Page<UnidadResponse> paginaRespuesta = paginaUnidades.map(this::mapearAUnidadResponse);
        return ResponseEntity.ok(new RespuestaPaginada<>(paginaRespuesta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUnidad(@PathVariable Long id) {
        UnidadForm formulario = unidadService.obtenerFormUnidad(id);
        if (formulario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Unidad no encontrada."));
        }
        return ResponseEntity.ok(formulario);
    }

    @PostMapping
    public ResponseEntity<?> registrarUnidad(@Valid @RequestBody UnidadForm peticion) {
        try {
            Unidad unidadGuardada = unidadService.registrarOActualizarUnidad(peticion);
            UnidadResponse respuesta = mapearAUnidadResponse(unidadGuardada);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al registrar la unidad"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUnidad(@PathVariable Long id, @Valid @RequestBody UnidadForm peticion) {
        try {
            peticion.setId(id);
            Unidad unidadGuardada = unidadService.registrarOActualizarUnidad(peticion);
            UnidadResponse respuesta = mapearAUnidadResponse(unidadGuardada);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al actualizar la unidad"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUnidad(@PathVariable Long id) {
        try {
            unidadService.eliminarUnidad(id);
            return ResponseEntity.ok(Map.of("mensaje", "Unidad eliminada correctamente."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al eliminar la unidad."));
        }
    }

    @PostMapping("/busqueda/torres")
    public ResponseEntity<List<pe.edu.utp.condominio.api.dominios.unidades.dto.response.TorreDto>> buscarTorres(@RequestBody List<Long> condominioIds) {
        return ResponseEntity.ok(unidadService.buscarTorresPorCondominios(condominioIds));
    }

    @PostMapping("/busqueda/viviendas")
    public ResponseEntity<List<UnidadResponse>> buscarViviendas(@RequestBody List<pe.edu.utp.condominio.api.dominios.unidades.dto.response.TorreDto> torresDto) {
        if (torresDto == null || torresDto.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<Long> condominioIds = torresDto.stream().map(pe.edu.utp.condominio.api.dominios.unidades.dto.response.TorreDto::getCondominioId).collect(java.util.stream.Collectors.toList());
        List<String> torres = torresDto.stream().map(pe.edu.utp.condominio.api.dominios.unidades.dto.response.TorreDto::getTorre).collect(java.util.stream.Collectors.toList());
        
        List<UnidadResponse> respuestas = unidadService.buscarUnidadesPorTorres(condominioIds, torres).stream()
            .map(this::mapearAUnidadResponse)
            .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(respuestas);
    }

    private UnidadResponse mapearAUnidadResponse(Unidad unidad) {
        UnidadResponse response = new UnidadResponse();
        response.setId(unidad.getId());
        response.setCondominioId(unidad.getCondominio() != null ? unidad.getCondominio().getId() : null);
        response.setNombreCondominio(unidad.getCondominio() != null ? unidad.getCondominio().getNombre() : null);
        response.setNumeroUnidad(unidad.getNumeroUnidad());
        response.setTorre(unidad.getTorre());
        response.setPiso(unidad.getPiso());
        response.setArea(unidad.getArea());
        String estado = "Sin asignar";
        if (unidad.getResidente() != null && unidad.getResidente().isActivo()) {
            estado = "Ocupado (" + unidad.getResidente().getNombre() + ")";
        } else if (unidad.getPropietario() != null) {
            estado = "Propietario asignado";
        }
        response.setEstado(estado);

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionesDeValidacion(MethodArgumentNotValidException ex) {
        String mensajeError = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos.");
        return ResponseEntity.badRequest().body(Map.of("error", mensajeError));
    }
}
