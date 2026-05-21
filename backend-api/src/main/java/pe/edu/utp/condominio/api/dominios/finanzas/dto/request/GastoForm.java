package pe.edu.utp.condominio.api.dominios.finanzas.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.MetodoDistribucion;
import pe.edu.utp.condominio.api.dominios.finanzas.enums.TipoGasto;

public class GastoForm {

    @NotBlank(message = "La descripcion del gasto es obligatoria.")
    @Size(min = 5, max = 300, message = "La descripcion debe tener entre 5 y 300 caracteres.")
    private String descripcion;

    @NotNull(message = "El tipo de gasto es obligatorio.")
    private TipoGasto tipoGasto;

    @NotNull(message = "El metodo de distribucion es obligatorio.")
    private MetodoDistribucion metodoDistribucion;

    @Min(value = 1, message = "El monto total debe ser mayor a cero.")
    private double montoTotal;

    private Long incidenciaId;

    public GastoForm() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoGasto getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(TipoGasto tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public MetodoDistribucion getMetodoDistribucion() {
        return metodoDistribucion;
    }

    public void setMetodoDistribucion(MetodoDistribucion metodoDistribucion) {
        this.metodoDistribucion = metodoDistribucion;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Long getIncidenciaId() {
        return incidenciaId;
    }

    public void setIncidenciaId(Long incidenciaId) {
        this.incidenciaId = incidenciaId;
    }
}
