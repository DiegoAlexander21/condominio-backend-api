package pe.edu.utp.condominio.api.compartido.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public class RespuestaPaginada<T> {
    private List<T> contenido;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;
    private boolean ultimaPagina;

    public RespuestaPaginada(Page<T> pagina) {
        this.contenido = pagina.getContent();
        this.paginaActual = pagina.getNumber();
        this.totalPaginas = pagina.getTotalPages();
        this.totalElementos = pagina.getTotalElements();
        this.ultimaPagina = pagina.isLast();
    }

    public List<T> getContenido() { return contenido; }
    public void setContenido(List<T> contenido) { this.contenido = contenido; }
    
    public int getPaginaActual() { return paginaActual; }
    public void setPaginaActual(int paginaActual) { this.paginaActual = paginaActual; }
    
    public int getTotalPaginas() { return totalPaginas; }
    public void setTotalPaginas(int totalPaginas) { this.totalPaginas = totalPaginas; }
    
    public long getTotalElementos() { return totalElementos; }
    public void setTotalElementos(long totalElementos) { this.totalElementos = totalElementos; }
    
    public boolean isUltimaPagina() { return ultimaPagina; }
    public void setUltimaPagina(boolean ultimaPagina) { this.ultimaPagina = ultimaPagina; }
}
