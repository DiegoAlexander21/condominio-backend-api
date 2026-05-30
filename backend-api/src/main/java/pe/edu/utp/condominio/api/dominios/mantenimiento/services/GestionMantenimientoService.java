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
            for (UsoInsumoForm formularioUso : formulario.getUsosInsumos()) {
                InsumoMantenimiento insumo = insumoRepository.findById(formularioUso.getInsumoId())
                        .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + formularioUso.getInsumoId()));

                if (insumo.getStockActual() < formularioUso.getCantidadUsada()) {
                    throw new RuntimeException("Stock insuficiente para el insumo: " + insumo.getNombre());
                }
                insumo.setStockActual(insumo.getStockActual() - formularioUso.getCantidadUsada());
                insumoRepository.save(insumo);

                UsoInsumo uso = new UsoInsumo();
                uso.setTarea(tarea);
                uso.setInsumo(insumo);
                uso.setCantidadUsada(formularioUso.getCantidadUsada());
                tarea.getUsosInsumos().add(uso);
                costoTotalInsumos += (formularioUso.getCantidadUsada() * insumo.getPrecioUnitario());
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

    private TareaMantenimientoResponse mapearTareaAResponse(TareaMantenimiento entidad) {
        double costoTotal = 0;
        List<UsoInsumoResponse> usos = entidad.getUsosInsumos().stream().map(uso -> {
            double subtotal = uso.getCantidadUsada() * uso.getInsumo().getPrecioUnitario();
            return new UsoInsumoResponse(
                    uso.getId(),
                    uso.getInsumo().getNombre(),
                    uso.getCantidadUsada(),
                    uso.getInsumo().getUnidadMedida(),
                    uso.getInsumo().getPrecioUnitario(),
                    subtotal);
        }).collect(Collectors.toList());

        for (UsoInsumoResponse uso : usos) {
            costoTotal += uso.getSubtotal();
        }

        return new TareaMantenimientoResponse(
                entidad.getId(),
                entidad.getAreaComun().getNombre(),
                entidad.getDescripcion(),
                entidad.getFechaProgramada(),
                usos,
                costoTotal);
    }
}

