package pe.edu.utp.condominio.api.dominios.saludambiental.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.request.MantenimientoAmbientalForm;
import pe.edu.utp.condominio.api.dominios.saludambiental.dto.response.MantenimientoAmbientalResponse;
import pe.edu.utp.condominio.api.dominios.saludambiental.models.RegistroMantenimientoAmbiental;
import pe.edu.utp.condominio.api.dominios.saludambiental.repositories.RegistroMantenimientoAmbientalRepository;

@Service
public class MantenimientoAmbientalService {

    private final RegistroMantenimientoAmbientalRepository mantenimientoRepository;
    private final AreaComunRepository areaComunRepository;

    public MantenimientoAmbientalService(RegistroMantenimientoAmbientalRepository mantenimientoRepository,
            AreaComunRepository areaComunRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.areaComunRepository = areaComunRepository;
    }

    @Transactional
    public MantenimientoAmbientalResponse registrarMantenimiento(MantenimientoAmbientalForm formulario) {
        AreaComun area = areaComunRepository.findById(formulario.getAreaComunId())
                .orElseThrow(() -> new IllegalArgumentException("Area comun no encontrada"));

        RegistroMantenimientoAmbiental registro = new RegistroMantenimientoAmbiental();
        registro.setAreaComun(area);
        registro.setDescripcion(formulario.getDescripcion());
        registro.setResponsable(formulario.getResponsable());

        RegistroMantenimientoAmbiental guardado = mantenimientoRepository.save(registro);
        return mapearMantenimientoAResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<MantenimientoAmbientalResponse> obtenerHistorialMantenimiento(Long areaId) {
        return mantenimientoRepository.listarPorArea(areaId).stream()
                .map(this::mapearMantenimientoAResponse)
                .collect(Collectors.toList());
    }

    private MantenimientoAmbientalResponse mapearMantenimientoAResponse(RegistroMantenimientoAmbiental entidad) {
        return new MantenimientoAmbientalResponse(
                entidad.getId(),
                entidad.getAreaComun().getNombre(),
                entidad.getDescripcion(),
                entidad.getFechaRegistro(),
                entidad.getResponsable());
    }
}
