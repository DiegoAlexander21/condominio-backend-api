package pe.edu.utp.condominio.api.dominios.saludambiental.dto.response;

public class ItemChecklistResponse {

    private Long id;
    private String descripcion;
    private Integer orden;

    public ItemChecklistResponse() {}

    public ItemChecklistResponse(Long id, String descripcion, Integer orden) {
        this.id = id;
        this.descripcion = descripcion;
        this.orden = orden;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
}
