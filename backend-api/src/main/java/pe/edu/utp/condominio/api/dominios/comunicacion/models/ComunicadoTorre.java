package pe.edu.utp.condominio.api.dominios.comunicacion.models;

import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ComunicadoTorre {

    @Column(name = "condominio_id", nullable = false)
    private Long condominioId;

    @Column(name = "torre", nullable = false, length = 20)
    private String torre;

    public ComunicadoTorre() {
    }

    public ComunicadoTorre(Long condominioId, String torre) {
        this.condominioId = condominioId;
        this.torre = torre;
    }

    public Long getCondominioId() {
        return condominioId;
    }

    public void setCondominioId(Long condominioId) {
        this.condominioId = condominioId;
    }

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComunicadoTorre that = (ComunicadoTorre) o;
        return Objects.equals(condominioId, that.condominioId) &&
               Objects.equals(torre, that.torre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condominioId, torre);
    }
}
