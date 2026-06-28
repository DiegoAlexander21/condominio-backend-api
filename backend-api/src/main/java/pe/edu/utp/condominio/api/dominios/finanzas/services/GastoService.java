package pe.edu.utp.condominio.api.dominios.finanzas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.condominio.models.Condominio;
import pe.edu.utp.condominio.api.dominios.condominio.repositories.CondominioRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.GastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.DetalleGastoUnidadResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.GastoResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.MetodoDistribucion;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;
import pe.edu.utp.condominio.api.dominios.finanzas.models.DetalleGastoUnidad;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Gasto;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.DetalleGastoUnidadRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.GastoRepository;
import pe.edu.utp.condominio.api.dominios.incidencias.models.Incidencia;
import pe.edu.utp.condominio.api.dominios.incidencias.models.IncidenciaUnidad;
import pe.edu.utp.condominio.api.dominios.incidencias.repositories.IncidenciaRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;

@Service
public class GastoService {

    private final GastoRepository gastoRepository;
    private final DetalleGastoUnidadRepository detalleGastoUnidadRepository;
    private final IncidenciaRepository incidenciaRepository;
    private final CondominioRepository condominioRepository;

    public GastoService(GastoRepository gastoRepository,
            DetalleGastoUnidadRepository detalleGastoUnidadRepository,
            IncidenciaRepository incidenciaRepository,
            CondominioRepository condominioRepository) {
        this.gastoRepository = gastoRepository;
        this.detalleGastoUnidadRepository = detalleGastoUnidadRepository;
        this.incidenciaRepository = incidenciaRepository;
        this.condominioRepository = condominioRepository;
    }

    @Transactional
    public synchronized GastoResponse registrarGasto(GastoForm formulario) {
        validarGasto(formulario);

        Gasto gasto = new Gasto();
        gasto.setDescripcion(formulario.getDescripcion().trim());
        gasto.setTipoGasto(formulario.getTipoGasto());
        gasto.setMetodoDistribucion(formulario.getMetodoDistribucion());
        gasto.setMontoTotal(formulario.getMontoTotal());
        gasto.setFechaLimite(formulario.getFechaLimite());

        Condominio condominio = condominioRepository.findById(formulario.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("El condominio no existe."));
        gasto.setCondominio(condominio);
        gasto.getTorres().clear();
        if (formulario.getTorre() != null && !formulario.getTorre().isBlank()) {
            gasto.getTorres().add(formulario.getTorre().trim());
        }

        if (formulario.getIncidenciaId() != null) {
            Incidencia incidencia = incidenciaRepository.findById(formulario.getIncidenciaId())
                    .orElseThrow(() -> new IllegalArgumentException("La incidencia no existe."));
            gasto.setIncidencia(incidencia);
        }

        Gasto guardado = gastoRepository.save(gasto);
        return convertirGastoResponse(guardado);
    }

    @Transactional(readOnly = true)
    public synchronized GastoForm obtenerGastoParaEdicion(Long id) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El gasto no existe."));
        GastoForm formulario = new GastoForm();
        formulario.setId(gasto.getId());
        formulario.setDescripcion(gasto.getDescripcion());
        formulario.setTipoGasto(gasto.getTipoGasto());
        formulario.setMetodoDistribucion(gasto.getMetodoDistribucion());
        formulario.setMontoTotal(gasto.getMontoTotal());
        formulario.setFechaLimite(gasto.getFechaLimite());
        if (gasto.getIncidencia() != null) {
            formulario.setIncidenciaId(gasto.getIncidencia().getId());
        }
        if (gasto.getCondominio() != null) {
            formulario.setCondominioId(gasto.getCondominio().getId());
        }
        if (!gasto.getTorres().isEmpty()) {
            formulario.setTorre(gasto.getTorres().get(0));
        }
        return formulario;
    }

    @Transactional
    public synchronized GastoResponse actualizarGasto(Long id, GastoForm formulario) {
        validarGasto(formulario);

        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El gasto no existe."));

        gasto.setDescripcion(formulario.getDescripcion().trim());
        gasto.setTipoGasto(formulario.getTipoGasto());
        gasto.setMetodoDistribucion(formulario.getMetodoDistribucion());
        gasto.setMontoTotal(formulario.getMontoTotal());
        gasto.setFechaLimite(formulario.getFechaLimite());

        Condominio condominio = condominioRepository.findById(formulario.getCondominioId())
                .orElseThrow(() -> new IllegalArgumentException("El condominio no existe."));
        gasto.setCondominio(condominio);
        gasto.getTorres().clear();
        if (formulario.getTorre() != null && !formulario.getTorre().isBlank()) {
            gasto.getTorres().add(formulario.getTorre().trim());
        }

        if (formulario.getIncidenciaId() != null) {
            Incidencia incidencia = incidenciaRepository.findById(formulario.getIncidenciaId())
                    .orElseThrow(() -> new IllegalArgumentException("La incidencia no existe."));
            gasto.setIncidencia(incidencia);
        } else {
            gasto.setIncidencia(null);
        }

        List<DetalleGastoUnidad> detalles = detalleGastoUnidadRepository.listarPorGasto(id);
        if (!detalles.isEmpty()) {
            detalleGastoUnidadRepository.deleteAll(detalles);
        }

        Gasto guardado = gastoRepository.save(gasto);
        return convertirGastoResponse(guardado);
    }

    @Transactional(readOnly = true)
    public synchronized Page<GastoResponse> listarGastosPorTipo(TipoGasto tipo, Pageable pageable) {
        if (tipo == null) {
            return gastoRepository.findAll(pageable)
                    .map(this::convertirGastoResponse);
        }
        return gastoRepository.listarPorTipo(tipo, pageable)
                .map(this::convertirGastoResponse);
    }

    @Transactional
    public synchronized void eliminarGasto(Long gastoId) {
        Gasto gasto = gastoRepository.findById(gastoId)
                .orElseThrow(() -> new IllegalArgumentException("El gasto no existe."));
        List<DetalleGastoUnidad> detalles = detalleGastoUnidadRepository.listarPorGasto(gastoId);
        detalleGastoUnidadRepository.deleteAll(detalles);
        gastoRepository.delete(gasto);
    }

    public synchronized List<DetalleGastoUnidadResponse> listarDetallesPorGasto(Long gastoId) {
        if (gastoId == null) {
            throw new IllegalArgumentException("Debe seleccionar un gasto valido.");
        }
        return detalleGastoUnidadRepository.listarPorGasto(gastoId).stream()
                .map(this::convertirDetalleResponse)
                .collect(Collectors.toList());
    }

    public synchronized List<DetalleGastoUnidadResponse> listarDetallesPorUnidad(Long unidadId) {
        if (unidadId == null) {
            throw new IllegalArgumentException("Debe seleccionar una unidad valida.");
        }
        return detalleGastoUnidadRepository.listarPorUnidad(unidadId).stream()
                .map(this::convertirDetalleResponse)
                .collect(Collectors.toList());
    }

    private void validarGasto(GastoForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario del gasto es obligatorio.");
        }
        if (formulario.getDescripcion() == null || formulario.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        if (formulario.getTipoGasto() == null) {
            throw new IllegalArgumentException("El tipo de gasto es obligatorio.");
        }
        if (formulario.getMetodoDistribucion() == null) {
            throw new IllegalArgumentException("El metodo de distribucion es obligatorio.");
        }
        if (formulario.getMontoTotal() <= 0) {
            throw new IllegalArgumentException("El monto total debe ser mayor a cero.");
        }

        if (formulario.getTipoGasto() == TipoGasto.EXTRAORDINARIO && formulario.getIncidenciaId() == null) {
            throw new IllegalArgumentException("Debe asociar la incidencia al gasto extraordinario.");
        }

        if (formulario.getTipoGasto() == TipoGasto.FIJO && formulario.getIncidenciaId() != null) {
            throw new IllegalArgumentException("El gasto fijo no debe asociarse a incidencias.");
        }
    }

    private GastoResponse convertirGastoResponse(Gasto gasto) {
        GastoResponse respuesta = new GastoResponse(gasto.getId(), gasto.getDescripcion(), gasto.getTipoGasto(),
                gasto.getMetodoDistribucion(),
                gasto.getIncidencia() != null ? gasto.getIncidencia().getId() : null,
                gasto.getMontoTotal(),
                gasto.getFechaRegistro(),
                gasto.getFechaLimite(),
                gasto.getCondominio() != null ? gasto.getCondominio().getId() : null,
                gasto.getCondominio() != null ? gasto.getCondominio().getNombre() : null,
                gasto.getTorres().isEmpty() ? null : gasto.getTorres().get(0));

        if (gasto.getIncidencia() != null) {
            Incidencia incidenciaReal = (Incidencia) Hibernate.unproxy(gasto.getIncidencia());
            if (incidenciaReal instanceof IncidenciaUnidad) {
                IncidenciaUnidad incidenciaUnidadLocal = (IncidenciaUnidad) incidenciaReal;
                Unidad u = incidenciaUnidadLocal.getUnidad();
                respuesta.setUnidadIdCausante(u.getId());
                respuesta.setNombreUnidadCausante(
                        "Unidad N° " + u.getNumeroUnidad() + (u.getTorre() != null ? " (" + u.getTorre() + ")" : ""));
            }
        }

        List<DetalleGastoUnidad> detalles = detalleGastoUnidadRepository.listarPorGasto(gasto.getId());
        boolean distribuido = !detalles.isEmpty();
        respuesta.setDistribuido(distribuido);

        if (gasto.getMetodoDistribucion() == MetodoDistribucion.COBRO_DIRECTO && distribuido) {
            Unidad u = detalles.get(0).getUnidad();
            respuesta.setUnidadIdCausante(u.getId());
            respuesta.setNombreUnidadCausante(
                    "Unidad N° " + u.getNumeroUnidad() + (u.getTorre() != null ? " (" + u.getTorre() + ")" : ""));
        }

        return respuesta;
    }

    private DetalleGastoUnidadResponse convertirDetalleResponse(DetalleGastoUnidad detalle) {
        return new DetalleGastoUnidadResponse(detalle.getId(),
                detalle.getGasto() != null ? detalle.getGasto().getId() : null,
                detalle.getUnidad() != null ? detalle.getUnidad().getId() : null,
                detalle.getGasto() != null ? detalle.getGasto().getDescripcion() : "Desconocido",
                detalle.getGasto() != null ? detalle.getGasto().getTipoGasto().name() : "-",
                detalle.getMontoAsignado(),
                detalle.getFechaRegistro(),
                detalle.getGasto() != null ? detalle.getGasto().getFechaLimite() : null);
    }
}
