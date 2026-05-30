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
    public synchronized Unidad registrarOActualizarUnidad(UnidadForm formulario) {
        validarUnidad(formulario);

        Condominio condominio = obtenerCondominio(formulario.getNombreCondominio());
        Optional<Unidad> existente = unidadRepository.buscarPorCondominioTorreYNumero(condominio.getId(),
                formulario.getTorre(), formulario.getNumeroUnidad());

        if (existente.isPresent() && !existente.get().getId().equals(formulario.getId())) {
            throw new IllegalArgumentException("Ya existe la Unidad '" + formulario.getNumeroUnidad()
                    + "' en la torre/bloque '" + formulario.getTorre() + "' de este condominio.");
        }

        if (formulario.getId() != null) {
            return unidadRepository.findById(formulario.getId())
                    .map(unidad -> actualizarUnidadExistente(unidad, formulario))
                    .orElseGet(() -> crearNuevaUnidad(formulario));
        }

        return existente.map(unidad -> actualizarUnidadExistente(unidad, formulario))
                .orElseGet(() -> crearNuevaUnidad(formulario));
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
        UnidadForm formulario = new UnidadForm();
        formulario.setId(unidad.getId());
        formulario.setNombreCondominio(unidad.getCondominio() != null ? unidad.getCondominio().getNombre() : null);
        formulario.setNumeroUnidad(unidad.getNumeroUnidad());
        formulario.setTorre(unidad.getTorre());
        formulario.setPiso(unidad.getPiso());
        formulario.setArea(unidad.getArea());
        return formulario;
    }

    private Unidad actualizarUnidadExistente(Unidad unidad, UnidadForm formulario) {
        Condominio condominio = obtenerCondominio(formulario.getNombreCondominio());

        if (formulario.getPiso() > condominio.getPisosPorTorre()) {
            throw new IllegalArgumentException("El piso ingresado (" + formulario.getPiso()
                    + ") supera el máximo de pisos por torre del condominio (" + condominio.getPisosPorTorre() + ").");
        }

        unidad.setNumeroUnidad(formulario.getNumeroUnidad());
        unidad.setTorre(formulario.getTorre());
        unidad.setPiso(formulario.getPiso());
        unidad.setArea(formulario.getArea());
        unidad.setCondominio(condominio);
        return unidadRepository.save(unidad);
    }

    private Unidad crearNuevaUnidad(UnidadForm formulario) {
        Condominio condominio = obtenerCondominio(formulario.getNombreCondominio());

        if (formulario.getPiso() > condominio.getPisosPorTorre()) {
            throw new IllegalArgumentException("El piso ingresado (" + formulario.getPiso()
                    + ") supera el máximo de pisos por torre del condominio (" + condominio.getPisosPorTorre() + ").");
        }

        Unidad nueva = new Unidad();
        nueva.setCondominio(condominio);
        nueva.setNumeroUnidad(formulario.getNumeroUnidad());
        nueva.setTorre(formulario.getTorre());
        nueva.setPiso(formulario.getPiso());
        nueva.setArea(formulario.getArea());
        return unidadRepository.save(nueva);
    }

    private Condominio obtenerCondominio(String nombreCondominio) {
        return condominioRepository.buscarPorNombre(nombreCondominio)
                .orElseThrow(() -> new IllegalArgumentException("Debe seleccionar un condominio valido."));
    }

    private void validarUnidad(UnidadForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de la unidad es obligatorio.");
        }
        if (formulario.getNombreCondominio() == null || formulario.getNombreCondominio().isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar un condominio.");
        }
        if (formulario.getNumeroUnidad() == null || formulario.getNumeroUnidad().isBlank()) {
            throw new IllegalArgumentException("El numero de unidad no puede estar vacio.");
        }
        if (formulario.getTorre() == null || formulario.getTorre().isBlank()) {
            throw new IllegalArgumentException("La torre no puede estar vacia.");
        }
        if (formulario.getPiso() <= 0) {
            throw new IllegalArgumentException("El piso debe ser mayor a cero.");
        }
        if (formulario.getArea() <= 0) {
            throw new IllegalArgumentException("El area debe ser mayor a cero.");
        }
    }

    @Transactional(readOnly = true)
    public synchronized AsignarOcupantesForm obtenerFormOcupantes(Long id) {
        Unidad unidad = unidadRepository.findById(id).orElse(null);
        if (unidad == null) {
            return null;
        }
        AsignarOcupantesForm formulario = new AsignarOcupantesForm();
        formulario.setId(unidad.getId());

        Propietario propietario = unidad.getPropietario();
        if (propietario != null) {
            formulario.setNombrePropietario(propietario.getNombre());
            formulario.setDniPropietario(propietario.getDni());
            formulario.setEmailPropietario(propietario.getEmail());
            formulario.setTelefonoPropietario(propietario.getTelefono());
        }

        Residente residente = unidad.getResidente();
        if (residente != null) {
            formulario.setNombreResidente(residente.getNombre());
            formulario.setEmailResidente(residente.getEmail());
            formulario.setDniResidente(residente.getDni());
            formulario.setParentesco(residente.getParentesco());
            formulario.setResidenteActivo(residente.isActivo());
        }
        return formulario;
    }

    @Transactional
    public synchronized Unidad asignarOcupantes(AsignarOcupantesForm formulario) {
        Unidad unidad = unidadRepository.findById(formulario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Unidad no encontrada."));

        String dniPropietarioForm = formulario.getDniPropietario() != null ? formulario.getDniPropietario().trim() : "";
        String dniResidenteForm = formulario.getDniResidente() != null ? formulario.getDniResidente().trim() : "";

        if (!dniPropietarioForm.isEmpty() && !dniResidenteForm.isEmpty()) {
            if (dniPropietarioForm.equalsIgnoreCase(dniResidenteForm)) {
                throw new IllegalArgumentException(
                        "El DNI del residente no puede ser igual al del propietario. Si el propietario vive en la unidad, deje los datos del residente en blanco.");
            }
        }

        boolean tieneNombrePropietario = formulario.getNombrePropietario() != null && !formulario.getNombrePropietario().isBlank();
        boolean tieneNombreResidente = formulario.getNombreResidente() != null && !formulario.getNombreResidente().isBlank();

        if (!tieneNombrePropietario && tieneNombreResidente) {
            throw new IllegalArgumentException(
                    "No puede haber un residente si no hay un propietario asignado a la unidad.");
        }

        if (tieneNombrePropietario && tieneNombreResidente) {
            if (formulario.getNombrePropietario().trim().equalsIgnoreCase(formulario.getNombreResidente().trim())) {
                throw new IllegalArgumentException(
                        "El nombre del residente no puede ser igual al del propietario. Si el propietario vive en la unidad, deje los datos del residente en blanco.");
            }
        }

        String emailPropietarioForm = formulario.getEmailPropietario() != null ? formulario.getEmailPropietario().trim() : "";
        String emailResidenteForm = formulario.getEmailResidente() != null ? formulario.getEmailResidente().trim() : "";
        if (!emailPropietarioForm.isEmpty() && !emailResidenteForm.isEmpty()) {
            if (emailPropietarioForm.equalsIgnoreCase(emailResidenteForm)) {
                throw new IllegalArgumentException(
                        "El correo electrónico del residente no puede ser igual al del propietario. Si el propietario vive en la unidad, deje los datos del residente en blanco.");
            }
        }

        String dniActualPropietario = unidad.getPropietario() != null ? unidad.getPropietario().getDni().trim() : "";
        boolean cambioDePropietario = !dniActualPropietario.equalsIgnoreCase(dniPropietarioForm);

        if (cambioDePropietario) {
            String nuevoPropietarioNombre = tieneNombrePropietario ? formulario.getNombrePropietario().trim() : "Sin propietario";
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
            propietario.setNombre(formulario.getNombrePropietario().trim());
            propietario.setDni(dniPropietarioForm);
            propietario.setEmail(emailPropietarioForm.isEmpty() ? null : emailPropietarioForm);
            propietario.setTelefono(formulario.getTelefonoPropietario() != null ? formulario.getTelefonoPropietario().trim() : null);
            unidad.setPropietario(propietario);
        } else {
            unidad.setPropietario(null);
        }

        if (tieneNombreResidente) {
            Residente residente = unidad.getResidente() != null ? unidad.getResidente() : new Residente();
            residente.setUnidad(unidad);
            residente.setNombre(formulario.getNombreResidente().trim());
            residente.setDni(dniResidenteForm);
            residente.setEmail(emailResidenteForm.isEmpty() ? null : emailResidenteForm);
            residente.setParentesco(formulario.getParentesco() != null ? formulario.getParentesco().trim() : null);
            residente.setActivo(formulario.isResidenteActivo());
            unidad.setResidente(residente);
        } else {
            unidad.setResidente(null);
        }

        return unidadRepository.save(unidad);
    }
}
