package pe.edu.utp.condominio.api.dominios.mantenimiento.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InsumoForm {

    @NotBlank(message = "El nombre del insumo es obligatorio.")
    @Size(max = 150, message = "El nombre no puede exceder los 150 caracteres.")
    private String nombre;

    @NotBlank(message = "La unidad de medida es obligatoria.")
    @Size(max = 30, message = "La unidad de medida no puede exceder los 30 caracteres.")
    private String unidadMedida;

    @NotNull(message = "El stock inicial es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Double stockActual;

    @NotNull(message = "El stock mínimo es obligatorio.")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo.")
    private Double stockMinimo;

    @NotNull(message = "El precio unitario es obligatorio.")
    @Min(value = 0, message = "El precio no puede ser negativo.")
    private Double precioUnitario;

    public InsumoForm() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public Double getStockActual() { return stockActual; }
    public void setStockActual(Double stockActual) { this.stockActual = stockActual; }
    public Double getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Double stockMinimo) { this.stockMinimo = stockMinimo; }
    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
}
