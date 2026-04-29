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

public class InventarioLoader {
        private Gson gson = new Gson();

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

    public void guardarPaquetes(String archivo, List<Paquete<Contenido>> paquetes) throws IOException {
        JsonArray paquetesJson = new JsonArray();

        for (Paquete<Contenido> paquete : paquetes) {
            JsonObject paqueteJson = new JsonObject();
            paqueteJson.addProperty("id", paquete.getId());
            paqueteJson.addProperty("peso", paquete.getPeso());
            paqueteJson.addProperty("destino", paquete.getDestino());
            paqueteJson.addProperty("urgente", paquete.isUrgente());
            paqueteJson.addProperty("tipoContenido", obtenerTipoContenido(paquete.getContenido()));
            paqueteJson.add("contenido", gson.toJsonTree(paquete.getContenido()));
            paquetesJson.add(paqueteJson);
        }

        JsonObject raiz = new JsonObject();
        raiz.add("paquetes", paquetesJson);

        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(raiz, writer);
        }
    }

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

    private String obtenerTipoContenido(Contenido contenido) {
        if (contenido instanceof Electronica) {
            return "Electronica";
        }
        if (contenido instanceof Alimento) {
            return "Alimento";
        }
        if (contenido instanceof Fragil) {
            return "Fragil";
        }
        throw new IllegalArgumentException("Tipo de contenido no soportado: " + contenido.getClass().getSimpleName());
    }
}
