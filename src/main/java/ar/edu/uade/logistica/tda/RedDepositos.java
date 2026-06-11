package ar.edu.uade.logistica.tda;

import ar.edu.uade.logistica.modelo.Deposito;

import java.time.LocalDateTime;
import java.util.ArrayList;

// ABB manual de depositos, ordenado por id numerico
public class RedDepositos {
    private Nodo raiz;

    // O(log n) promedio, O(n) peor caso
    public void insertar(Deposito deposito) {
        raiz = insertar(raiz, deposito);
    }

    private Nodo insertar(Nodo nodo, Deposito deposito) {
        if (nodo == null) return new Nodo(deposito);
        if (deposito.getId() < nodo.deposito.getId()) {
            nodo.izq = insertar(nodo.izq, deposito);
        } else if (deposito.getId() > nodo.deposito.getId()) {
            nodo.der = insertar(nodo.der, deposito);
        }
        return nodo;
    }

    // O(log n) promedio, O(n) peor caso
    public Deposito buscar(int id) {
        Nodo nodo = buscar(raiz, id);
        return nodo != null ? nodo.deposito : null;
    }

    private Nodo buscar(Nodo nodo, int id) {
        if (nodo == null) return null;
        if (id == nodo.deposito.getId()) return nodo;
        if (id < nodo.deposito.getId()) return buscar(nodo.izq, id);
        return buscar(nodo.der, id);
    }

    // O(n) - recorre en post-orden y marca visitado=true si no auditado en ultimos 30 dias
    public void auditoria() {
        auditoria(raiz);
    }

    private void auditoria(Nodo nodo) {
        if (nodo == null) return;
        auditoria(nodo.izq);  // izquierdo
        auditoria(nodo.der);  // derecho
        // post-orden: procesa el nodo actual despues de sus hijos
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        if (nodo.deposito.getFechaUltimaAuditoria() == null ||
                nodo.deposito.getFechaUltimaAuditoria().isBefore(hace30Dias)) {
            nodo.deposito.setVisitado(true);
        }
    }

    // O(n) - devuelve depositos en el nivel N (raiz = nivel 0)
    public ArrayList<Deposito> depositosEnNivel(int nivel) {
        ArrayList<Deposito> resultado = new ArrayList<>();
        depositosEnNivel(raiz, nivel, 0, resultado);
        return resultado;
    }

    private void depositosEnNivel(Nodo nodo, int nivelBuscado, int nivelActual, ArrayList<Deposito> resultado) {
        if (nodo == null) return;
        if (nivelActual == nivelBuscado) {
            resultado.add(nodo.deposito);
            return;
        }
        depositosEnNivel(nodo.izq, nivelBuscado, nivelActual + 1, resultado);
        depositosEnNivel(nodo.der, nivelBuscado, nivelActual + 1, resultado);
    }

    // O(1)
    public boolean estaVacio() {
        return raiz == null;
    }

    // O(n) - devuelve todos los depositos en orden (in-order) para persistencia
    public ArrayList<Deposito> getTodos() {
        ArrayList<Deposito> lista = new ArrayList<>();
        inOrder(raiz, lista);
        return lista;
    }

    private void inOrder(Nodo nodo, ArrayList<Deposito> lista) {
        if (nodo == null) return;
        inOrder(nodo.izq, lista);
        lista.add(nodo.deposito);
        inOrder(nodo.der, lista);
    }

    private static class Nodo {
        Deposito deposito;
        Nodo izq;
        Nodo der;

        Nodo(Deposito deposito) {
            this.deposito = deposito;
        }
    }
}
