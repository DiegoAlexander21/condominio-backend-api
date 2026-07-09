package pe.edu.utp.condominio.api.dominios.historial.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.utp.condominio.api.compartido.dto.RespuestaPaginada;
import pe.edu.utp.condominio.api.dominios.historial.dto.response.HistorialTitularidadResponse;
import pe.edu.utp.condominio.api.dominios.historial.models.HistorialTitularidad;
import pe.edu.utp.condominio.api.dominios.historial.services.HistorialTitularidadService;

@RestController
@RequestMapping("/api/historial")
public class HistorialTitularidadRestController {

    private final HistorialTitularidadService historialService;

    public HistorialTitularidadRestController(HistorialTitularidadService historialService) {
        this.historialService = historialService;
    }

    @GetMapping
    public ResponseEntity<RespuestaPaginada<HistorialTitularidadResponse>> obtenerHistorial(
            @RequestParam(required = false) String termino,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        Pageable paginacion = PageRequest.of(pagina, tamano, Sort.by("id").descending());
        String terminoBusqueda = (termino == null) ? "" : termino;
        Page<HistorialTitularidad> paginaHistorial = historialService.obtenerHistorialPaginado(terminoBusqueda, paginacion);

        Page<HistorialTitularidadResponse> paginaRespuesta = paginaHistorial.map(this::mapearAHistorialResponse);
        return ResponseEntity.ok(new RespuestaPaginada<>(paginaRespuesta));
    }

    private HistorialTitularidadResponse mapearAHistorialResponse(HistorialTitularidad historial) {
        HistorialTitularidadResponse response = new HistorialTitularidadResponse();
        response.setId(historial.getId());
        if (historial.getUnidad() != null) {
            response.setUnidadId(historial.getUnidad().getId());
            response.setNumeroUnidad(historial.getUnidad().getNumeroUnidad());
        }
        response.setPropietarioAnterior(historial.getPropietarioAnterior());
        response.setNuevoPropietario(historial.getNuevoPropietario());
        response.setFechaCambio(historial.getFechaCambio());
        return response;
    }
}
