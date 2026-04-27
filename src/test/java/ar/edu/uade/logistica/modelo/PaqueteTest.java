package ar.edu.uade.logistica.modelo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PaqueteTest {
    @Test
    public void paqueteUrgenteRequierePrioridad() {
        Paquete<String> paquete = new Paquete<>("P-001", 10, "Cordoba", true, "Documento");

        assertTrue(paquete.requierePrioridad());
    }

    @Test
    public void paquetePesadoRequierePrioridad() {
        Paquete<String> paquete = new Paquete<>("P-002", 51, "Rosario", false, "Caja");

        assertTrue(paquete.requierePrioridad());
    }

    @Test
    public void paqueteEstandarNoRequierePrioridad() {
        Paquete<String> paquete = new Paquete<>("P-003", 20, "Mendoza", false, "Libro");

        assertFalse(paquete.requierePrioridad());
    }

    @Test
    public void permiteContenidoGenerico() {
        Paquete<Electronica> paquete = new Paquete<>(
                "P-004",
                2,
                "San Juan",
                false,
                new Electronica("Camara", true)
        );

        assertEquals("Camara", paquete.getContenido().getDescripcion());
    }

    @Test
    public void permiteDistintosTiposDeContenido() {
        Paquete<Alimento> alimento = new Paquete<>(
                "P-005",
                8,
                "Salta",
                false,
                new Alimento("Lacteos", true)
        );
        Paquete<Fragil> fragil = new Paquete<>(
                "P-006",
                3,
                "Neuquen",
                false,
                new Fragil("Vajilla")
        );

        assertTrue(alimento.getContenido().isRefrigerado());
        assertEquals("Vajilla", fragil.getContenido().getDescripcion());
    }
}
