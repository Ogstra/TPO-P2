package ar.edu.uade.logistica.modelo;

public class Fragil implements Contenido {
    private String descripcion;

    public Fragil() {
    }

    public Fragil(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Fragil [descripcion=" + descripcion + "]";
    }
}
