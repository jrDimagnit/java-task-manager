package managers.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private static String kvServerUrl;
    private String apiToken;
    private int status;
    private HttpClient client;


    public KVTaskClient(String kvServerUrl) {
        this.kvServerUrl = kvServerUrl;
        this.client = HttpClient.newHttpClient();
    }

    public String getApiToken() {
        return kvServerUrl;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void register() {
        URI uri = URI.create(kvServerUrl + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            setApiToken(response.body());
            setStatus(response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void put(String key, String json) {
        URI uri = URI.create(kvServerUrl + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            setStatus(response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public String load(String key) {
        URI uri = URI.create(kvServerUrl + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.body().isEmpty() || response.body() == null || response.body().equals("null")) {
                return null;
            } else {
                setStatus(response.statusCode());
                return response.body();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
