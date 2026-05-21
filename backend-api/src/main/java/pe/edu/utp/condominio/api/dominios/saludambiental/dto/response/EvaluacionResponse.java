package pe.edu.utp.condominio.api.dominios.saludambiental.dto.response;

import java.time.LocalDateTime;
import pe.edu.utp.condominio.api.dominios.saludambiental.enums.ResultadoChecklist;

public class EvaluacionResponse {

    private Long id;
    private String nombreChecklist;
    private String nombreArea;
    private LocalDateTime fechaEvaluacion;
    private ResultadoChecklist resultado;
    private String observacion;
    private boolean alertaGenerada;

    public EvaluacionResponse() {}

    public EvaluacionResponse(Long id, String nombreChecklist, String nombreArea, LocalDateTime fechaEvaluacion, ResultadoChecklist resultado, String observacion, boolean alertaGenerada) {
        this.id = id;
        this.nombreChecklist = nombreChecklist;
        this.nombreArea = nombreArea;
        this.fechaEvaluacion = fechaEvaluacion;
        this.resultado = resultado;
        this.observacion = observacion;
        this.alertaGenerada = alertaGenerada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreChecklist() { return nombreChecklist; }
    public void setNombreChecklist(String nombreChecklist) { this.nombreChecklist = nombreChecklist; }
    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String nombreArea) { this.nombreArea = nombreArea; }
    public LocalDateTime getFechaEvaluacion() { return fechaEvaluacion; }
    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) { this.fechaEvaluacion = fechaEvaluacion; }
    public ResultadoChecklist getResultado() { return resultado; }
    public void setResultado(ResultadoChecklist resultado) { this.resultado = resultado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public boolean isAlertaGenerada() { return alertaGenerada; }
    public void setAlertaGenerada(boolean alertaGenerada) { this.alertaGenerada = alertaGenerada; }
}
