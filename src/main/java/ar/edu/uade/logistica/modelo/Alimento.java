package ar.edu.uade.logistica.modelo;

public class Alimento implements Contenido {
    private String descripcion;
    private boolean refrigerado;

    public Alimento() {
    }

    public Alimento(String descripcion, boolean refrigerado) {
        this.descripcion = descripcion;
        this.refrigerado = refrigerado;
    }

    // O(1)
    public String getDescripcion() {
        return descripcion;
    }

    // O(1)
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // O(1)
    public boolean isRefrigerado() {
        return refrigerado;
    }

    // O(1)
    public void setRefrigerado(boolean refrigerado) {
        this.refrigerado = refrigerado;
    }

    // O(1)
    @Override
    public String toString() {
        return "Alimento [descripcion=" + descripcion + ", refrigerado=" + refrigerado + "]";
    }
}
