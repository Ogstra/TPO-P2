package ar.edu.uade.logistica.tda;

import java.util.*;

// Grafo no dirigido con lista de adyacencia para modelar rutas entre depositos
public class GrafoDepositos {
    // Espacio O(V + E)
    private Map<Integer, List<Integer>> adyacencia = new HashMap<>();

    // O(1) promedio
    public void agregarVertice(int id) {
        adyacencia.putIfAbsent(id, new ArrayList<>());
    }

    // O(1) promedio - agrega arista en ambas direcciones (no dirigido)
    public void agregarArista(int origen, int destino) {
        adyacencia.computeIfAbsent(origen, k -> new ArrayList<>()).add(destino);
        adyacencia.computeIfAbsent(destino, k -> new ArrayList<>()).add(origen);
    }

    // O(V + E) - BFS: devuelve distancia minima en saltos entre dos depositos, -1 si no hay camino
    public int distanciaMinima(int origen, int destino) {
        if (!adyacencia.containsKey(origen) || !adyacencia.containsKey(destino)) return -1;
        if (origen == destino) return 0;

        Set<Integer> visitados = new HashSet<>();
        Queue<int[]> cola = new LinkedList<>();  // {id, distancia en saltos}
        cola.add(new int[]{origen, 0});
        visitados.add(origen);

        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            int idActual = actual[0];
            int dist = actual[1];

            for (int vecino : adyacencia.getOrDefault(idActual, Collections.emptyList())) {
                if (vecino == destino) return dist + 1;
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(new int[]{vecino, dist + 1});
                }
            }
        }
        return -1;  // no existe camino
    }

    // O(grado(v)) - vecinos directos de un deposito
    public List<Integer> obtenerVecinos(int id) {
        return adyacencia.getOrDefault(id, Collections.emptyList());
    }

    // O(1) promedio
    public boolean existeVertice(int id) {
        return adyacencia.containsKey(id);
    }

    // O(V) - todos los ids de vertices
    public Set<Integer> getVertices() {
        return adyacencia.keySet();
    }
}
