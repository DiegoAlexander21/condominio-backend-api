package pe.edu.utp.condominio.api.dominios.incidencias.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pe.edu.utp.condominio.api.dominios.areascomunes.models.AreaComun;

@Entity
@Table(name = "incidencias_areas_comunes")
public class IncidenciaAreaComun extends Incidencia {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_comun_id", nullable = false)
    private AreaComun areaComun;

    public IncidenciaAreaComun() {
    }

    public AreaComun getAreaComun() {
        return areaComun;
    }

    public void setAreaComun(AreaComun areaComun) {
        this.areaComun = areaComun;
    }
}
