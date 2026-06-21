package pe.edu.utp.condominio.api.dominios.areascomunes.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.AreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.response.AreaComunResponse;
import pe.edu.utp.condominio.api.dominios.areascomunes.services.GestionAreasComunesService;

@RestController
@RequestMapping("/api/areas-comunes")
@Validated
public class AreaComunRestController {

    private final GestionAreasComunesService gestionAreasComunesService;

    public AreaComunRestController(GestionAreasComunesService gestionAreasComunesService) {
        this.gestionAreasComunesService = gestionAreasComunesService;
    }

    @GetMapping
    public ResponseEntity<RespuestaPaginada<AreaComunResponse>> listarAreasComunes(
            @RequestParam(value = "condominioId", required = false) Long condominioId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano);
        if (condominioId != null) {
            return ResponseEntity.ok(new RespuestaPaginada<>(
                    gestionAreasComunesService.listarPorCondominioPaginado(condominioId, pageable)));
        }
        return ResponseEntity
                .ok(new RespuestaPaginada<>(gestionAreasComunesService.obtenerTodasLasAreasComunesPaginado(pageable)));
    }

    @PostMapping
    public ResponseEntity<AreaComunResponse> registrarAreaComun(@Valid @RequestBody AreaComunForm formulario) {
        AreaComunResponse areaRegistrada = gestionAreasComunesService.registrarOActualizarArea(formulario);
        return ResponseEntity.ok(areaRegistrada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaComunForm> obtenerArea(@PathVariable("id") Long id) {
        return ResponseEntity.ok(gestionAreasComunesService.obtenerFormularioArea(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAreaComun(@PathVariable("id") Long id) {
        gestionAreasComunesService.eliminarArea(id);
        return ResponseEntity.noContent().build();
    }
}
