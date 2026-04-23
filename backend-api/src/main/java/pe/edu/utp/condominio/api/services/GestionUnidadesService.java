package pe.edu.utp.condominio.api.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import pe.edu.utp.condominio.api.dto.UnidadForm;
import pe.edu.utp.condominio.api.models.Unidad;
import org.springframework.stereotype.Service;

@Service
public class GestionUnidadesService {

    private final List<Unidad> unidades = new ArrayList<>();
    private long secuenciaUnidad = 1L;

    public synchronized Unidad registrarOActualizarUnidad(UnidadForm form) {
        validarUnidad(form);

        if (form.getId() != null) {
            for (Unidad unidad : unidades) {
                if (unidad.getId().equals(form.getId())) {
                    actualizarUnidadExistente(unidad, form);
                    return unidad;
                }
            }
        }

        String claveUnidad = generarClaveUnidad(form.getNumeroUnidad(), form.getTorre(), form.getPiso());

        for (Unidad unidad : unidades) {
            String claveExistente = generarClaveUnidad(unidad.getNumeroUnidad(), unidad.getTorre(), unidad.getPiso());
            if (claveExistente.equals(claveUnidad)) {
                actualizarUnidadExistente(unidad, form);
                return unidad;
            }
        }

        return crearNuevaUnidad(form);
    }

    public synchronized List<Unidad> obtenerUnidades() {
        List<Unidad> copia = new ArrayList<>(unidades);
        copia.sort(Comparator.comparing(Unidad::getNumeroUnidad, String.CASE_INSENSITIVE_ORDER));
        return List.copyOf(copia);
    }

    public synchronized int contarPropietarios() {
        int contador = 0;
        for (Unidad unidad : unidades) {
            if (unidad.getNombrePropietario() != null && !unidad.getNombrePropietario().isBlank()) {
                contador++;
            }
        }
        return contador;
    }

    public synchronized int contarResidentesActivos() {
        int contador = 0;
        for (Unidad unidad : unidades) {
            if (unidad.isResidenteActivo() && unidad.getNombreResidente() != null) {
                contador++;
            }
        }
        return contador;
    }

    public synchronized Unidad buscarPorId(Long id) {
        for (Unidad unidad : unidades) {
            if (unidad.getId().equals(id)) {
                return unidad;
            }
        }
        return null;
    }

    public UnidadForm convertirAForm(Unidad unidad) {
        UnidadForm form = new UnidadForm();
        form.setId(unidad.getId());
        form.setNombreCondominio(unidad.getNombreCondominio());
        form.setNumeroUnidad(unidad.getNumeroUnidad());
        form.setTorre(unidad.getTorre());
        form.setPiso(unidad.getPiso());
        form.setArea(unidad.getArea());
        form.setNombrePropietario(unidad.getNombrePropietario());
        form.setDniPropietario(unidad.getDniPropietario());
        form.setEmailPropietario(unidad.getEmailPropietario());
        form.setTelefonoPropietario(unidad.getTelefonoPropietario());
        form.setNombreResidente(unidad.getNombreResidente());
        form.setDniResidente(unidad.getDniResidente());
        form.setParentesco(unidad.getParentesco());
        form.setResidenteActivo(unidad.isResidenteActivo());
        return form;
    }

    private String generarClaveUnidad(String numero, String torre, int piso) {
        return torre.toUpperCase() + "-" + piso + "-" + numero.toUpperCase();
    }

    private void actualizarUnidadExistente(Unidad unidad, UnidadForm form) {
        unidad.setNumeroUnidad(form.getNumeroUnidad());
        unidad.setTorre(form.getTorre());
        unidad.setPiso(form.getPiso());
        unidad.setArea(form.getArea());
        unidad.setNombreCondominio(form.getNombreCondominio());
        unidad.setNombrePropietario(form.getNombrePropietario());
        unidad.setDniPropietario(form.getDniPropietario());
        unidad.setEmailPropietario(form.getEmailPropietario());
        unidad.setTelefonoPropietario(form.getTelefonoPropietario());
        unidad.setNombreResidente(form.getNombreResidente());
        unidad.setDniResidente(form.getDniResidente());
        unidad.setParentesco(form.getParentesco());
        unidad.setResidenteActivo(form.isResidenteActivo());
        unidad.setFechaActualizacion(LocalDateTime.now());
    }

    private Unidad crearNuevaUnidad(UnidadForm form) {
        Unidad nueva = new Unidad();
        nueva.setId(secuenciaUnidad++);
        nueva.setNombreCondominio(form.getNombreCondominio());
        nueva.setNumeroUnidad(form.getNumeroUnidad());
        nueva.setTorre(form.getTorre());
        nueva.setPiso(form.getPiso());
        nueva.setArea(form.getArea());
        nueva.setNombrePropietario(form.getNombrePropietario());
        nueva.setDniPropietario(form.getDniPropietario());
        nueva.setEmailPropietario(form.getEmailPropietario());
        nueva.setTelefonoPropietario(form.getTelefonoPropietario());
        nueva.setNombreResidente(form.getNombreResidente());
        nueva.setDniResidente(form.getDniResidente());
        nueva.setParentesco(form.getParentesco());
        nueva.setResidenteActivo(form.isResidenteActivo());
        nueva.setFechaRegistro(LocalDateTime.now());
        nueva.setFechaActualizacion(LocalDateTime.now());
        unidades.add(nueva);
        return nueva;
    }

    private void validarUnidad(UnidadForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario de la unidad es obligatorio.");
        }

        if (form.getNombreCondominio() == null || form.getNombreCondominio().isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar un condominio.");
        }

        if (form.getNumeroUnidad() == null || form.getNumeroUnidad().isBlank()) {
            throw new IllegalArgumentException("El numero de unidad no puede estar vacio.");
        }

        if (form.getTorre() == null || form.getTorre().isBlank()) {
            throw new IllegalArgumentException("La torre no puede estar vacia.");
        }

        if (form.getPiso() <= 0) {
            throw new IllegalArgumentException("El piso debe ser mayor a cero.");
        }

        if (form.getArea() <= 0) {
            throw new IllegalArgumentException("El area debe ser mayor a cero.");
        }

        if (form.getNombrePropietario() == null || form.getNombrePropietario().isBlank()) {
            throw new IllegalArgumentException("El nombre del propietario es obligatorio.");
        }

        if (form.getDniPropietario() == null || form.getDniPropietario().isBlank()) {
            throw new IllegalArgumentException("El DNI del propietario es obligatorio.");
        }
    }
}
