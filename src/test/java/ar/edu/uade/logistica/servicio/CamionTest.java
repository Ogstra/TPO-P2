package ar.edu.uade.logistica.servicio;

import ar.edu.uade.logistica.modelo.Paquete;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CamionTest {
    @Test
    public void descargaUltimoPaqueteCargadoPrimero() {
        Camion<String> camion = new Camion<>("ABC-123");
        Paquete<String> primero = new Paquete<>("P-001", 10, "Cordoba", false, "A");
        Paquete<String> segundo = new Paquete<>("P-002", 20, "Rosario", false, "B");

        camion.cargar(primero);
        camion.cargar(segundo);

        assertEquals(segundo, camion.descargar());
        assertEquals(primero, camion.descargar());
    }

    @Test
    public void deshacerUltimaCargaQuitaElUltimoPaqueteCargado() {
        Camion<String> camion = new Camion<>("ABC-123");
        Paquete<String> primero = new Paquete<>("P-001", 10, "Cordoba", false, "A");
        Paquete<String> segundo = new Paquete<>("P-002", 20, "Rosario", false, "B");

        camion.cargar(primero);
        camion.cargar(segundo);

        assertEquals(segundo, camion.deshacerUltimaCarga());
        assertEquals(1, camion.cantidadPaquetes());
        assertEquals(primero, camion.verProximaDescarga());
    }
}
