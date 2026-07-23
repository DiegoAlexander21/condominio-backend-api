package pe.edu.utp.condominio.api.dominios.unidades.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.historial.models.HistorialTitularidad;
import pe.edu.utp.condominio.api.dominios.historial.repositories.HistorialTitularidadRepository;
import pe.edu.utp.condominio.api.dominios.unidades.dto.request.AsignarOcupantesForm;
import pe.edu.utp.condominio.api.dominios.unidades.models.Propietario;
import pe.edu.utp.condominio.api.dominios.unidades.models.Residente;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class OcupanteService {

    private final UnidadRepository unidadRepository;
    private final HistorialTitularidadRepository historialTitularidadRepository;

    public OcupanteService(UnidadRepository unidadRepository,
            HistorialTitularidadRepository historialTitularidadRepository) {
        this.unidadRepository = unidadRepository;
        this.historialTitularidadRepository = historialTitularidadRepository;
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
        String nombreActualPropietario = unidad.getPropietario() != null ? unidad.getPropietario().getNombre().trim() : "";

        boolean cambioDeDni = !dniActualPropietario.equalsIgnoreCase(dniPropietarioForm);
        boolean cambioDeNombre = !nombreActualPropietario.equalsIgnoreCase(formulario.getNombrePropietario() != null ? formulario.getNombrePropietario().trim() : "");

        boolean cambioDePropietario = cambioDeDni || cambioDeNombre;

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
            
            if (unidad.getHistorialTitularidad() != null) {
                unidad.getHistorialTitularidad().add(historial);
            }
            
            historialTitularidadRepository.save(historial);
        }

        if (tieneNombrePropietario) {
            if (dniPropietarioForm.isEmpty() || !dniPropietarioForm.matches("^[0-9]{8}$")) {
                throw new IllegalArgumentException("El DNI del propietario es obligatorio y debe tener exactamente 8 dígitos.");
            }
            if (emailPropietarioForm.isEmpty() || !emailPropietarioForm.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("El correo del propietario es obligatorio y debe tener un formato válido.");
            }
            String telefonoPropietarioForm = formulario.getTelefonoPropietario() != null ? formulario.getTelefonoPropietario().trim() : "";
            if (telefonoPropietarioForm.isEmpty() || !telefonoPropietarioForm.matches("^[0-9]{9}$")) {
                throw new IllegalArgumentException("El teléfono del propietario es obligatorio y debe tener 9 dígitos.");
            }

            Propietario propietario = unidad.getPropietario() != null ? unidad.getPropietario() : new Propietario();
            propietario.setUnidad(unidad);
            propietario.setNombre(formulario.getNombrePropietario().trim());
            propietario.setDni(dniPropietarioForm);
            propietario.setEmail(emailPropietarioForm);
            propietario.setTelefono(telefonoPropietarioForm);
            unidad.setPropietario(propietario);
        } else {
            unidad.setPropietario(null);
        }

        if (tieneNombreResidente) {
            if (dniResidenteForm.isEmpty() || !dniResidenteForm.matches("^[0-9]{8}$")) {
                throw new IllegalArgumentException("El DNI del residente es obligatorio y debe tener exactamente 8 dígitos.");
            }
            if (emailResidenteForm.isEmpty() || !emailResidenteForm.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("El correo del residente es obligatorio y debe tener un formato válido.");
            }
            String parentescoForm = formulario.getParentesco() != null ? formulario.getParentesco().trim() : "";
            if (parentescoForm.isEmpty()) {
                throw new IllegalArgumentException("El parentesco del residente es obligatorio.");
            }

            Residente residente = unidad.getResidente() != null ? unidad.getResidente() : new Residente();
            residente.setUnidad(unidad);
            residente.setNombre(formulario.getNombreResidente().trim());
            residente.setDni(dniResidenteForm);
            residente.setEmail(emailResidenteForm);
            residente.setParentesco(parentescoForm);
            residente.setActivo(formulario.isResidenteActivo());
            unidad.setResidente(residente);
        } else {
            unidad.setResidente(null);
        }
        
        Unidad guardada = unidadRepository.save(unidad);
        if (guardada.getCondominio() != null) {
            org.hibernate.Hibernate.initialize(guardada.getCondominio());
        }
        return guardada;
    }
}
