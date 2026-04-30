package ar.edu.uade.logistica.servicio;

import ar.edu.uade.logistica.modelo.Paquete;
import ar.edu.uade.logistica.tda.ColaPrioridadPaquetes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// Centro de distribucion: punto de entrada de paquetes antes de ser cargados al camion.
// Internamente usa una ColaPrioridadPaquetes para procesar urgentes/pesados primero.
// El HashSet idsRegistrados permite validar unicidad de ID en O(1) promedio,
// sin necesidad de recorrer la cola (que no tiene busqueda directa).
public class CentroDistribucion<T> {
    private ColaPrioridadPaquetes<T> colaProcesamiento = new ColaPrioridadPaquetes<>();
    private Set<String> idsRegistrados = new HashSet<>();

    // O(1) promedio
    // Valida que el paquete no sea nulo y que su ID no este ya registrado,
    // luego lo encola y registra el ID en el HashSet.
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
    // Extrae el siguiente paquete segun prioridad y lo elimina del registro de IDs,
    // liberando ese ID para reutilizacion futura.
    public Paquete<T> procesarSiguiente() {
        Paquete<T> paquete = colaProcesamiento.desencolar();
        idsRegistrados.remove(paquete.getId());
        return paquete;
    }

    // O(1)
    // Consulta sin extraer el siguiente en orden de prioridad.
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
