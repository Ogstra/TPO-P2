package ar.edu.uade.logistica.servicio;

import ar.edu.uade.logistica.modelo.Paquete;
import ar.edu.uade.logistica.tda.Pila;

public class Camion<T> {
    private String patente;
    private Pila<Paquete<T>> carga = new Pila<>();

    public Camion(String patente) {
        if (patente == null || patente.isBlank()) {
            throw new IllegalArgumentException("La patente es obligatoria");
        }
        this.patente = patente;
    }

    // O(1)
    public void cargar(Paquete<T> paquete) {
        carga.apilar(paquete);
    }

    // O(1)
    public Paquete<T> descargar() {
        return carga.desapilar();
    }

    // O(1)
    public Paquete<T> deshacerUltimaCarga() {
        return descargar();
    }

    // O(1)
    public Paquete<T> verProximaDescarga() {
        return carga.verCima();
    }

    // O(1)
    public String getPatente() {
        return patente;
    }

    // O(1)
    public boolean estaVacio() {
        return carga.estaVacia();
    }

    // O(1)
    public int cantidadPaquetes() {
        return carga.tamanio();
    }
}
