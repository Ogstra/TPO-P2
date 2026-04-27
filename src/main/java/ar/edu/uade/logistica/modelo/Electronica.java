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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isAsegurado() {
        return asegurado;
    }

    public void setAsegurado(boolean asegurado) {
        this.asegurado = asegurado;
    }

    @Override
    public String toString() {
        return "Electronica [descripcion=" + descripcion + ", asegurado=" + asegurado + "]";
    }
}
