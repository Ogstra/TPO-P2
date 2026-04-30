package ar.edu.uade.logistica.servicio;

import ar.edu.uade.logistica.modelo.Paquete;
import ar.edu.uade.logistica.tda.ColaPrioridadPaquetes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CentroDistribucion<T> {
    private ColaPrioridadPaquetes<T> colaProcesamiento = new ColaPrioridadPaquetes<>();
    private Set<String> idsRegistrados = new HashSet<>();

    // O(1) promedio
    public void recibir(Paquete<T> paquete) {
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete es obligatorio");
        }
        if (idsRegistrados.contains(paquete.getId())) {
            throw new IllegalArgumentException("Ya existe un paquete con ID " + paquete.getId());
        }
        idsRegistrados.add(paquete.getId());
        colaProcesamiento.encolar(paquete);
    }

    // O(1) promedio
    public Paquete<T> procesarSiguiente() {
        Paquete<T> paquete = colaProcesamiento.desencolar();
        idsRegistrados.remove(paquete.getId());
        return paquete;
    }

    // O(1)
    public Paquete<T> verSiguiente() {
        return colaProcesamiento.verSiguiente();
    }

    // O(1)
    public boolean estaVacio() {
        return colaProcesamiento.estaVacia();
    }

    // O(1)
    public int cantidadPendiente() {
        return colaProcesamiento.tamanio();
    }

    // O(n)
    public ArrayList<Paquete<T>> verProximos(int limite) {
        return colaProcesamiento.verProximos(limite);
    }
}
