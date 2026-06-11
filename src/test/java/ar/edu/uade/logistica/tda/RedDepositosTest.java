package ar.edu.uade.logistica.tda;

import ar.edu.uade.logistica.modelo.Deposito;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class RedDepositosTest {

    private Deposito crearDeposito(int id) {
        return new Deposito(id, "Deposito " + id, false, null);
    }

    @Test
    public void insertarYBuscarDeposito() {
        RedDepositos red = new RedDepositos();
        red.insertar(crearDeposito(50));
        red.insertar(crearDeposito(20));
        red.insertar(crearDeposito(80));

        assertNotNull(red.buscar(50));
        assertNotNull(red.buscar(20));
        assertNotNull(red.buscar(80));
        assertEquals(50, red.buscar(50).getId());
    }

    @Test
    public void buscarIdInexistenteRetornaNull() {
        RedDepositos red = new RedDepositos();
        red.insertar(crearDeposito(50));

        assertNull(red.buscar(99));
    }

    @Test
    public void auditoriaPostOrdenMarcaDepositosSinAuditoriaReciente() {
        RedDepositos red = new RedDepositos();
        // auditado hace mas de 30 dias -> debe marcarse visitado
        Deposito viejo = new Deposito(10, "Viejo", false, LocalDateTime.now().minusDays(60));
        // auditado hace menos de 30 dias -> no debe marcarse
        Deposito reciente = new Deposito(20, "Reciente", false, LocalDateTime.now().minusDays(5));
        // sin auditoria -> debe marcarse visitado
        Deposito sinAuditoria = new Deposito(30, "Sin auditoria", false, null);

        red.insertar(viejo);
        red.insertar(reciente);
        red.insertar(sinAuditoria);

        red.auditoria();

        assertTrue(red.buscar(10).isVisitado());
        assertFalse(red.buscar(20).isVisitado());
        assertTrue(red.buscar(30).isVisitado());
    }

    @Test
    public void depositosEnNivelRetornaDepositos() {
        RedDepositos red = new RedDepositos();
        // insertar en orden: 50 (nivel 0), 20 (nivel 1 izq), 80 (nivel 1 der)
        red.insertar(crearDeposito(50));
        red.insertar(crearDeposito(20));
        red.insertar(crearDeposito(80));

        ArrayList<Deposito> nivel0 = red.depositosEnNivel(0);
        ArrayList<Deposito> nivel1 = red.depositosEnNivel(1);

        assertEquals(1, nivel0.size());
        assertEquals(50, nivel0.get(0).getId());
        assertEquals(2, nivel1.size());
    }

    @Test
    public void depositosEnNivelInexistenteRetornaVacio() {
        RedDepositos red = new RedDepositos();
        red.insertar(crearDeposito(50));

        ArrayList<Deposito> nivel5 = red.depositosEnNivel(5);

        assertTrue(nivel5.isEmpty());
    }

    @Test
    public void noInsertaDuplicados() {
        RedDepositos red = new RedDepositos();
        red.insertar(crearDeposito(50));
        red.insertar(crearDeposito(50));  // duplicado: ignora

        ArrayList<Deposito> nivel0 = red.depositosEnNivel(0);
        assertEquals(1, nivel0.size());
    }
}
