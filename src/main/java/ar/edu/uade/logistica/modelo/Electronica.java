package ar.edu.uade.logistica.modelo;

public class Electronica implements Contenido {
    private String descripcion;
    private boolean asegurado;

    public Electronica() {
    }

    public Electronica(String descripcion, boolean asegurado) {
        this.descripcion = descripcion;
        this.asegurado = asegurado;
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
    public boolean isAsegurado() {
        return asegurado;
    }

    // O(1)
    public void setAsegurado(boolean asegurado) {
        this.asegurado = asegurado;
    }

    // O(1)
    @Override
    public String toString() {
        return "Electronica [descripcion=" + descripcion + ", asegurado=" + asegurado + "]";
    }
}
