package ar.edu.uade.logistica.modelo;

import java.time.LocalDateTime;

public class Deposito {
    private int id;
    private String nombre;
    private boolean visitado;
    private LocalDateTime fechaUltimaAuditoria;

    public Deposito(int id, String nombre, boolean visitado, LocalDateTime fechaUltimaAuditoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del deposito es obligatorio");
        }
        this.id = id;
        this.nombre = nombre;
        this.visitado = visitado;
        this.fechaUltimaAuditoria = fechaUltimaAuditoria;
    }

    // O(1)
    public int getId() { return id; }

    // O(1)
    public String getNombre() { return nombre; }

    // O(1)
    public boolean isVisitado() { return visitado; }

    // O(1)
    public void setVisitado(boolean visitado) { this.visitado = visitado; }

    // O(1)
    public LocalDateTime getFechaUltimaAuditoria() { return fechaUltimaAuditoria; }

    // O(1)
    public void setFechaUltimaAuditoria(LocalDateTime fecha) { this.fechaUltimaAuditoria = fecha; }

    // O(1)
    @Override
    public String toString() {
        return "Deposito{id=" + id + ", nombre='" + nombre + "', visitado=" + visitado +
                ", ultimaAuditoria=" + fechaUltimaAuditoria + "}";
    }
}
