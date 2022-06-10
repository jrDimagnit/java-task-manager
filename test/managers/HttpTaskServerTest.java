package managers;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.*;
import com.google.gson.Gson;
import managers.http.HttpTaskServer;
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
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    static KVServer kvServer;
    static HttpTaskServer httpTaskServer;
    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new TestLocalDateTimeAdapter())
            .create();

    @BeforeEach
    void initServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskServer = new HttpTaskServer(Managers.getDefault());
            httpTaskServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    void endInit() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void CreateEpicTest() {
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
    }

    @Test
    void createTaskWithoutDateTest() {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient client = HttpClient.newHttpClient();
        Task newTask = new Task("T1", "inf", StatusTask.NEW);
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void createTaskWithBadURITest() {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpClient client = HttpClient.newHttpClient();
        Task newTask = new Task("T1", "inf", StatusTask.NEW);
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(400, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void createSubAndEpicWithoutDateTest() {
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
            SubTask subTask = new SubTask("T1", "inf", 1, StatusTask.NEW);
            String json1 = gson.toJson(subTask);
            final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();

            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response1.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getTaskByIdTest() {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient client = HttpClient.newHttpClient();
        Task newTask = new Task("T1", "info", StatusTask.NEW);
        newTask.setUpDateAndDuration(LocalDateTime.of(2022, 01, 10, 20, 10), 60);
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url1 = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpClient client1 = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        try {
            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            assertTrue(response1.body().contains("info"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getPriorityTest() {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient client = HttpClient.newHttpClient();
        Task newTask = new Task("T1", "info", StatusTask.NEW);
        newTask.setUpDateAndDuration(LocalDateTime.of(2022, 01, 10, 20, 10), 60);
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        URI url1 = URI.create("http://localhost:8080/tasks/");
        HttpClient client1 = HttpClient.newHttpClient();
        try {
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
            String str = response1.body();
            assertTrue(str.contains("T1"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getHistoryAndTaskAndCreateTaskWithDateTask() {
        URI url1 = URI.create("http://localhost:8080/tasks/task/");
        HttpClient client1 = HttpClient.newHttpClient();
        Task newTask = new Task("T1", "info", StatusTask.NEW);
        newTask.setUpDateAndDuration(LocalDateTime.of(2022, 01, 10, 20, 10), 60);
        String json1 = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
        try {
            HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response1.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url2 = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpClient client2 = HttpClient.newHttpClient();
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        try {
            HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response2.statusCode());
            assertTrue(response2.body().contains("info"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url3 = URI.create("http://localhost:8080/tasks/history");
        HttpClient client3 = HttpClient.newHttpClient();
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).GET().build();
        try {
            HttpResponse<String> response3 = client3.send(request3, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response3.statusCode());
            assertTrue(response3.body().contains("info"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void addEpicAndSubAndGetByIdTest() {
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
    }

    @Test
    void deleteTaskAfterAddTest() {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpClient client = HttpClient.newHttpClient();
        Task newTask = new Task("T1", "info", StatusTask.NEW);
        newTask.setUpDateAndDuration(LocalDateTime.of(2022, 01, 10, 20, 10), 60);
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url4 = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpClient client4 = HttpClient.newHttpClient();
        HttpRequest request4 = HttpRequest.newBuilder().uri(url4).DELETE().build();
        try {
            HttpResponse<String> response4 = client4.send(request4, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response4.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url2 = URI.create("http://localhost:8080/tasks/");
        HttpClient client2 = HttpClient.newHttpClient();
        try {
            HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
            HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response2.statusCode());
            String str = response2.body();
            assertFalse(str.contains("T1"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void deleteSubTaskTest() {
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
        URI url2 = URI.create("http://localhost:8080/tasks/subtask");
        HttpClient client2 = HttpClient.newHttpClient();
        try {
            HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
            HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response2.statusCode());
            String str = response2.body();
            assertTrue(str.contains("S1"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url4 = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpClient client4 = HttpClient.newHttpClient();
        HttpRequest request4 = HttpRequest.newBuilder().uri(url4).DELETE().build();
        try {
            HttpResponse<String> response4 = client4.send(request4, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response4.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url5 = URI.create("http://localhost:8080/tasks/subtask");
        HttpClient client5 = HttpClient.newHttpClient();
        try {
            HttpRequest request5 = HttpRequest.newBuilder().uri(url5).GET().build();
            HttpResponse<String> response5 = client5.send(request5, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response5.statusCode());
            String str = response5.body();
            assertFalse(str.contains("S1"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void deleteEpicWithSubTest() {
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
        URI url2 = URI.create("http://localhost:8080/tasks/subtask");
        HttpClient client2 = HttpClient.newHttpClient();
        try {
            HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
            HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response2.statusCode());
            String str = response2.body();
            assertTrue(str.contains("S1"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url4 = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpClient client4 = HttpClient.newHttpClient();
        HttpRequest request4 = HttpRequest.newBuilder().uri(url4).DELETE().build();
        try {
            HttpResponse<String> response4 = client4.send(request4, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response4.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url5 = URI.create("http://localhost:8080/tasks/subtask");
        HttpClient client5 = HttpClient.newHttpClient();
        try {
            HttpRequest request5 = HttpRequest.newBuilder().uri(url5).GET().build();
            HttpResponse<String> response5 = client5.send(request5, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response5.statusCode());
            String str = response5.body();
            assertFalse(str.contains("S1"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        URI url6 = URI.create("http://localhost:8080/tasks/epic");
        HttpClient client6 = HttpClient.newHttpClient();
        try {
            HttpRequest request6 = HttpRequest.newBuilder().uri(url6).GET().build();
            HttpResponse<String> response6 = client6.send(request6, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response6.statusCode());
            String str = response6.body();
            assertFalse(str.contains("T1"));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }


    static class TestLocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
            if (localDate == null) {
                jsonWriter.value("null");
            } else {
                jsonWriter.value(localDate.format(formatter));
            }
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            String str = jsonReader.nextString();
            if (str.equals("null")) {
                return null;
            }
            return LocalDateTime.parse(str, formatter);
        }
    }
}
