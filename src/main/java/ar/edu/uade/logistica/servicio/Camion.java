package ar.edu.uade.logistica.servicio;

import ar.edu.uade.logistica.modelo.Paquete;
import ar.edu.uade.logistica.tda.Pila;

import java.util.ArrayList;

// Camion modelado como una pila (LIFO).
// El ultimo paquete cargado es el primero en descargarse,
// lo que representa el apilamiento fisico de cajas en el vehiculo.
// deshacerUltimaCarga permite revertir la ultima carga y devolver el paquete al centro.
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
    // Alias semantico de descargar: deshace la ultima carga sin cambiar el comportamiento.
    // El llamador es responsable de devolver el paquete al centro si corresponde.
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

    // O(n)
    // Devuelve todos los paquetes en orden LIFO: el primero de la lista es el
    // que se descargaría primero (ultimo en haber sido cargado).
    public ArrayList<Paquete<T>> listarCarga() {
        return carga.verTodos();
    }
}
