package pe.edu.utp.condominio.api.dominios.reportes.dto.response;

import java.time.LocalDate;

public class UnidadMorosaResponse {

    private Long unidadId;
    private String unidadNombre;
    private double saldo;
    private LocalDate periodo;

    public UnidadMorosaResponse() {
    }

    public UnidadMorosaResponse(Long unidadId, String unidadNombre, double saldo, LocalDate periodo) {
        this.unidadId = unidadId;
        this.unidadNombre = unidadNombre;
        this.saldo = saldo;
        this.periodo = periodo;
    }

    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public String getUnidadNombre() {
        return unidadNombre;
    }

    public void setUnidadNombre(String unidadNombre) {
        this.unidadNombre = unidadNombre;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public LocalDate getPeriodo() {
        return periodo;
    }

    public void setPeriodo(LocalDate periodo) {
        this.periodo = periodo;
    }
}
