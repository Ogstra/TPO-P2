package ar.edu.uade.logistica.tda;

import org.junit.Test;

import static org.junit.Assert.*;

public class GrafoDepositosTest {

    @Test
    public void distanciaMinimaEntreDosConectados() {
        GrafoDepositos grafo = new GrafoDepositos();
        grafo.agregarArista(50, 20);
        grafo.agregarArista(20, 10);

        assertEquals(1, grafo.distanciaMinima(50, 20));
        assertEquals(2, grafo.distanciaMinima(50, 10));
    }

    @Test
    public void distanciaMinimaMismoNodoEsCero() {
        GrafoDepositos grafo = new GrafoDepositos();
        grafo.agregarVertice(50);

        assertEquals(0, grafo.distanciaMinima(50, 50));
    }

    @Test
    public void distanciaMinimaNoHayCaminoRetornaMenosUno() {
        GrafoDepositos grafo = new GrafoDepositos();
        grafo.agregarVertice(50);
        grafo.agregarVertice(99);  // sin conexion con 50

        assertEquals(-1, grafo.distanciaMinima(50, 99));
    }

    @Test
    public void distanciaMinimaVerticeInexistenteRetornaMenosUno() {
        GrafoDepositos grafo = new GrafoDepositos();
        grafo.agregarVertice(50);

        assertEquals(-1, grafo.distanciaMinima(50, 999));
    }

    @Test
    public void bfsEncuentraCaminoMasCortoConMultiplesCaminos() {
        GrafoDepositos grafo = new GrafoDepositos();
        // camino corto: 1 -> 3 (1 salto)
        // camino largo: 1 -> 2 -> 3 (2 saltos)
        grafo.agregarArista(1, 2);
        grafo.agregarArista(2, 3);
        grafo.agregarArista(1, 3);

        assertEquals(1, grafo.distanciaMinima(1, 3));
    }

    @Test
    public void grafoNoDirigidoBidireccional() {
        GrafoDepositos grafo = new GrafoDepositos();
        grafo.agregarArista(50, 20);

        assertEquals(1, grafo.distanciaMinima(50, 20));
        assertEquals(1, grafo.distanciaMinima(20, 50));  // bidireccional
    }
}
