package ar.edu.uade.logistica.tda;

import ar.edu.uade.logistica.modelo.Paquete;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ColaPrioridadPaquetes<T> {
    private Cola<Paquete<T>> prioritarios = new Cola<>();
    private Cola<Paquete<T>> estandar = new Cola<>();

    // O(1)
    public void encolar(Paquete<T> paquete) {
        if (paquete.requierePrioridad()) {
            prioritarios.encolar(paquete);
        } else {
            estandar.encolar(paquete);
        }
    }

    // O(1)
    public Paquete<T> desencolar() {
        if (!prioritarios.estaVacia()) {
            return prioritarios.desencolar();
        }
        if (!estandar.estaVacia()) {
            return estandar.desencolar();
        }
        throw new NoSuchElementException("No hay paquetes para procesar");
    }

    // O(1)
    public Paquete<T> verSiguiente() {
        if (!prioritarios.estaVacia()) {
            return prioritarios.verPrimero();
        }
        if (!estandar.estaVacia()) {
            return estandar.verPrimero();
        }
        throw new NoSuchElementException("No hay paquetes para procesar");
    }

    // O(1)
    public boolean estaVacia() {
        return prioritarios.estaVacia() && estandar.estaVacia();
    }

    // O(1)
    public int tamanio() {
        return prioritarios.tamanio() + estandar.tamanio();
    }

    // O(n)
    public ArrayList<Paquete<T>> verProximos(int limite) {
        ArrayList<Paquete<T>> proximos = prioritarios.verPrimeros(limite);
        if (proximos.size() < limite) {
            proximos.addAll(estandar.verPrimeros(limite - proximos.size()));
        }
        return proximos;
    }
}
