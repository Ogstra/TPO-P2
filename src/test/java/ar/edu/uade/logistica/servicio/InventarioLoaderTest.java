package ar.edu.uade.logistica.servicio;

import ar.edu.uade.logistica.modelo.Alimento;
import ar.edu.uade.logistica.modelo.Contenido;
import ar.edu.uade.logistica.modelo.Electronica;
import ar.edu.uade.logistica.modelo.Fragil;
import ar.edu.uade.logistica.modelo.Paquete;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class InventarioLoaderTest {
    @Test
    public void cargaPaquetesDesdeJson() throws Exception {
        InventarioLoader loader = new InventarioLoader();

        ArrayList<Paquete<Contenido>> paquetes = loader.cargarPaquetes("src/main/resources/inventario.json");
        Paquete<Contenido> p001 = buscarPorId(paquetes, "P-001");
        Paquete<Contenido> p002 = buscarPorId(paquetes, "P-002");
        Paquete<Contenido> p003 = buscarPorId(paquetes, "P-003");

        assertTrue(paquetes.size() >= 3);
        assertSame(Electronica.class, p001.getContenido().getClass());
        assertSame(Alimento.class, p002.getContenido().getClass());
        assertSame(Fragil.class, p003.getContenido().getClass());
        assertTrue(p002.requierePrioridad());
        assertTrue(p003.requierePrioridad());
    }

    @Test
    public void guardaYRecuperaPaquetesDesdeJson() throws Exception {
        InventarioLoader loader = new InventarioLoader();
        ArrayList<Paquete<Contenido>> originales = new ArrayList<>();
        originales.add(new Paquete<>("P-010", 8, "Salta", false, new Electronica("Tablet", true)));
        originales.add(new Paquete<>("P-011", 55, "Neuquen", false, new Alimento("Vacunas", true)));

        Path archivoTemporal = Files.createTempFile("inventario-test", ".json");

        loader.guardarPaquetes(archivoTemporal.toString(), originales);
        ArrayList<Paquete<Contenido>> recuperados = loader.cargarPaquetes(archivoTemporal.toString());

        assertEquals(2, recuperados.size());
        assertSame(Electronica.class, recuperados.get(0).getContenido().getClass());
        assertSame(Alimento.class, recuperados.get(1).getContenido().getClass());
        assertEquals("P-011", recuperados.get(1).getId());
    }

    private Paquete<Contenido> buscarPorId(ArrayList<Paquete<Contenido>> paquetes, String id) {
        for (Paquete<Contenido> paquete : paquetes) {
            if (paquete.getId().equals(id)) {
                return paquete;
            }
        }
        throw new AssertionError("No se encontro el paquete " + id);
    }
}
