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

    public void cargar(Paquete<T> paquete) {
        carga.apilar(paquete);
    }

    public Paquete<T> descargar() {
        return carga.desapilar();
    }

    public Paquete<T> deshacerUltimaCarga() {
        return descargar();
    }

    public Paquete<T> verProximaDescarga() {
        return carga.verCima();
    }

    public String getPatente() {
        return patente;
    }

    public boolean estaVacio() {
        return carga.estaVacia();
    }

    public int cantidadPaquetes() {
        return carga.tamanio();
    }
}
