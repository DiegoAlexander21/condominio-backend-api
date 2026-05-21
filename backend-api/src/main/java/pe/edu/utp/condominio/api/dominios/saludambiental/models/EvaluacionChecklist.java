package pe.edu.utp.condominio.api.dominios.saludambiental.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import pe.edu.utp.condominio.api.dominios.saludambiental.enums.ResultadoChecklist;

@Entity
@Table(name = "evaluaciones_checklist")
public class EvaluacionChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id", nullable = false)
    private ChecklistSaludAmbiente checklist;

    @Column(nullable = false)
    private LocalDateTime fechaEvaluacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ResultadoChecklist resultado;

    @Column(length = 500)
    private String observacion;

    @Column(nullable = false)
    private boolean alertaGenerada;

    public EvaluacionChecklist() {
    }

    @PrePersist
    public void prepararRegistro() {
        fechaEvaluacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChecklistSaludAmbiente getChecklist() {
        return checklist;
    }

    public void setChecklist(ChecklistSaludAmbiente checklist) {
        this.checklist = checklist;
    }

    public LocalDateTime getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public ResultadoChecklist getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoChecklist resultado) {
        this.resultado = resultado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean isAlertaGenerada() {
        return alertaGenerada;
    }

    public void setAlertaGenerada(boolean alertaGenerada) {
        this.alertaGenerada = alertaGenerada;
    }
}
