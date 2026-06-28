package pe.edu.utp.condominio.api.dominios.mantenimiento.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request.InsumoForm;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.response.InsumoResponse;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.InsumoMantenimiento;
import pe.edu.utp.condominio.api.dominios.mantenimiento.repositories.InsumoMantenimientoRepository;

@Service
public class InsumoService {

    private final InsumoMantenimientoRepository insumoRepository;

    public InsumoService(InsumoMantenimientoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    @Transactional
    public synchronized InsumoResponse registrarInsumo(InsumoForm formulario) {
        InsumoMantenimiento insumo = new InsumoMantenimiento();
        insumo.setNombre(formulario.getNombre());
        insumo.setUnidadMedida(formulario.getUnidadMedida());
        insumo.setStockActual(formulario.getStockActual());
        insumo.setStockMinimo(formulario.getStockMinimo());
        insumo.setPrecioUnitario(formulario.getPrecioUnitario());

        InsumoMantenimiento guardado = insumoRepository.save(insumo);
        return mapearInsumoAResponse(guardado);
    }

    @Transactional
    public synchronized InsumoMantenimiento reducirStock(Long insumoId, double cantidadReducir) {
        InsumoMantenimiento insumo = insumoRepository.findById(insumoId)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + insumoId));

        if (insumo.getStockActual() < cantidadReducir) {
            throw new RuntimeException("Stock insuficiente para el insumo: " + insumo.getNombre());
        }
        
        insumo.setStockActual(insumo.getStockActual() - cantidadReducir);
        return insumoRepository.save(insumo);
    }

    @Transactional(readOnly = true)
    public synchronized List<InsumoResponse> listarInsumos() {
        return insumoRepository.findAll().stream()
                .map(this::mapearInsumoAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public synchronized List<InsumoResponse> listarInsumosCriticos() {
        return insumoRepository.listarCriticos().stream()
                .map(this::mapearInsumoAResponse)
                .collect(Collectors.toList());
    }

    private InsumoResponse mapearInsumoAResponse(InsumoMantenimiento entidad) {
        return new InsumoResponse(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getUnidadMedida(),
                entidad.getStockActual(),
                entidad.getStockMinimo(),
                entidad.getPrecioUnitario(),
                entidad.getFechaActualizacion());
    }
}
