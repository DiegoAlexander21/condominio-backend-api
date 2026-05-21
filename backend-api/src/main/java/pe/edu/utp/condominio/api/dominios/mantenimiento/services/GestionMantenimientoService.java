package pe.edu.utp.condominio.api.dominios.mantenimiento.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.MetodoDistribucion;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Gasto;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.GastoRepository;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request.InsumoForm;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request.TareaMantenimientoForm;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request.UsoInsumoForm;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.response.InsumoResponse;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.response.TareaMantenimientoResponse;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.response.UsoInsumoResponse;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.InsumoMantenimiento;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.TareaMantenimiento;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.UsoInsumo;
import pe.edu.utp.condominio.api.dominios.mantenimiento.repositories.InsumoMantenimientoRepository;
import pe.edu.utp.condominio.api.dominios.mantenimiento.repositories.TareaMantenimientoRepository;

@Service
public class GestionMantenimientoService {

    private final InsumoMantenimientoRepository insumoRepository;
    private final TareaMantenimientoRepository tareaRepository;
    private final AreaComunRepository areaComunRepository;
    private final GastoRepository gastoRepository;

    public GestionMantenimientoService(
            InsumoMantenimientoRepository insumoRepository,
            TareaMantenimientoRepository tareaRepository,
            AreaComunRepository areaComunRepository,
            GastoRepository gastoRepository) {
        this.insumoRepository = insumoRepository;
        this.tareaRepository = tareaRepository;
        this.areaComunRepository = areaComunRepository;
        this.gastoRepository = gastoRepository;
    }

    @Transactional
    public InsumoResponse registrarInsumo(InsumoForm formulario) {
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
    public TareaMantenimientoResponse registrarTareaConInsumos(TareaMantenimientoForm formulario) {
        AreaComun area = areaComunRepository.findById(formulario.getAreaComunId())
                .orElseThrow(() -> new RuntimeException("Ãrea comÃºn no encontrada"));

        TareaMantenimiento tarea = new TareaMantenimiento();
        tarea.setAreaComun(area);
        tarea.setDescripcion(formulario.getDescripcion());
        tarea.setFechaProgramada(formulario.getFechaProgramada());

        double costoTotalInsumos = 0;

        if (formulario.getUsosInsumos() != null) {
            for (UsoInsumoForm usoForm : formulario.getUsosInsumos()) {
                InsumoMantenimiento insumo = insumoRepository.findById(usoForm.getInsumoId())
                        .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + usoForm.getInsumoId()));

                if (insumo.getStockActual() < usoForm.getCantidadUsada()) {
                    throw new RuntimeException("Stock insuficiente para el insumo: " + insumo.getNombre());
                }
                insumo.setStockActual(insumo.getStockActual() - usoForm.getCantidadUsada());
                insumoRepository.save(insumo);

                UsoInsumo uso = new UsoInsumo();
                uso.setTarea(tarea);
                uso.setInsumo(insumo);
                uso.setCantidadUsada(usoForm.getCantidadUsada());
                tarea.getUsosInsumos().add(uso);
                costoTotalInsumos += (usoForm.getCantidadUsada() * insumo.getPrecioUnitario());
            }
        }

        TareaMantenimiento guardada = tareaRepository.save(tarea);

        if (costoTotalInsumos > 0) {
            Gasto gasto = new Gasto();
            gasto.setDescripcion("Mantenimiento: " + tarea.getDescripcion());
            gasto.setTipoGasto(TipoGasto.FIJO);
            gasto.setMetodoDistribucion(MetodoDistribucion.PARTES_IGUALES);
            gasto.setMontoTotal(costoTotalInsumos);
            gastoRepository.save(gasto);
        }

        return mapearTareaAResponse(guardada);
    }

    public List<InsumoResponse> listarInsumos() {
        return insumoRepository.findAll().stream()
                .map(this::mapearInsumoAResponse)
                .collect(Collectors.toList());
    }

    public List<InsumoResponse> listarInsumosCriticos() {
        return insumoRepository.listarCriticos().stream()
                .map(this::mapearInsumoAResponse)
                .collect(Collectors.toList());
    }

    private InsumoResponse mapearInsumoAResponse(InsumoMantenimiento entity) {
        return new InsumoResponse(
                entity.getId(),
                entity.getNombre(),
                entity.getUnidadMedida(),
                entity.getStockActual(),
                entity.getStockMinimo(),
                entity.getPrecioUnitario(),
                entity.getFechaActualizacion());
    }

    private TareaMantenimientoResponse mapearTareaAResponse(TareaMantenimiento entity) {
        double costoTotal = 0;
        List<UsoInsumoResponse> usos = entity.getUsosInsumos().stream().map(u -> {
            double subtotal = u.getCantidadUsada() * u.getInsumo().getPrecioUnitario();
            return new UsoInsumoResponse(
                    u.getId(),
                    u.getInsumo().getNombre(),
                    u.getCantidadUsada(),
                    u.getInsumo().getUnidadMedida(),
                    u.getInsumo().getPrecioUnitario(),
                    subtotal);
        }).collect(Collectors.toList());

        for (UsoInsumoResponse u : usos) {
            costoTotal += u.getSubtotal();
        }

        return new TareaMantenimientoResponse(
                entity.getId(),
                entity.getAreaComun().getNombre(),
                entity.getDescripcion(),
                entity.getFechaProgramada(),
                usos,
                costoTotal);
    }
}

