package pe.edu.utp.condominio.api.dominios.finanzas.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.utp.condominio.api.dominios.finanzas.dto.request.DistribucionGastoForm;
import pe.edu.utp.condominio.api.dominios.finanzas.dto.response.DetalleGastoUnidadResponse;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.MetodoDistribucion;
import pe.edu.utp.condominio.api.dominios.finanzas.models.DetalleGastoUnidad;
import pe.edu.utp.condominio.api.dominios.finanzas.models.Gasto;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.DetalleGastoUnidadRepository;
import pe.edu.utp.condominio.api.dominios.finanzas.repositories.GastoRepository;
import pe.edu.utp.condominio.api.dominios.unidades.models.Unidad;
import pe.edu.utp.condominio.api.dominios.unidades.repositories.UnidadRepository;

@Service
public class DistribucionGastoService {

    private final GastoRepository gastoRepository;
    private final DetalleGastoUnidadRepository detalleGastoUnidadRepository;
    private final UnidadRepository unidadRepository;

    public DistribucionGastoService(GastoRepository gastoRepository,
            DetalleGastoUnidadRepository detalleGastoUnidadRepository,
            UnidadRepository unidadRepository) {
        this.gastoRepository = gastoRepository;
        this.detalleGastoUnidadRepository = detalleGastoUnidadRepository;
        this.unidadRepository = unidadRepository;
    }

    @Transactional
    public synchronized List<DetalleGastoUnidadResponse> distribuirGasto(DistribucionGastoForm formulario) {
        validarDistribucion(formulario);

        Gasto gasto = gastoRepository.findById(formulario.getGastoId())
                .orElseThrow(() -> new IllegalArgumentException("El gasto no existe."));

        if (!detalleGastoUnidadRepository.listarPorGasto(gasto.getId()).isEmpty()) {
            throw new IllegalArgumentException("El gasto ya tiene distribucion registrada.");
        }

        List<DetalleGastoUnidad> detalles = generarDetalles(gasto, formulario.getUnidadId());
        List<DetalleGastoUnidad> guardados = detalleGastoUnidadRepository.saveAll(detalles);

        return guardados.stream()
                .map(this::convertirDetalleResponse)
                .collect(Collectors.toList());
    }

    private List<DetalleGastoUnidad> generarDetalles(Gasto gasto, Long unidadId) {
        if (gasto.getCondominio() == null) {
            throw new IllegalArgumentException("El gasto debe estar asociado a un condominio.");
        }

        List<Unidad> unidades;
        if (!gasto.getTorres().isEmpty()) {
            unidades = unidadRepository.listarPorCondominioYTorre(gasto.getCondominio().getId(),
                    gasto.getTorres().get(0));
            if (unidades.isEmpty()) {
                throw new IllegalArgumentException(
                        "No existen unidades registradas en la torre especificada para este condominio.");
            }
        } else {
            unidades = unidadRepository.listarPorCondominio(gasto.getCondominio().getId());
            if (unidades.isEmpty()) {
                throw new IllegalArgumentException("No existen unidades registradas en este condominio.");
            }
        }

        if (gasto.getMetodoDistribucion() == MetodoDistribucion.COBRO_DIRECTO) {
            if (unidadId == null) {
                throw new IllegalArgumentException("Debe seleccionar la unidad para cobro directo.");
            }
            Unidad unidad = unidadRepository.findById(unidadId)
                    .orElseThrow(() -> new IllegalArgumentException("La unidad no existe."));
            return List.of(crearDetalle(gasto, unidad, gasto.getMontoTotal()));
        }

        if (gasto.getMetodoDistribucion() == MetodoDistribucion.PARTES_IGUALES) {
            double monto = gasto.getMontoTotal() / unidades.size();
            monto = Math.round(monto * 100.0) / 100.0;
            List<DetalleGastoUnidad> detalles = new ArrayList<>();
            for (Unidad unidad : unidades) {
                detalles.add(crearDetalle(gasto, unidad, monto));
            }
            return detalles;
        }

        double totalArea = unidades.stream().mapToDouble(Unidad::getArea).sum();
        if (totalArea <= 0) {
            throw new IllegalArgumentException("El area total de las unidades es invalida.");
        }

        List<DetalleGastoUnidad> detalles = new ArrayList<>();
        for (Unidad unidad : unidades) {
            double proporcion = unidad.getArea() / totalArea;
            double monto = gasto.getMontoTotal() * proporcion;
            monto = Math.round(monto * 100.0) / 100.0;
            detalles.add(crearDetalle(gasto, unidad, monto));
        }

        return detalles;
    }

    private DetalleGastoUnidad crearDetalle(Gasto gasto, Unidad unidad, double montoAsignado) {
        DetalleGastoUnidad detalle = new DetalleGastoUnidad();
        detalle.setGasto(gasto);
        detalle.setUnidad(unidad);
        detalle.setMontoAsignado(montoAsignado);
        return detalle;
    }

    private void validarDistribucion(DistribucionGastoForm formulario) {
        if (formulario == null) {
            throw new IllegalArgumentException("El formulario de distribucion es obligatorio.");
        }
        if (formulario.getGastoId() == null) {
            throw new IllegalArgumentException("El gasto es obligatorio.");
        }
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
