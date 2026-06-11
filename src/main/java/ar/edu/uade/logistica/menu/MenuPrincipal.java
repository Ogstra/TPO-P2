package ar.edu.uade.logistica.menu;

import ar.edu.uade.logistica.modelo.Alimento;
import ar.edu.uade.logistica.modelo.Contenido;
import ar.edu.uade.logistica.modelo.Deposito;
import ar.edu.uade.logistica.modelo.Electronica;
import ar.edu.uade.logistica.modelo.Fragil;
import ar.edu.uade.logistica.modelo.Paquete;
import ar.edu.uade.logistica.servicio.Camion;
import ar.edu.uade.logistica.servicio.CentroDistribucion;
import ar.edu.uade.logistica.servicio.DepositoLoader;
import ar.edu.uade.logistica.servicio.InventarioLoader;
import ar.edu.uade.logistica.tda.GrafoDepositos;
import ar.edu.uade.logistica.tda.RedDepositos;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuPrincipal {
    private Scanner scanner = new Scanner(System.in);
    private CentroDistribucion<Contenido> centro = new CentroDistribucion<>();
    private Camion<Contenido> camion = new Camion<>("UADE-001");
    private InventarioLoader inventarioLoader = new InventarioLoader();
    private String rutaInventario = "src/main/resources/inventario.json";

    private RedDepositos redDepositos = new RedDepositos();
    private GrafoDepositos grafoDepositos = new GrafoDepositos();
    private DepositoLoader depositoLoader = new DepositoLoader();
    private String rutaDepositos = "src/main/resources/depositos.json";

    public void iniciar() {
        cargarInventarioInicial();
        cargarDepositosInicial();
        int opcion;
        do {
            mostrarOpciones();
            opcion = leerEntero("Opcion: ");
            ejecutar(opcion);
        } while (opcion != 0);
    }

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("=== Logi UADE 2026 ===");
        System.out.println("--- Iteracion 1: Paquetes ---");
        System.out.println("1. Crear paquete manual");
        System.out.println("2. Cargar siguiente paquete al camion");
        System.out.println("3. Deshacer ultima carga del camion");
        System.out.println("4. Ver estado");
        System.out.println("5. Listar proximos 10 paquetes");
        System.out.println("--- Iteracion 2: Depositos ---");
        System.out.println("6. Buscar deposito por ID");
        System.out.println("7. Agregar deposito");
        System.out.println("8. Ejecutar auditoria de depositos");
        System.out.println("9. Ver depositos en nivel N");
        System.out.println("10. Calcular distancia minima entre depositos");
        System.out.println("0. Salir");
    }

    private void ejecutar(int opcion) {
        try {
            switch (opcion) {
                case 1: crearPaqueteManual(); break;
                case 2: cargarCamion(); break;
                case 3: deshacerCarga(); break;
                case 4: mostrarEstado(); break;
                case 5: listarProximosPaquetes(); break;
                case 6: buscarDeposito(); break;
                case 7: agregarDeposito(); break;
                case 8: ejecutarAuditoria(); break;
                case 9: verDepositosEnNivel(); break;
                case 10: calcularDistanciaMinima(); break;
                case 0: System.out.println("Fin del sistema"); break;
                default: System.out.println("Opcion invalida"); break;
            }
        } catch (RuntimeException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cargarInventarioInicial() {
        try {
            cargarJson();
            System.out.println("Inventario cargado automaticamente desde " + rutaInventario);
        } catch (RuntimeException | IOException e) {
            System.out.println("No se pudo cargar el inventario inicial: " + e.getMessage());
        }
    }

    private void cargarDepositosInicial() {
        try {
            depositoLoader.cargar(rutaDepositos, redDepositos, grafoDepositos);
            System.out.println("Depositos cargados automaticamente desde " + rutaDepositos);
        } catch (RuntimeException | IOException e) {
            System.out.println("No se pudo cargar los depositos: " + e.getMessage());
        }
    }

    private void buscarDeposito() {
        int id = leerEntero("ID del deposito: ");
        Deposito deposito = redDepositos.buscar(id);
        if (deposito == null) {
            System.out.println("Deposito no encontrado");
        } else {
            System.out.println(deposito);
        }
    }

    private void agregarDeposito() throws IOException {
        int id = leerEntero("ID: ");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        // fecha ultima auditoria (opcional)
        LocalDateTime fechaAuditoria = null;
        System.out.print("Fecha ultima auditoria [yyyy-MM-ddTHH:mm:ss] (Enter para omitir): ");
        String fechaStr = scanner.nextLine().trim();
        if (!fechaStr.isBlank()) {
            try {
                fechaAuditoria = LocalDateTime.parse(fechaStr);
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido, se deja sin fecha de auditoria");
            }
        }

        Deposito deposito = new Deposito(id, nombre, false, fechaAuditoria);
        redDepositos.insertar(deposito);
        grafoDepositos.agregarVertice(id);

        // conexiones con otros depositos
        System.out.print("IDs de depositos a conectar separados por coma (Enter para omitir): ");
        String conexionesStr = scanner.nextLine().trim();
        if (!conexionesStr.isBlank()) {
            for (String parte : conexionesStr.split(",")) {
                try {
                    int idVecino = Integer.parseInt(parte.trim());
                    if (grafoDepositos.existeVertice(idVecino) &&
                            !grafoDepositos.obtenerVecinos(id).contains(idVecino)) {
                        grafoDepositos.agregarArista(id, idVecino);
                    } else if (!grafoDepositos.existeVertice(idVecino)) {
                        System.out.println("Deposito " + idVecino + " no existe, conexion omitida");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID invalido: " + parte.trim());
                }
            }
        }

        guardarDepositos();
        System.out.println("Deposito agregado: " + deposito);
    }

    private void ejecutarAuditoria() throws IOException {
        redDepositos.auditoria();
        guardarDepositos();
        System.out.println("Auditoria completada (post-orden). Depositos sin auditoria reciente marcados como visitados.");
    }

    private void verDepositosEnNivel() {
        int nivel = leerEntero("Nivel (raiz = 0): ");
        ArrayList<Deposito> depositos = redDepositos.depositosEnNivel(nivel);
        if (depositos.isEmpty()) {
            System.out.println("No hay depositos en el nivel " + nivel);
        } else {
            System.out.println("Depositos en nivel " + nivel + ":");
            for (Deposito d : depositos) {
                System.out.println("  " + d);
            }
        }
    }

    private void calcularDistanciaMinima() {
        int origen = leerEntero("ID deposito origen: ");
        int destino = leerEntero("ID deposito destino: ");
        int distancia = grafoDepositos.distanciaMinima(origen, destino);
        if (distancia == -1) {
            System.out.println("No existe camino entre los depositos " + origen + " y " + destino);
        } else {
            System.out.println("Distancia minima entre " + origen + " y " + destino + ": " + distancia + " salto(s)");
        }
    }

    private void cargarJson() throws IOException {
        ArrayList<Paquete<Contenido>> paquetes = inventarioLoader.cargarPaquetes(rutaInventario);
        for (Paquete<Contenido> paquete : paquetes) {
            centro.recibir(paquete);
        }
    }

    private void crearPaqueteManual() throws IOException {
        System.out.print("ID: ");
        String id = scanner.nextLine();
        double peso = leerDouble("Peso: ");
        System.out.print("Destino: ");
        String destino = scanner.nextLine();
        boolean urgente = leerBoolean("Urgente? [s/n]: ");
        Contenido contenido = leerContenido();
        Paquete<Contenido> paquete = new Paquete<>(id, peso, destino, urgente, contenido);
        centro.recibir(paquete);
        guardarCentroEnJson();
        System.out.println("Paquete recibido en el centro");
        mostrarPaqueteEnTabla(paquete);
    }

    private void cargarCamion() throws IOException {
        Paquete<Contenido> paquete = centro.procesarSiguiente();
        camion.cargar(paquete);
        guardarCentroEnJson();
        System.out.println("Cargado al camion:");
        mostrarPaqueteEnTabla(paquete);
    }

    private void deshacerCarga() throws IOException {
        Paquete<Contenido> paquete = camion.deshacerUltimaCarga();
        centro.recibir(paquete);
        guardarCentroEnJson();
        System.out.println("Carga deshecha y paquete devuelto al centro:");
        mostrarPaqueteEnTabla(paquete);
    }

    private void mostrarEstado() {
        System.out.println("Pendientes en centro: " + centro.cantidadPendiente());
        System.out.println("Paquetes en camion: " + camion.cantidadPaquetes());
        if (!centro.estaVacio()) {
            System.out.println("Siguiente del centro:");
            mostrarPaqueteEnTabla(centro.verSiguiente());
        }
        if (!camion.estaVacio()) {
            System.out.println("Proxima descarga del camion:");
            mostrarPaqueteEnTabla(camion.verProximaDescarga());
        }
    }

    private void listarProximosPaquetes() {
        ArrayList<Paquete<Contenido>> proximos = centro.verProximos(10);
        if (proximos.isEmpty()) {
            System.out.println("No hay paquetes pendientes en el centro");
            return;
        }

        System.out.println("Proximos paquetes del centro:");
        mostrarPaquetesEnTabla(proximos);
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido");
            }
        }
    }

    private boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = scanner.nextLine().trim().toLowerCase();
            if (valor.equals("s") || valor.equals("si")) {
                return true;
            }
            if (valor.equals("n") || valor.equals("no")) {
                return false;
            }
            System.out.println("Responda s o n");
        }
    }

    private Contenido leerContenido() {
        while (true) {
            System.out.println("Tipo de contenido:");
            System.out.println("1. Electronica");
            System.out.println("2. Alimento");
            System.out.println("3. Fragil");
            int opcion = leerEntero("Opcion: ");

            System.out.print("Descripcion: ");
            String descripcion = scanner.nextLine();

            switch (opcion) {
                case 1:
                    boolean asegurado = leerBoolean("Asegurado? [s/n]: ");
                    return new Electronica(descripcion, asegurado);
                case 2:
                    boolean refrigerado = leerBoolean("Refrigerado? [s/n]: ");
                    return new Alimento(descripcion, refrigerado);
                case 3:
                    return new Fragil(descripcion);
                default:
                    System.out.println("Tipo de contenido invalido");
                    break;
            }
        }
    }

    private void mostrarPaqueteEnTabla(Paquete<Contenido> paquete) {
        ArrayList<Paquete<Contenido>> paquetes = new ArrayList<>();
        paquetes.add(paquete);
        mostrarPaquetesEnTabla(paquetes);
    }

    private void mostrarPaquetesEnTabla(ArrayList<Paquete<Contenido>> paquetes) {
        System.out.println("+--------+--------+----------+----------+-------------+------------------------------+------------------+");
        System.out.println("| ID     | Peso   | Destino  | Urgente  | Tipo        | Descripcion                  | Detalle          |");
        System.out.println("+--------+--------+----------+----------+-------------+------------------------------+------------------+");
        for (int i = 0; i < paquetes.size(); i++) {
            Paquete<Contenido> paquete = paquetes.get(i);
            String[] datosContenido = describirContenido(paquete.getContenido());
            System.out.printf(
                    "| %-6s | %-6.1f | %-8s | %-8s | %-11s | %-28s | %-16s |%n",
                    paquete.getId(),
                    paquete.getPeso(),
                    recortar(paquete.getDestino(), 8),
                    paquete.isUrgente() ? "Si" : "No",
                    datosContenido[0],
                    recortar(datosContenido[1], 28),
                    recortar(datosContenido[2], 16)
            );
        }
        System.out.println("+--------+--------+----------+----------+-------------+------------------------------+------------------+");
    }

    private String[] describirContenido(Contenido contenido) {
        if (contenido instanceof Electronica electronica) {
            return new String[]{
                    "Electronica",
                    electronica.getDescripcion(),
                    "Asegurado: " + (electronica.isAsegurado() ? "Si" : "No")
            };
        }
        if (contenido instanceof Alimento alimento) {
            return new String[]{
                    "Alimento",
                    alimento.getDescripcion(),
                    "Refrig.: " + (alimento.isRefrigerado() ? "Si" : "No")
            };
        }
        if (contenido instanceof Fragil fragil) {
            return new String[]{
                    "Fragil",
                    fragil.getDescripcion(),
                    "Manejo especial"
            };
        }
        return new String[]{"N/D", String.valueOf(contenido), "-"};
    }

    private String recortar(String valor, int largoMaximo) {
        if (valor.length() <= largoMaximo) {
            return valor;
        }
        return valor.substring(0, largoMaximo - 3) + "...";
    }

    private void guardarCentroEnJson() throws IOException {
        inventarioLoader.guardarPaquetes(rutaInventario, centro.verProximos(centro.cantidadPendiente()));
    }

    private void guardarDepositos() throws IOException {
        depositoLoader.guardar(rutaDepositos, redDepositos, grafoDepositos);
    }
}
