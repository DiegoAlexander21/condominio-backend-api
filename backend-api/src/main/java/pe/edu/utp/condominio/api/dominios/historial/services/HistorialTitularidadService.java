package pe.edu.utp.condominio.api.dominios.historial.services;

import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.utp.condominio.api.dominios.historial.models.HistorialTitularidad;
import pe.edu.utp.condominio.api.dominios.historial.repositories.HistorialTitularidadRepository;

@Service
public class HistorialTitularidadService {

    private final HistorialTitularidadRepository historialRepository;

    public HistorialTitularidadService(HistorialTitularidadRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    public synchronized List<HistorialTitularidad> obtenerTodoElHistorial() {
        return historialRepository.listarTodoOrdenado();
    }
}
