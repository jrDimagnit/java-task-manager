package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.http.HttpTaskManager;
import managers.http.HttpTaskServer;
import managers.http.KVTaskClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import tasks.Epic;
import tasks.StatusTask;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskManagerTest {
    static KVServer kvServer;
    static HttpTaskServer httpTaskServer;
    static KVTaskClient kvTaskClient;
    static HttpTaskManager manager;

    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new HttpTaskServerTest.TestLocalDateTimeAdapter())
            .create();

    @BeforeEach
    void initServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            kvTaskClient = new KVTaskClient(Managers.kvServerUrl);
            manager = new HttpTaskManager(kvTaskClient);
            httpTaskServer = new HttpTaskServer(manager);
            httpTaskServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void createTasksThenStopHttpAndLoadTest() {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpClient client = HttpClient.newHttpClient();
        Epic epic = new Epic("T1", "inf", StatusTask.NEW);
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url1 = URI.create("http://localhost:8080/tasks/subtask/");
        HttpClient client1 = HttpClient.newHttpClient();
        try {
            SubTask subTask = new SubTask("S1", "inf", 1, StatusTask.NEW);
            String json1 = gson.toJson(subTask);
            final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response1.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url3 = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpClient client3 = HttpClient.newHttpClient();
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).GET().build();
        try {
            HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response3.statusCode());
            assertTrue(response3.body().contains("inf"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url4 = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpClient client4 = HttpClient.newHttpClient();
        HttpRequest request4 = HttpRequest.newBuilder().uri(url4).GET().build();
        try {
            HttpResponse<String> response4 = client4.send(request4, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response4.statusCode());
            assertTrue(response4.body().contains("S1"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url2 = URI.create("http://localhost:8080/tasks/task/");
        HttpClient client2 = HttpClient.newHttpClient();
        Task newTask = new Task("T1", "info", StatusTask.NEW);
        newTask.setUpDateAndDuration(LocalDateTime.of(2022, 01, 10, 20, 10), 60);
        String json2 = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        try {
            HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response2.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        String str = kvTaskClient.getApiToken();

        httpTaskServer.stop();
        KVTaskClient kvTaskClient2 = new KVTaskClient(Managers.kvServerUrl);
        kvTaskClient2.setApiToken(str);
        HttpTaskManager manager1 = new HttpTaskManager(kvTaskClient2);
        try {
            HttpTaskServer httpTaskServer1 = new HttpTaskServer(manager1);
            httpTaskServer1.start();
            manager1.load();
            URI url5 = URI.create("http://localhost:8080/tasks/subtask");
            HttpClient client5 = HttpClient.newHttpClient();
            HttpRequest request5 = HttpRequest.newBuilder().uri(url5).GET().build();
            try {
                HttpResponse<String> response5 = client5.send(request5, HttpResponse.BodyHandlers.ofString());
                assertEquals(200, response5.statusCode());
                assertTrue(response5.body().contains("S1"));
                httpTaskServer1.stop();
                kvServer.stop();
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}

