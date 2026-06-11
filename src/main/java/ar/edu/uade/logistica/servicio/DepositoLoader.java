package ar.edu.uade.logistica.servicio;

import ar.edu.uade.logistica.modelo.Deposito;
import ar.edu.uade.logistica.tda.GrafoDepositos;
import ar.edu.uade.logistica.tda.RedDepositos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DepositoLoader {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // O(n) - carga depositos desde JSON y construye ABB y grafo
    public void cargar(String archivo, RedDepositos red, GrafoDepositos grafo) throws IOException {
        try (FileReader reader = new FileReader(archivo)) {
            JsonObject raiz = gson.fromJson(reader, JsonObject.class);
            if (raiz == null) throw new IllegalArgumentException("JSON invalido");

            // acepta "depositos" o "depósitos"
            JsonArray depositosJson;
            if (raiz.has("depositos")) {
                depositosJson = raiz.getAsJsonArray("depositos");
            } else if (raiz.has("depósitos")) {
                depositosJson = raiz.getAsJsonArray("depósitos");
            } else {
                throw new IllegalArgumentException("El JSON debe tener la clave 'depositos'");
            }

            // primera pasada: insertar todos en ABB y grafo
            for (JsonElement elemento : depositosJson) {
                JsonObject obj = elemento.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                String nombre = obj.get("nombre").getAsString();
                boolean visitado = obj.has("visitado") && obj.get("visitado").getAsBoolean();
                LocalDateTime fecha = null;
                if (obj.has("fechaUltimaAuditoria") && !obj.get("fechaUltimaAuditoria").isJsonNull()) {
                    try {
                        fecha = LocalDateTime.parse(obj.get("fechaUltimaAuditoria").getAsString());
                    } catch (DateTimeParseException ignored) { }
                }
                red.insertar(new Deposito(id, nombre, visitado, fecha));
                grafo.agregarVertice(id);
            }

            // segunda pasada: agregar aristas (sin duplicados)
            for (JsonElement elemento : depositosJson) {
                JsonObject obj = elemento.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                if (obj.has("conexiones")) {
                    for (JsonElement conexion : obj.getAsJsonArray("conexiones")) {
                        int idVecino = conexion.getAsInt();
                        if (grafo.existeVertice(idVecino) &&
                                !grafo.obtenerVecinos(id).contains(idVecino)) {
                            grafo.agregarArista(id, idVecino);
                        }
                    }
                }
            }
        }
    }

    // O(n) - guarda estado actual de ABB y grafo a JSON
    public void guardar(String archivo, RedDepositos red, GrafoDepositos grafo) throws IOException {
        ArrayList<Deposito> depositos = red.getTodos();
        JsonArray depositosJson = new JsonArray();

        for (Deposito deposito : depositos) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", deposito.getId());
            obj.addProperty("nombre", deposito.getNombre());
            obj.addProperty("visitado", deposito.isVisitado());
            if (deposito.getFechaUltimaAuditoria() != null) {
                obj.addProperty("fechaUltimaAuditoria", deposito.getFechaUltimaAuditoria().toString());
            } else {
                obj.add("fechaUltimaAuditoria", gson.toJsonTree(null));
            }
            // conexiones del grafo
            JsonArray conexiones = new JsonArray();
            for (int vecino : grafo.obtenerVecinos(deposito.getId())) {
                conexiones.add(vecino);
            }
            obj.add("conexiones", conexiones);
            depositosJson.add(obj);
        }

        JsonObject raiz = new JsonObject();
        raiz.add("depositos", depositosJson);

        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(raiz, writer);
        }
    }
}
