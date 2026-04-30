package ar.edu.uade.logistica.tda;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Cola<T> {
    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int cantidad;

    // O(1)
    public void encolar(T valor) {
        Nodo<T> nuevo = new Nodo<>(valor);
        if (estaVacia()) {
            primero = nuevo;
        } else {
            ultimo.siguiente = nuevo;
        }
        ultimo = nuevo;
        cantidad++;
    }

    // O(1)
    public T desencolar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola esta vacia");
        }
        T valor = primero.valor;
        primero = primero.siguiente;
        if (primero == null) {
            ultimo = null;
        }
        cantidad--;
        return valor;
    }

    // O(1)
    public T verPrimero() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola esta vacia");
        }
        return primero.valor;
    }

    // O(1)
    public boolean estaVacia() {
        return primero == null;
    }

    // O(1)
    public int tamanio() {
        return cantidad;
    }

    // O(n)
    public ArrayList<T> verPrimeros(int limite) {
        ArrayList<T> valores = new ArrayList<>();
        Nodo<T> actual = primero;

        while (actual != null && valores.size() < limite) {
            valores.add(actual.valor);
            actual = actual.siguiente;
        }

        return valores;
    }

    private static class Nodo<T> {
        private T valor;
        private Nodo<T> siguiente;

        private Nodo(T valor) {
            this.valor = valor;
        }
    }
}