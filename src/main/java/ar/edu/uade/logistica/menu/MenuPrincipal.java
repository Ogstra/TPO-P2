package ar.edu.uade.logistica.menu;

import ar.edu.uade.logistica.modelo.Alimento;
import ar.edu.uade.logistica.modelo.Contenido;
import ar.edu.uade.logistica.modelo.Electronica;
import ar.edu.uade.logistica.modelo.Fragil;
import ar.edu.uade.logistica.modelo.Paquete;
import ar.edu.uade.logistica.servicio.Camion;
import ar.edu.uade.logistica.servicio.CentroDistribucion;
import ar.edu.uade.logistica.servicio.InventarioLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuPrincipal {
    private Scanner scanner = new Scanner(System.in);
    private CentroDistribucion<Contenido> centro = new CentroDistribucion<>();
    private Camion<Contenido> camion = new Camion<>("UADE-001");
    private InventarioLoader inventarioLoader = new InventarioLoader();
    private String rutaInventario = "src/main/resources/inventario.json";

    public void iniciar() {
        cargarInventarioInicial();
        int opcion;
        do {
            mostrarOpciones();
            opcion = leerEntero("Opcion: ");
            ejecutar(opcion);
        } while (opcion != 0);
    }

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("Logi UADE 2026 - Iteracion 1");
        System.out.println("1. Crear paquete manual");
        System.out.println("2. Cargar siguiente paquete al camion");
        System.out.println("3. Deshacer ultima carga del camion");
        System.out.println("4. Ver resumen del sistema");
        System.out.println("5. Ver detalle completo");
        System.out.println("0. Salir");
    }

    private void ejecutar(int opcion) {
        try {
            switch (opcion) {
                case 1:
                    crearPaqueteManual();
                    break;
                case 2:
                    cargarCamion();
                    break;
                case 3:
                    deshacerCarga();
                    break;
                case 4:
                    mostrarEstado();
                    break;
                case 5:
                    verDetalleCompleto();
                    break;
                case 0:
                    System.out.println("Fin del sistema");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
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
        System.out.println("\nPendientes en centro: " + centro.cantidadPendiente());
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

    private void verDetalleCompleto() {
        System.out.println();
        System.out.println("=== CENTRO DE DISTRIBUCION (" + centro.cantidadPendiente() + " pendientes) ===");
        ArrayList<Paquete<Contenido>> pendientes = centro.verProximos(centro.cantidadPendiente());
        if (pendientes.isEmpty()) {
            System.out.println("Sin paquetes pendientes.");
        } else {
            mostrarPaquetesEnTabla(pendientes);
        }

        System.out.println();
        System.out.println("=== CAMION " + camion.getPatente() + " (" + camion.cantidadPaquetes() + " paquetes) ===");
        ArrayList<Paquete<Contenido>> carga = camion.listarCarga();
        if (carga.isEmpty()) {
            System.out.println("Camion vacio.");
        } else {
            mostrarPaquetesEnTabla(carga);
        }
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
}
