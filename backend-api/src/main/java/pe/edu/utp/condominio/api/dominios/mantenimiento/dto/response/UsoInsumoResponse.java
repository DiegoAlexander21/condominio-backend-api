package pe.edu.utp.condominio.api.dominios.mantenimiento.dto.response;

public class UsoInsumoResponse {

    private Long id;
    private String nombreInsumo;
    private double cantidadUsada;
    private String unidadMedida;
    private double costoUnitario;
    private double subtotal;

    public UsoInsumoResponse() {}

    public UsoInsumoResponse(Long id, String nombreInsumo, double cantidadUsada, String unidadMedida, double costoUnitario, double subtotal) {
        this.id = id;
        this.nombreInsumo = nombreInsumo;
        this.cantidadUsada = cantidadUsada;
        this.unidadMedida = unidadMedida;
        this.costoUnitario = costoUnitario;
        this.subtotal = subtotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreInsumo() { return nombreInsumo; }
    public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }
    public double getCantidadUsada() { return cantidadUsada; }
    public void setCantidadUsada(double cantidadUsada) { this.cantidadUsada = cantidadUsada; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public double getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(double costoUnitario) { this.costoUnitario = costoUnitario; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
