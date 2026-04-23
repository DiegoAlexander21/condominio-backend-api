package pe.edu.utp.condominio.api.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.condominio.api.dto.HistorialForm;
import pe.edu.utp.condominio.api.models.HistorialTitularidad;
import org.springframework.stereotype.Service;

@Service
public class HistorialTitularidadService {

    private final List<HistorialTitularidad> historiales = new ArrayList<>();
    private long secuenciaId = 1L;

    public synchronized void registrarCambioTitularidad(HistorialForm form) {
        validarFormulario(form);

        String anterior = form.getPropietarioAnterior().trim();
        String nuevo = form.getNuevoPropietario().trim();

        if (anterior.equalsIgnoreCase(nuevo)) {
            throw new IllegalArgumentException("El nuevo propietario no puede ser la misma persona que el anterior.");
        }

        HistorialTitularidad registro = new HistorialTitularidad();
        registro.setId(secuenciaId++);
        registro.setDepartamentoId(form.getDepartamentoId());
        registro.setPropietarioAnterior(anterior);
        registro.setNuevoPropietario(nuevo);
        registro.setFechaCambio(LocalDateTime.now());

        historiales.add(registro);
    }

    public synchronized List<HistorialTitularidad> obtenerTodoElHistorial() {
        return new ArrayList<>(historiales);
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