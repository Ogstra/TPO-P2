package ar.edu.uade.logistica.tda;

import java.util.NoSuchElementException;

public class Pila<T> {
    private Nodo<T> cima;
    private int cantidad;

    public void apilar(T valor) {
        cima = new Nodo<>(valor, cima);
        cantidad++;
    }

    public T desapilar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La pila esta vacia");
        }
        T valor = cima.valor;
        cima = cima.siguiente;
        cantidad--;
        return valor;
    }

    public T verCima() {
        if (estaVacia()) {
            throw new NoSuchElementException("La pila esta vacia");
        }
        return cima.valor;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int tamanio() {
        return cantidad;
    }

    private static class Nodo<T> {
        private T valor;
        private Nodo<T> siguiente;

        public Nodo(T valor, Nodo<T> siguiente) {
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }
}
