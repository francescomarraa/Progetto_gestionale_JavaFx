package org.example.progettouid.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class PippoBot {

    /*
     * NOTA DI SICUREZZA:
     * L'API Key è qui hardcodata per semplicità didattica e facilità di test.
     * In un ambiente di produzione, questa pratica rappresenta una vulnerabilità (Hardcoded Secret).
     *
     * Soluzione corretta per la produzione:
     * 1. Utilizzare variabili d'ambiente o file di configurazione esterni (es. local.properties) non versionati.
     * 2. Implementare un Backend Proxy: il client chiama il nostro server sicuro, che a sua volta interroga
     *    le API di Gemini usando la chiave segreta, mantenendola confinata lato server.
     */
    private static final String API_KEY = "AIzaSyCcATx8tJtKpcbgZ4RNAPN-b7KVvcg7yXg";
    
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=" + API_KEY;

    public String chiedi(String richiesta) {

        try {

            JsonObject textPart = new JsonObject();
            textPart.addProperty("text", richiesta);

            JsonArray parts = new JsonArray();
            parts.add(textPart);

            JsonObject contentParts = new JsonObject();
            contentParts.add("parts", parts);

            JsonArray container = new JsonArray();
            container.add(contentParts);

            JsonObject requestBody = new JsonObject();
            requestBody.add("contents", container);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return "Errore nella richiesta: " + response.statusCode();
            } else {
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

                String rispostaBot = jsonResponse.getAsJsonArray("candidates")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("content")
                        .getAsJsonArray("parts")
                        .get(0).getAsJsonObject()
                        .get("text").getAsString();

                return rispostaBot;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Scusa, ho avuto un problema di connessione.";
        }
    }
}