package ar.edu.uade.logistica.tda;

import ar.edu.uade.logistica.modelo.Paquete;

import java.util.ArrayList;
import java.util.NoSuchElementException;

// Cola de prioridad para paquetes implementada con dos colas internas.
// En lugar de reordenar elementos, se separan en dos colas al momento de encolar:
//   - prioritarios: paquetes urgentes o de mas de 50 kg
//   - estandar: el resto
// Al desencolar, siempre se agota primero la cola prioritarios.
// Este diseno mantiene todas las operaciones en O(1) (sin heaps ni reordenamientos).
public class ColaPrioridadPaquetes<T> {
    private Cola<Paquete<T>> prioritarios = new Cola<>();
    private Cola<Paquete<T>> estandar = new Cola<>();

    // O(1)
    // La clasificacion ocurre una sola vez al encolar, segun requierePrioridad().
    public void encolar(Paquete<T> paquete) {
        if (paquete.requierePrioridad()) {
            prioritarios.encolar(paquete);
        } else {
            estandar.encolar(paquete);
        }
    }

    // O(1)
    // Siempre prefiere prioritarios; solo va a estandar si prioritarios esta vacia.
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
    // Consulta sin extraer, con la misma logica de preferencia que desencolar.
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
    // Completa el resultado con prioritarios primero; si no alcanzan para el limite,
    // agrega los primeros de estandar hasta completar.
    public ArrayList<Paquete<T>> verProximos(int limite) {
        ArrayList<Paquete<T>> proximos = prioritarios.verPrimeros(limite);
        if (proximos.size() < limite) {
            proximos.addAll(estandar.verPrimeros(limite - proximos.size()));
        }
        return proximos;
    }
}
