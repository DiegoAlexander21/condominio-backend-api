package pe.edu.utp.condominio.api.dominios.mantenimiento.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;
import pe.edu.utp.condominio.api.dominios.areascomunes.repositories.AreaComunRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.MetodoDistribucion;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Gasto;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.GastoRepository;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request.TareaMantenimientoForm;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request.UsoInsumoForm;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.response.TareaMantenimientoResponse;
import pe.edu.utp.condominio.api.dominios.mantenimiento.dto.response.UsoInsumoResponse;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.InsumoMantenimiento;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.TareaMantenimiento;
import pe.edu.utp.condominio.api.dominios.mantenimiento.models.UsoInsumo;
import pe.edu.utp.condominio.api.dominios.mantenimiento.repositories.TareaMantenimientoRepository;

@Service
public class TareaMantenimientoService {

    private final TareaMantenimientoRepository tareaRepository;
    private final AreaComunRepository areaComunRepository;
    private final GastoRepository gastoRepository;
    private final InsumoService insumoService;

    public TareaMantenimientoService(
            TareaMantenimientoRepository tareaRepository,
            AreaComunRepository areaComunRepository,
            GastoRepository gastoRepository,
            InsumoService insumoService) {
        this.tareaRepository = tareaRepository;
        this.areaComunRepository = areaComunRepository;
        this.gastoRepository = gastoRepository;
        this.insumoService = insumoService;
    }

    @Transactional
    public synchronized TareaMantenimientoResponse registrarTareaConInsumos(TareaMantenimientoForm formulario) {
        AreaComun area = areaComunRepository.findById(formulario.getAreaComunId())
                .orElseThrow(() -> new RuntimeException("Area comun no encontrada"));

        TareaMantenimiento tarea = new TareaMantenimiento();
        tarea.setAreaComun(area);
        tarea.setDescripcion(formulario.getDescripcion());
        tarea.setFechaProgramada(formulario.getFechaProgramada());

        double costoTotalInsumos = 0;

        if (formulario.getUsosInsumos() != null) {
            for (UsoInsumoForm formularioUso : formulario.getUsosInsumos()) {
                InsumoMantenimiento insumo = insumoService.reducirStock(formularioUso.getInsumoId(), formularioUso.getCantidadUsada());

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

    @Transactional(readOnly = true)
    public Page<TareaMantenimientoResponse> obtenerHistorialTareas(Pageable paginacion) {
        return tareaRepository.findAll(paginacion).map(this::mapearTareaAResponse);
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
