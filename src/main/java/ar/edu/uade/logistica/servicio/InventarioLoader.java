package ar.edu.uade.logistica.servicio;

import ar.edu.uade.logistica.modelo.Alimento;
import ar.edu.uade.logistica.modelo.Contenido;
import ar.edu.uade.logistica.modelo.Electronica;
import ar.edu.uade.logistica.modelo.Fragil;
import ar.edu.uade.logistica.modelo.Paquete;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Responsable de leer y escribir el inventario en formato JSON usando Gson.
// Desacopla la persistencia del resto del sistema: ni CentroDistribucion ni
// el menu conocen el formato del archivo.
public class InventarioLoader {
    private static final Gson gson = new Gson();

    // O(n)
    // Lee el archivo JSON, parsea cada elemento del array "paquetes" y
    // construye los objetos Java correspondientes, incluyendo el subtipo de Contenido.
    public ArrayList<Paquete<Contenido>> cargarPaquetes(String archivo) throws IOException {
        try (FileReader reader = new FileReader(archivo)) {
            JsonObject raiz = gson.fromJson(reader, JsonObject.class);
            if (raiz == null || !raiz.has("paquetes")) {
                throw new IllegalArgumentException("El JSON debe tener una propiedad 'paquetes'");
            }

            JsonArray paquetesJson = raiz.getAsJsonArray("paquetes");
            ArrayList<Paquete<Contenido>> paquetes = new ArrayList<>();

            for (JsonElement elemento : paquetesJson) {
                JsonObject paqueteJson = elemento.getAsJsonObject();
                paquetes.add(new Paquete<>(
                        paqueteJson.get("id").getAsString(),
                        paqueteJson.get("peso").getAsDouble(),
                        paqueteJson.get("destino").getAsString(),
                        paqueteJson.get("urgente").getAsBoolean(),
                        leerContenido(
                                paqueteJson.get("tipoContenido").getAsString(),
                                paqueteJson.getAsJsonObject("contenido")
                        )
                ));
            }

            return paquetes;
        }
    }

    // O(n)
    // Serializa la lista de paquetes a JSON y sobreescribe el archivo.
    // Se usa para persistir el estado del centro luego de cada operacion.
    public void guardarPaquetes(String archivo, List<Paquete<Contenido>> paquetes) throws IOException {
        JsonArray paquetesJson = new JsonArray();

        for (Paquete<Contenido> paquete : paquetes) {
            JsonObject paqueteJson = new JsonObject();
            paqueteJson.addProperty("id", paquete.getId());
            paqueteJson.addProperty("peso", paquete.getPeso());
            paqueteJson.addProperty("destino", paquete.getDestino());
            paqueteJson.addProperty("urgente", paquete.isUrgente());
            paqueteJson.addProperty("tipoContenido", obtenerTipoContenido(paquete.getContenido()));
            // gson.toJsonTree serializa el objeto Contenido concreto con sus campos propios
            paqueteJson.add("contenido", gson.toJsonTree(paquete.getContenido()));
            paquetesJson.add(paqueteJson);
        }

        JsonObject raiz = new JsonObject();
        raiz.add("paquetes", paquetesJson);

        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(raiz, writer);
        }
    }

    // O(1)
    // Usa el campo "tipoContenido" del JSON para determinar que subclase instanciar.
    // Gson deserializa los campos automaticamente por nombre.
    private Contenido leerContenido(String tipoContenido, JsonObject contenidoJson) {
        switch (tipoContenido.toLowerCase()) {
            case "electronica":
                return gson.fromJson(contenidoJson, Electronica.class);
            case "alimento":
                return gson.fromJson(contenidoJson, Alimento.class);
            case "fragil":
                return gson.fromJson(contenidoJson, Fragil.class);
            default:
                throw new IllegalArgumentException("Tipo de contenido no soportado: " + tipoContenido);
        }
    }

    // O(1)
    // Mapea la clase Java del contenido al string que se guarda en el JSON.
    private String obtenerTipoContenido(Contenido contenido) {
        if (contenido instanceof Electronica) {
            return "electronica";
        }
        if (contenido instanceof Alimento) {
            return "alimento";
        }
        if (contenido instanceof Fragil) {
            return "fragil";
        }
        throw new IllegalArgumentException("Tipo de contenido no soportado: " + contenido.getClass().getSimpleName());
    }
}
