package pe.edu.utp.condominio.api.dominios.historial.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pe.edu.utp.condominio.api.dominios.historial.models.HistorialTitularidad;
import pe.edu.utp.condominio.api.dominios.historial.repositories.HistorialTitularidadRepository;

@Service
public class HistorialTitularidadService {

    private final HistorialTitularidadRepository historialRepository;

    public HistorialTitularidadService(HistorialTitularidadRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    public Page<HistorialTitularidad> obtenerHistorialPaginado(String termino, Pageable paginacion) {
        return historialRepository.buscarHistorialPaginado(termino, paginacion);
    }
}
