package pe.edu.utp.condominio.api.dominios.unidades.controllers;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.AsignarOcupantesForm;
import pe.edu.utp.condominio.api.dominios.unidades.dto.response.UnidadResponse;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.services.OcupanteService;

@RestController
@RequestMapping("/api/unidades/{id}/ocupantes")
public class OcupanteRestController {

    private final OcupanteService ocupanteService;

    public OcupanteRestController(OcupanteService ocupanteService) {
        this.ocupanteService = ocupanteService;
    }

    @GetMapping
    public ResponseEntity<?> obtenerOcupantes(@PathVariable Long id) {
        AsignarOcupantesForm formulario = ocupanteService.obtenerFormOcupantes(id);
        if (formulario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Unidad no encontrada."));
        }
        return ResponseEntity.ok(formulario);
    }

    @PutMapping
    public ResponseEntity<?> asignarOcupantes(@PathVariable Long id,
            @Valid @RequestBody AsignarOcupantesForm peticion) {
        try {
            peticion.setId(id);
            Unidad unidadActualizada = ocupanteService.asignarOcupantes(peticion);
            UnidadResponse respuesta = mapearAUnidadResponse(unidadActualizada);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error interno al asignar ocupantes"));
        }
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
}
