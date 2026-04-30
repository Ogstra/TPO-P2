package ar.edu.uade.logistica.tda;

import java.util.ArrayList;
import java.util.NoSuchElementException;

// Cola generica implementada con lista enlazada simple.
// Comportamiento FIFO: el primero en entrar es el primero en salir.
// Se mantienen referencias al primer y ultimo nodo para que encolar y desencolar
// sean O(1) sin necesidad de recorrer la lista.
public class Cola<T> {
    private Nodo<T> primero; // nodo que se desencola primero
    private Nodo<T> ultimo;  // nodo donde se encola el siguiente elemento
    private int cantidad;

    // O(1)
    // Agrega el nuevo nodo al final de la cadena.
    // Si la cola esta vacia, primero y ultimo apuntan al mismo nodo.
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
    // Extrae el nodo del frente y avanza primero al siguiente.
    // Si queda vacia, ultimo tambien se limpia para evitar referencias huerfanas.
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
    // Recorre la lista desde el frente hasta alcanzar el limite o el final.
    // No modifica la cola.
    public ArrayList<T> verPrimeros(int limite) {
        ArrayList<T> valores = new ArrayList<>();
        Nodo<T> actual = primero;

        while (actual != null && valores.size() < limite) {
            valores.add(actual.valor);
            actual = actual.siguiente;
        }

        return valores;
    }

    // Nodo interno de la lista enlazada.
    // Solo visible dentro de Cola; cada nodo guarda su valor y el puntero al siguiente.
    private static class Nodo<T> {
        private T valor;
        private Nodo<T> siguiente;

        private Nodo(T valor) {
            this.valor = valor;
        }
    }
}
