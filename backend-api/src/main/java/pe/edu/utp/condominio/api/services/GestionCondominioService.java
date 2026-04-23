package pe.edu.utp.condominio.api.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import pe.edu.utp.condominio.api.dto.CondominioForm;
import pe.edu.utp.condominio.api.models.Condominio;
import org.springframework.stereotype.Service;

@Service
public class GestionCondominioService {

    private final List<Condominio> condominios = new ArrayList<>();

    private long secuenciaCondominio = 1L;

    public synchronized Condominio registrarOActualizarCondominio(CondominioForm form) {
        validarCondominio(form);

        String nombreNormalizado = form.getNombre().trim();
        int torres = form.getTorres();
        int pisosPorTorre = form.getPisosPorTorre();

        for (Condominio condominio : condominios) {
            if (condominio.getNombre().equalsIgnoreCase(nombreNormalizado)) {
                condominio.setNombre(nombreNormalizado);
                condominio.setTorres(torres);
                condominio.setPisosPorTorre(pisosPorTorre);
                condominio.setFechaActualizacion(LocalDateTime.now());
                return condominio;
            }
        }

        Condominio nuevo = new Condominio();
        nuevo.setId(secuenciaCondominio++);
        nuevo.setNombre(nombreNormalizado);
        nuevo.setTorres(torres);
        nuevo.setPisosPorTorre(pisosPorTorre);
        nuevo.setFechaRegistro(LocalDateTime.now());
        nuevo.setFechaActualizacion(LocalDateTime.now());

        condominios.add(nuevo);
        return nuevo;
    }

    public synchronized List<Condominio> obtenerCondominios() {
        List<Condominio> copia = new ArrayList<>(condominios);
        copia.sort(Comparator.comparing(Condominio::getNombre, String.CASE_INSENSITIVE_ORDER));
        return List.copyOf(copia);
    }

    public synchronized int obtenerTotalTorres() {
        int total = 0;

        for (Condominio condominio : condominios) {
            total += condominio.getTorres();
        }

        return total;
    }

    public synchronized int obtenerTotalPisos() {
        int total = 0;

        for (Condominio condominio : condominios) {
            total += condominio.getTorres() * condominio.getPisosPorTorre();
        }

        return total;
    }

    private void validarCondominio(CondominioForm form) {
        if (form == null) {
            throw new IllegalArgumentException("El formulario del condominio es obligatorio.");
        }

        if (form.getNombre() == null || form.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del condominio no puede estar vacio.");
        }

        if (form.getTorres() == null || form.getTorres() <= 0) {
            throw new IllegalArgumentException("El numero de torres debe ser mayor a cero.");
        }

        if (form.getTorres() > 100) {
            throw new IllegalArgumentException("El numero de torres no puede ser mayor a 100.");
        }

        if (form.getPisosPorTorre() == null || form.getPisosPorTorre() <= 0) {
            throw new IllegalArgumentException("El numero de pisos por torre debe ser mayor a cero.");
        }

        if (form.getPisosPorTorre() > 200) {
            throw new IllegalArgumentException("El numero de pisos por torre no puede ser mayor a 200.");
        }
    }
}
