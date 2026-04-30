package ar.edu.uade.logistica.tda;

import java.util.ArrayList;
import java.util.NoSuchElementException;

// Pila generica implementada con lista enlazada simple.
// Comportamiento LIFO: el ultimo en entrar es el primero en salir.
// La cima siempre apunta al elemento mas reciente; apilar y desapilar son O(1)
// porque solo se opera sobre el extremo superior de la cadena.
public class Pila<T> {
    private Nodo<T> cima; // nodo en el tope de la pila (ultimo apilado)
    private int cantidad;

    // O(1)
    // El nuevo nodo apunta al nodo que era cima antes, luego cima se actualiza.
    // Equivale a insertar al inicio de la lista enlazada.
    public void apilar(T valor) {
        cima = new Nodo<>(valor, cima);
        cantidad++;
    }

    // O(1)
    // Guarda el valor de la cima, avanza cima al nodo siguiente y retorna el valor.
    public T desapilar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La pila esta vacia");
        }
        T valor = cima.valor;
        cima = cima.siguiente;
        cantidad--;
        return valor;
    }

    // O(1)
    public T verCima() {
        if (estaVacia()) {
            throw new NoSuchElementException("La pila esta vacia");
        }
        return cima.valor;
    }

    // O(1)
    public boolean estaVacia() {
        return cima == null;
    }

    // O(1)
    public int tamanio() {
        return cantidad;
    }

    // O(n)
    // Recorre desde la cima hacia abajo sin modificar la pila.
    // El primer elemento de la lista es el ultimo apilado (orden LIFO).
    public ArrayList<T> verTodos() {
        ArrayList<T> valores = new ArrayList<>();
        Nodo<T> actual = cima;
        while (actual != null) {
            valores.add(actual.valor);
            actual = actual.siguiente;
        }
        return valores;
    }

    // Nodo interno de la lista enlazada.
    // Cada nodo apunta al nodo que estaba en la cima antes de ser apilado,
    // formando la cadena LIFO.
    private static class Nodo<T> {
        private T valor;
        private Nodo<T> siguiente;

        private Nodo(T valor, Nodo<T> siguiente) {
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }
}
