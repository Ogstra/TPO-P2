package ar.edu.uade.logistica.servicio;

import ar.edu.uade.logistica.modelo.Paquete;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CentroDistribucionTest {
    @Test
    public void procesaUrgentesAntesQueEstandar() {
        CentroDistribucion<String> centro = new CentroDistribucion<>();
        Paquete<String> estandar = new Paquete<>("P-001", 10, "Cordoba", false, "A");
        Paquete<String> urgente = new Paquete<>("P-002", 20, "Rosario", true, "B");

        centro.recibir(estandar);
        centro.recibir(urgente);

        assertEquals(urgente, centro.procesarSiguiente());
        assertEquals(estandar, centro.procesarSiguiente());
    }

    @Test
    public void procesaPesadosAntesQueEstandar() {
        CentroDistribucion<String> centro = new CentroDistribucion<>();
        Paquete<String> estandar = new Paquete<>("P-001", 10, "Cordoba", false, "A");
        Paquete<String> pesado = new Paquete<>("P-002", 70, "Rosario", false, "B");

        centro.recibir(estandar);
        centro.recibir(pesado);

        assertEquals(pesado, centro.procesarSiguiente());
        assertEquals(estandar, centro.procesarSiguiente());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaIdDuplicadoAlRecibir() {
        CentroDistribucion<String> centro = new CentroDistribucion<>();

        centro.recibir(new Paquete<>("P-001", 10, "Cordoba", false, "A"));
        centro.recibir(new Paquete<>("P-001", 20, "Rosario", true, "B"));
    }

    @Test
    public void procesarPaqueteLiberaSuId() {
        CentroDistribucion<String> centro = new CentroDistribucion<>();
        centro.recibir(new Paquete<>("P-001", 10, "Cordoba", false, "A"));

        centro.procesarSiguiente();
        centro.recibir(new Paquete<>("P-001", 20, "Rosario", true, "B"));

        assertFalse(centro.estaVacio());
    }

    @Test
    public void listaLosProximosPaquetesSinConsumirlos() {
        CentroDistribucion<String> centro = new CentroDistribucion<>();
        Paquete<String> estandar = new Paquete<>("P-001", 10, "Cordoba", false, "A");
        Paquete<String> urgente = new Paquete<>("P-002", 20, "Rosario", true, "B");
        Paquete<String> pesado = new Paquete<>("P-003", 70, "Mendoza", false, "C");

        centro.recibir(estandar);
        centro.recibir(urgente);
        centro.recibir(pesado);

        ArrayList<Paquete<String>> proximos = centro.verProximos(10);

        assertEquals(3, proximos.size());
        assertEquals("P-002", proximos.get(0).getId());
        assertEquals("P-003", proximos.get(1).getId());
        assertEquals("P-001", proximos.get(2).getId());
        assertEquals("P-002", centro.procesarSiguiente().getId());
    }
}
