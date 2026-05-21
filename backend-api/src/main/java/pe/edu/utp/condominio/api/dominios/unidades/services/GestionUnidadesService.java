package pe.edu.utp.condominio.api.dominios.unidades.services;

import java.util.List;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.UnidadForm;
import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestionUnidadesService {

    private final UnidadRepository unidadRepository;
    private final CondominioRepository condominioRepository;

    public GestionUnidadesService(UnidadRepository unidadRepository,
            CondominioRepository condominioRepository) {
        this.unidadRepository = unidadRepository;
        this.condominioRepository = condominioRepository;
    }

    @Transactional
    public synchronized Unidad registrarOActualizarUnidad(UnidadForm form) {
        validarUnidad(form);

        if (form.getId() != null) {
            return unidadRepository.findById(form.getId())
                    .map(unidad -> actualizarUnidadExistente(unidad, form))
                    .orElseGet(() -> crearNuevaUnidad(form));
        }

        return unidadRepository.buscarPorClave(form.getNumeroUnidad(), form.getTorre(), form.getPiso())
                .map(unidad -> actualizarUnidadExistente(unidad, form))
                .orElseGet(() -> crearNuevaUnidad(form));
    }

    public synchronized List<Unidad> obtenerUnidades() {
        return unidadRepository.listarTodosOrdenado();
    }

    public synchronized int contarPropietarios() {
        List<Unidad> unidades = unidadRepository.findAll();
        int contador = 0;
        for (Unidad unidad : unidades) {
            if (unidad.getNombrePropietario() != null && !unidad.getNombrePropietario().isBlank()) {
                contador++;
            }
        }
        return contador;
    }

    public synchronized int contarResidentesActivos() {
        List<Unidad> unidades = unidadRepository.findAll();
        int contador = 0;
        for (Unidad unidad : unidades) {
            if (unidad.isResidenteActivo() && unidad.getNombreResidente() != null) {
                contador++;
            }
        }
        return contador;
    }

    public synchronized Unidad buscarPorId(Long id) {
        return unidadRepository.findById(id).orElse(null);
    }

    public UnidadForm convertirAForm(Unidad unidad) {
        UnidadForm form = new UnidadForm();
        form.setId(unidad.getId());
        form.setNombreCondominio(unidad.getCondominio() != null ? unidad.getCondominio().getNombre() : null);
        form.setNumeroUnidad(unidad.getNumeroUnidad());
        form.setTorre(unidad.getTorre());
        form.setPiso(unidad.getPiso());
        form.setArea(unidad.getArea());
        form.setNombrePropietario(unidad.getNombrePropietario());
        form.setDniPropietario(unidad.getDniPropietario());
        form.setEmailPropietario(unidad.getEmailPropietario());
        form.setTelefonoPropietario(unidad.getTelefonoPropietario());
        form.setNombreResidente(unidad.getNombreResidente());
        form.setEmailResidente(unidad.getEmailResidente());
        form.setDniResidente(unidad.getDniResidente());
        form.setParentesco(unidad.getParentesco());
        form.setResidenteActivo(unidad.isResidenteActivo());
        return form;
    }

    private Unidad actualizarUnidadExistente(Unidad unidad, UnidadForm form) {
        Condominio condominio = obtenerCondominio(form.getNombreCondominio());
        unidad.setNumeroUnidad(form.getNumeroUnidad());
        unidad.setTorre(form.getTorre());
        unidad.setPiso(form.getPiso());
        unidad.setArea(form.getArea());
        unidad.setCondominio(condominio);
        unidad.setNombrePropietario(form.getNombrePropietario());
        unidad.setDniPropietario(form.getDniPropietario());
        unidad.setEmailPropietario(form.getEmailPropietario());
        unidad.setTelefonoPropietario(form.getTelefonoPropietario());
        unidad.setNombreResidente(form.getNombreResidente());
        unidad.setEmailResidente(form.getEmailResidente());
        unidad.setDniResidente(form.getDniResidente());
        unidad.setParentesco(form.getParentesco());
        unidad.setResidenteActivo(form.isResidenteActivo());
        return unidadRepository.save(unidad);
    }

    private Unidad crearNuevaUnidad(UnidadForm form) {
        Condominio condominio = obtenerCondominio(form.getNombreCondominio());
        Unidad nueva = new Unidad();
        nueva.setCondominio(condominio);
        nueva.setNumeroUnidad(form.getNumeroUnidad());
        nueva.setTorre(form.getTorre());
        nueva.setPiso(form.getPiso());
        nueva.setArea(form.getArea());
        nueva.setNombrePropietario(form.getNombrePropietario());
        nueva.setDniPropietario(form.getDniPropietario());
        nueva.setEmailPropietario(form.getEmailPropietario());
        nueva.setTelefonoPropietario(form.getTelefonoPropietario());
        nueva.setNombreResidente(form.getNombreResidente());
        nueva.setEmailResidente(form.getEmailResidente());
        nueva.setDniResidente(form.getDniResidente());
        nueva.setParentesco(form.getParentesco());
        nueva.setResidenteActivo(form.isResidenteActivo());
        return unidadRepository.save(nueva);
    }

    private Condominio obtenerCondominio(String nombreCondominio) {
        return condominioRepository.buscarPorNombre(nombreCondominio)
                .orElseThrow(() -> new IllegalArgumentException("Debe seleccionar un condominio valido."));
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

