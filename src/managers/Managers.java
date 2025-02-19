package managers;

import managers.http.HttpTaskManager;

public class Managers {
    public static String pathFile = "src\\task.csv";
    public static String kvServerUrl = "http://localhost:8078";

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new HttpTaskManager(kvServerUrl);
    }

}
