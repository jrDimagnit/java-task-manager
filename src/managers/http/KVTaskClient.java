package managers.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private static String kvServerUrl;
    private String apiToken;
    private HttpClient client;


    public KVTaskClient(String kvServerUrl) {
        this.kvServerUrl = kvServerUrl;
        this.client = HttpClient.newHttpClient();
        register();
    }

    public void register() {
        URI uri = URI.create(kvServerUrl + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                apiToken = response.body();
            } else {
                System.out.println("Register failed!");
            }
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
            if (response.statusCode() == 200) {
                System.out.println("Данные успешно записанны!");
            } else {
                System.out.println("Ошибка сохранения данных!");
            }
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
            } else if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.out.println("Ошибка загрузки данных!");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
