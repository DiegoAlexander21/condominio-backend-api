package pe.edu.utp.condominio.api.dominios.areascomunes.controllers;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import pe.edu.utp.condominio.api.compartido.dto.RespuestaPaginada;
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
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.request.ReservaAreaComunForm;
import pe.edu.utp.condominio.api.dominios.areascomunes.dto.response.ReservaAreaComunResponse;
import pe.edu.utp.condominio.api.dominios.areascomunes.services.GestionAreasComunesService;

@RestController
@RequestMapping("/api/reservas-areas")
@Validated
public class ReservaAreaComunRestController {

    private final GestionAreasComunesService gestionAreasComunesService;

    public ReservaAreaComunRestController(GestionAreasComunesService gestionAreasComunesService) {
        this.gestionAreasComunesService = gestionAreasComunesService;
    }

    @GetMapping
    public ResponseEntity<RespuestaPaginada<ReservaAreaComunResponse>> listarReservas(
            @RequestParam("areaComunId") Long areaComunId,
            @RequestParam(value = "fecha", required = false) LocalDate fecha,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano);
        Page<ReservaAreaComunResponse> paginaReservas = gestionAreasComunesService.listarReservasPaginado(areaComunId, fecha, pageable);
        return ResponseEntity.ok(new RespuestaPaginada<>(paginaReservas));
    }

    @PostMapping
    public ResponseEntity<ReservaAreaComunResponse> registrarReserva(
            @Valid @RequestBody ReservaAreaComunForm formulario) {
        ReservaAreaComunResponse reserva = gestionAreasComunesService.registrarReserva(formulario);
        return ResponseEntity.ok(reserva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable("id") Long id) {
        gestionAreasComunesService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }
}
