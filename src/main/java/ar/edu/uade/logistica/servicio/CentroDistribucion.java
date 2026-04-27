package ar.edu.uade.logistica.servicio;

import ar.edu.uade.logistica.modelo.Paquete;
import ar.edu.uade.logistica.tda.ColaPrioridadPaquetes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CentroDistribucion<T> {
    private ColaPrioridadPaquetes<T> colaProcesamiento = new ColaPrioridadPaquetes<>();
    private Set<String> idsRegistrados = new HashSet<>();

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

    public Paquete<T> procesarSiguiente() {
        Paquete<T> paquete = colaProcesamiento.desencolar();
        idsRegistrados.remove(paquete.getId());
        return paquete;
    }

    public Paquete<T> verSiguiente() {
        return colaProcesamiento.verSiguiente();
    }

    public boolean estaVacio() {
        return colaProcesamiento.estaVacia();
    }

    public int cantidadPendiente() {
        return colaProcesamiento.tamanio();
    }

    public ArrayList<Paquete<T>> verProximos(int limite) {
        return colaProcesamiento.verProximos(limite);
    }
}
