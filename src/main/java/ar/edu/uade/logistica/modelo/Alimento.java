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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isRefrigerado() {
        return refrigerado;
    }

    public void setRefrigerado(boolean refrigerado) {
        this.refrigerado = refrigerado;
    }

    @Override
    public String toString() {
        return "Alimento [descripcion=" + descripcion + ", refrigerado=" + refrigerado + "]";
    }
}
