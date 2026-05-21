package pe.edu.utp.condominio.api.dominios.historial.services;

import java.util.List;
import pe.edu.utp.condominio.api.dominios.historial.dto.HistorialForm;
import pe.edu.utp.condominio.api.dominios.historial.models.HistorialTitularidad;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.historial.repositories.HistorialTitularidadRepository;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistorialTitularidadService {

    private final HistorialTitularidadRepository historialRepository;
    private final UnidadRepository unidadRepository;

    public HistorialTitularidadService(HistorialTitularidadRepository historialRepository,
            UnidadRepository unidadRepository) {
        this.historialRepository = historialRepository;
        this.unidadRepository = unidadRepository;
    }

    @Transactional
    public synchronized void registrarCambioTitularidad(HistorialForm form) {
        validarFormulario(form);

        String anterior = form.getPropietarioAnterior().trim();
        String nuevo = form.getNuevoPropietario().trim();

        if (anterior.equalsIgnoreCase(nuevo)) {
            throw new IllegalArgumentException("El nuevo propietario no puede ser la misma persona que el anterior.");
        }

        Unidad unidad = unidadRepository.findById(form.getDepartamentoId())
                .orElseThrow(() -> new IllegalArgumentException("La unidad indicada no existe."));

        HistorialTitularidad registro = new HistorialTitularidad();
        registro.setUnidad(unidad);
        registro.setPropietarioAnterior(anterior);
        registro.setNuevoPropietario(nuevo);
        historialRepository.save(registro);
    }

    public synchronized List<HistorialTitularidad> obtenerTodoElHistorial() {
        return historialRepository.listarTodoOrdenado();
    }

    private void validarFormulario(HistorialForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario no puede estar vacio.");
        }
        if (form.getDepartamentoId() == null || form.getDepartamentoId() <= 0) {
            throw new IllegalArgumentException("Debe ingresar un numero de departamento valido.");
        }
        if (form.getPropietarioAnterior() == null || form.getPropietarioAnterior().isBlank()) {
            throw new IllegalArgumentException("El nombre del propietario anterior es obligatorio.");
        }
        if (form.getNuevoPropietario() == null || form.getNuevoPropietario().isBlank()) {
            throw new IllegalArgumentException("El nombre del nuevo propietario es obligatorio.");
        }
    }
}
