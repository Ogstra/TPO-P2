package ar.edu.uade.logistica.tda;

import java.util.NoSuchElementException;

public class Pila<T> {
    private Nodo<T> cima;
    private int cantidad;

    // O(1)
    public void apilar(T valor) {
        cima = new Nodo<>(valor, cima);
        cantidad++;
    }

    // O(1)
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

    private static class Nodo<T> {
        private T valor;
        private Nodo<T> siguiente;

        private Nodo(T valor, Nodo<T> siguiente) {
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }
}
