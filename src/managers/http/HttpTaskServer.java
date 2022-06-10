package managers.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static TaskManager manager;
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private static List<Task> tasks;
    private static String response;
    private static HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.manager = taskManager;
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new PriorityHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/subtask", new SubTaskHandler());
        httpServer.createContext("/tasks/subtask/epic", new SubInEpicHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
    }

    static class PriorityHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            if (method.contains("GET")) {
                tasks = manager.getPrioritizedTasks();
                response = gson.toJson(tasks);
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                tasks = manager.getAllHistory();
                response = gson.toJson(tasks);
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    static class SubInEpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            if (method.equals("GET") && query.startsWith("id=")) {
                String[] splitQuery = query.split("=");
                response = gson.toJson(manager.getEpicById(Integer.parseInt(splitQuery[1])).getSubTasks());
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                httpExchange.sendResponseHeaders(400, 0);
                httpExchange.close();
            }
        }
    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            switch (method) {
                case "GET":
                    if (query == null) {
                        tasks = manager.getAllTask();
                        try {
                            response = gson.toJson(tasks);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if (query.startsWith("id=")) {
                        String[] splitQuery = query.split("=");
                        response = gson.toJson(manager.getTaskById(Integer.parseInt(splitQuery[1])));
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                    break;
                case "POST":
                    if (query == null) {
                        if (body.contains("nameTask") && body.contains("infoTask") && body.contains("statusTask")) {
                            Task task = gson.fromJson(body, Task.class);
                            if (task.getIdNumber() == null) {
                                manager.addTask(task);
                                httpExchange.sendResponseHeaders(201, 0);
                                httpExchange.close();
                            } else {
                                manager.updateTask(task);
                                httpExchange.sendResponseHeaders(201, 0);
                                httpExchange.close();
                                break;
                            }
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        httpExchange.close();
                    }
                    break;
                case "DELETE":
                    if (query.isEmpty()) {
                        manager.removeAllTask();
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                    } else if (query.startsWith("id=")) {
                        String[] splitQuery = query.split("=");
                        manager.removeIdTask(Integer.parseInt(splitQuery[1]));
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        httpExchange.close();
                    }
                default:
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
            }
        }
    }

    static class SubTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            switch (method) {
                case "GET":
                    if (query == null) {
                        tasks = new ArrayList<>(manager.getAllSubTask());
                        response = gson.toJson(tasks);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if (query.startsWith("id=")) {
                        String[] splitQuery = query.split("=");
                        response = gson.toJson(manager.getSubById(Integer.parseInt(splitQuery[1])));
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                    break;
                case "POST":
                    if (body.contains("nameTask") && body.contains("infoTask") && body.contains("statusTask") &&
                            body.contains("mainEpic")) {
                        SubTask subTask = gson.fromJson(body, SubTask.class);
                        if (subTask.getIdNumber() == null) {
                            manager.addSubTask(subTask);
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                        } else {
                            manager.updateSubTask(subTask);
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            break;
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        httpExchange.close();
                    }
                    break;
                case "DELETE":
                    if (query.isEmpty()) {
                        manager.removeAllSub();
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                    } else if (query.startsWith("id=")) {
                        String[] splitQuery = query.split("=");
                        manager.removeIdSub(Integer.parseInt(splitQuery[1]));
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        httpExchange.close();
                    }
                default:
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
            }
        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            switch (method) {
                case "GET":
                    if (query == null) {
                        tasks = new ArrayList<>(manager.getAllEpic());
                        response = gson.toJson(tasks);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if (query.startsWith("id=")) {
                        String[] splitQuery = query.split("=");
                        response = gson.toJson(manager.getEpicById(Integer.parseInt(splitQuery[1])));
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                    break;
                case "POST":
                    if (body.contains("nameTask") && body.contains("infoTask") && body.contains("statusTask")) {
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (epic.getIdNumber() == null) {
                            manager.addEpic(epic);
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                        } else {
                            manager.updateEpic(epic);
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            break;
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        httpExchange.close();
                    }
                    break;
                case "DELETE":
                    if (query.isEmpty()) {
                        manager.removeAllEpic();
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                    } else if (query.startsWith("id=")) {
                        String[] splitQuery = query.split("=");
                        manager.removeIdEpic(Integer.parseInt(splitQuery[1]));
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        httpExchange.close();
                    }
                default:
                    httpExchange.sendResponseHeaders(400, 0);
                    httpExchange.close();
            }
        }
    }

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
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
