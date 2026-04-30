package ar.edu.uade.logistica.modelo;

public class Paquete<T> {
    private String id;
    private double peso;
    private String destino;
    private boolean urgente;
    private T contenido;

    public Paquete(String id, double peso, String destino, boolean urgente, T contenido) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del paquete es obligatorio");
        }
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor a cero");
        }
        if (destino == null || destino.isBlank()) {
            throw new IllegalArgumentException("El destino es obligatorio");
        }
        this.id = id;
        this.peso = peso;
        this.destino = destino;
        this.urgente = urgente;
        this.contenido = contenido;
    }

    // O(1)
    public String getId() {
        return id;
    }

    // O(1)
    public double getPeso() {
        return peso;
    }

    // O(1)
    public String getDestino() {
        return destino;
    }

    // O(1)
    public boolean isUrgente() {
        return urgente;
    }

    // O(1)
    public T getContenido() {
        return contenido;
    }

    // O(1)
    public boolean requierePrioridad() {
        return urgente || peso > 50;
    }

    // O(1)
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paquete)) {
            return false;
        }
        Paquete<?> paquete = (Paquete<?>) o;
        return id.equals(paquete.getId());
    }

    // O(1)
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // O(1)
    @Override
    public String toString() {
        return "Paquete{" +
                "id='" + id + '\'' +
                ", peso=" + peso +
                ", destino='" + destino + '\'' +
                ", urgente=" + urgente +
                ", contenido=" + contenido +
                '}';
    }
}
