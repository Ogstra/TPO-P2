package ar.edu.uade.logistica.modelo;

public class Fragil implements Contenido {
    private String descripcion;

    public Fragil() {
    }

    public Fragil(String descripcion) {
        this.descripcion = descripcion;
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
    @Override
    public String toString() {
        return "Fragil [descripcion=" + descripcion + "]";
    }
}
