package pe.edu.utp.condominio.api.dominios.unidades.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;
import pe.edu.utp.condominio.api.dominios.historial.models.HistorialTitularidad;
import pe.edu.utp.condominio.api.dominios.historial.repositories.HistorialTitularidadRepository;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.AsignarOcupantesForm;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.UnidadForm;
import pe.edu.utp.condominio.api.dominios.unidades.models.Propietario;
import pe.edu.utp.condominio.api.dominios.unidades.models.Residente;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class GestionUnidadesService {

    private final UnidadRepository unidadRepository;
    private final CondominioRepository condominioRepository;
    private final HistorialTitularidadRepository historialTitularidadRepository;

    public GestionUnidadesService(UnidadRepository unidadRepository,
            CondominioRepository condominioRepository,
            HistorialTitularidadRepository historialTitularidadRepository) {
        this.unidadRepository = unidadRepository;
        this.condominioRepository = condominioRepository;
        this.historialTitularidadRepository = historialTitularidadRepository;
    }

    @Transactional
    public synchronized Unidad registrarOActualizarUnidad(UnidadForm form) {
        validarUnidad(form);

        Condominio condominio = obtenerCondominio(form.getNombreCondominio());
        Optional<Unidad> existente = unidadRepository.buscarPorCondominioTorreYNumero(condominio.getId(),
                form.getTorre(), form.getNumeroUnidad());

        if (existente.isPresent() && !existente.get().getId().equals(form.getId())) {
            throw new IllegalArgumentException("Ya existe la Unidad '" + form.getNumeroUnidad()
                    + "' en la torre/bloque '" + form.getTorre() + "' de este condominio.");
        }

        if (form.getId() != null) {
            return unidadRepository.findById(form.getId())
                    .map(unidad -> actualizarUnidadExistente(unidad, form))
                    .orElseGet(() -> crearNuevaUnidad(form));
        }

        return existente.map(unidad -> actualizarUnidadExistente(unidad, form))
                .orElseGet(() -> crearNuevaUnidad(form));
    }

    public synchronized List<Unidad> obtenerUnidades() {
        return unidadRepository.listarTodosConCondominioOrdenado();
    }

    public synchronized List<String> listarTorresPorCondominio(Long condominioId) {
        if (condominioId == null) return List.of();
        return unidadRepository.listarTorresPorCondominio(condominioId);
    }

    public synchronized List<Unidad> buscarUnidadesPorDni(String dni) {
        if (dni == null || dni.isBlank()) return List.of();
        return unidadRepository.buscarPorDniOcupante(dni.trim());
    }

    public synchronized int contarPropietarios() {
        return (int) unidadRepository.findAll().stream()
                .filter(unidad -> unidad.getPropietario() != null)
                .count();
    }

    public synchronized int contarResidentesActivos() {
        return (int) unidadRepository.findAll().stream()
                .filter(unidad -> unidad.getResidente() != null && unidad.getResidente().isActivo())
                .count();
    }

    public synchronized Unidad buscarPorId(Long id) {
        return unidadRepository.findById(id).orElse(null);
    }

    @Transactional
    public synchronized void eliminarUnidad(Long id) {
        unidadRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public synchronized UnidadForm obtenerFormUnidad(Long id) {
        Unidad unidad = unidadRepository.findById(id).orElse(null);
        if (unidad == null) {
            return null;
        }
        return convertirAForm(unidad);
    }

    public UnidadForm convertirAForm(Unidad unidad) {
        UnidadForm form = new UnidadForm();
        form.setId(unidad.getId());
        form.setNombreCondominio(unidad.getCondominio() != null ? unidad.getCondominio().getNombre() : null);
        form.setNumeroUnidad(unidad.getNumeroUnidad());
        form.setTorre(unidad.getTorre());
        form.setPiso(unidad.getPiso());
        form.setArea(unidad.getArea());
        return form;
    }

    private Unidad actualizarUnidadExistente(Unidad unidad, UnidadForm form) {
        Condominio condominio = obtenerCondominio(form.getNombreCondominio());

        if (form.getPiso() > condominio.getPisosPorTorre()) {
            throw new IllegalArgumentException("El piso ingresado (" + form.getPiso()
                    + ") supera el máximo de pisos por torre del condominio (" + condominio.getPisosPorTorre() + ").");
        }

        unidad.setNumeroUnidad(form.getNumeroUnidad());
        unidad.setTorre(form.getTorre());
        unidad.setPiso(form.getPiso());
        unidad.setArea(form.getArea());
        unidad.setCondominio(condominio);
        return unidadRepository.save(unidad);
    }

    private Unidad crearNuevaUnidad(UnidadForm form) {
        Condominio condominio = obtenerCondominio(form.getNombreCondominio());

        if (form.getPiso() > condominio.getPisosPorTorre()) {
            throw new IllegalArgumentException("El piso ingresado (" + form.getPiso()
                    + ") supera el máximo de pisos por torre del condominio (" + condominio.getPisosPorTorre() + ").");
        }

        Unidad nueva = new Unidad();
        nueva.setCondominio(condominio);
        nueva.setNumeroUnidad(form.getNumeroUnidad());
        nueva.setTorre(form.getTorre());
        nueva.setPiso(form.getPiso());
        nueva.setArea(form.getArea());
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
    }

    @Transactional(readOnly = true)
    public synchronized AsignarOcupantesForm obtenerFormOcupantes(Long id) {
        Unidad unidad = unidadRepository.findById(id).orElse(null);
        if (unidad == null) {
            return null;
        }
        AsignarOcupantesForm form = new AsignarOcupantesForm();
        form.setId(unidad.getId());

        Propietario propietario = unidad.getPropietario();
        if (propietario != null) {
            form.setNombrePropietario(propietario.getNombre());
            form.setDniPropietario(propietario.getDni());
            form.setEmailPropietario(propietario.getEmail());
            form.setTelefonoPropietario(propietario.getTelefono());
        }

        Residente residente = unidad.getResidente();
        if (residente != null) {
            form.setNombreResidente(residente.getNombre());
            form.setEmailResidente(residente.getEmail());
            form.setDniResidente(residente.getDni());
            form.setParentesco(residente.getParentesco());
            form.setResidenteActivo(residente.isActivo());
        }
        return form;
    }

    @Transactional
    public synchronized Unidad asignarOcupantes(AsignarOcupantesForm form) {
        Unidad unidad = unidadRepository.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Unidad no encontrada."));

        String dniPropietarioForm = form.getDniPropietario() != null ? form.getDniPropietario().trim() : "";
        String dniResidenteForm = form.getDniResidente() != null ? form.getDniResidente().trim() : "";

        if (!dniPropietarioForm.isEmpty() && !dniResidenteForm.isEmpty()) {
            if (dniPropietarioForm.equalsIgnoreCase(dniResidenteForm)) {
                throw new IllegalArgumentException(
                        "El DNI del residente no puede ser igual al del propietario. Si el propietario vive en la unidad, deje los datos del residente en blanco.");
            }
        }

        boolean tieneNombrePropietario = form.getNombrePropietario() != null && !form.getNombrePropietario().isBlank();
        boolean tieneNombreResidente = form.getNombreResidente() != null && !form.getNombreResidente().isBlank();

        if (!tieneNombrePropietario && tieneNombreResidente) {
            throw new IllegalArgumentException(
                    "No puede haber un residente si no hay un propietario asignado a la unidad.");
        }

        if (tieneNombrePropietario && tieneNombreResidente) {
            if (form.getNombrePropietario().trim().equalsIgnoreCase(form.getNombreResidente().trim())) {
                throw new IllegalArgumentException(
                        "El nombre del residente no puede ser igual al del propietario. Si el propietario vive en la unidad, deje los datos del residente en blanco.");
            }
        }

        String emailPropietarioForm = form.getEmailPropietario() != null ? form.getEmailPropietario().trim() : "";
        String emailResidenteForm = form.getEmailResidente() != null ? form.getEmailResidente().trim() : "";
        if (!emailPropietarioForm.isEmpty() && !emailResidenteForm.isEmpty()) {
            if (emailPropietarioForm.equalsIgnoreCase(emailResidenteForm)) {
                throw new IllegalArgumentException(
                        "El correo electrónico del residente no puede ser igual al del propietario. Si el propietario vive en la unidad, deje los datos del residente en blanco.");
            }
        }

        String dniActualPropietario = unidad.getPropietario() != null ? unidad.getPropietario().getDni().trim() : "";
        boolean cambioDePropietario = !dniActualPropietario.equalsIgnoreCase(dniPropietarioForm);

        if (cambioDePropietario) {
            String nuevoPropietarioNombre = tieneNombrePropietario ? form.getNombrePropietario().trim() : "Sin propietario";
            String viejoPropietarioNombre = unidad.getPropietario() != null ? unidad.getPropietario().getNombre().trim() : "Sin propietario";

            if (!nuevoPropietarioNombre.equals("Sin propietario") && viejoPropietarioNombre.equalsIgnoreCase(nuevoPropietarioNombre)) {
                throw new IllegalArgumentException(
                        "Si cambia el DNI del propietario, también debe actualizar el nombre completo del nuevo dueño.");
            }

            HistorialTitularidad historial = new HistorialTitularidad();
            historial.setUnidad(unidad);
            historial.setPropietarioAnterior(viejoPropietarioNombre);
            historial.setNuevoPropietario(nuevoPropietarioNombre);
            historialTitularidadRepository.save(historial);
        }

        if (tieneNombrePropietario) {
            Propietario propietario = unidad.getPropietario() != null ? unidad.getPropietario() : new Propietario();
            propietario.setUnidad(unidad);
            propietario.setNombre(form.getNombrePropietario().trim());
            propietario.setDni(dniPropietarioForm);
            propietario.setEmail(emailPropietarioForm.isEmpty() ? null : emailPropietarioForm);
            propietario.setTelefono(form.getTelefonoPropietario() != null ? form.getTelefonoPropietario().trim() : null);
            unidad.setPropietario(propietario);
        } else {
            unidad.setPropietario(null);
        }

        if (tieneNombreResidente) {
            Residente residente = unidad.getResidente() != null ? unidad.getResidente() : new Residente();
            residente.setUnidad(unidad);
            residente.setNombre(form.getNombreResidente().trim());
            residente.setDni(dniResidenteForm);
            residente.setEmail(emailResidenteForm.isEmpty() ? null : emailResidenteForm);
            residente.setParentesco(form.getParentesco() != null ? form.getParentesco().trim() : null);
            residente.setActivo(form.isResidenteActivo());
            unidad.setResidente(residente);
        } else {
            unidad.setResidente(null);
        }

        return unidadRepository.save(unidad);
    }
}
