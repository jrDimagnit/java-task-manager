package managers.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.FileBackedTasksManager;
import managers.Managers;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TypeTask;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
            .create();

    public HttpTaskManager(String kvServerUrl) {
        super(Managers.getDefaultHistory(), Managers.pathFile);
        this.kvTaskClient = new KVTaskClient(kvServerUrl);
        load();
    }


    public void load() {
        int setNewId = 1;
        if (kvTaskClient.load(TypeTask.TASK.toString().toLowerCase()) != null) {
            String[] tasks = kvTaskClient.load(TypeTask.TASK.toString().toLowerCase()).split(":::");
            for (String str : tasks) {
                Task task = gson.fromJson(str, Task.class);
                listTask.put(task.getIdNumber(), task);
                prioritizedTasks.add(task);
                if (setNewId < task.getIdNumber()) {
                    setNewId = task.getIdNumber();
                }
            }
        }
        if (kvTaskClient.load(TypeTask.EPIC.toString().toLowerCase()) != null) {
            String[] tasks = kvTaskClient.load(TypeTask.EPIC.toString().toLowerCase()).split(":::");
            for (String str : tasks) {
                Epic epic = gson.fromJson(str, Epic.class);
                listEpic.put(epic.getIdNumber(), epic);
                if (setNewId < epic.getIdNumber()) {
                    setNewId = epic.getIdNumber();
                }
            }
        }
        if (kvTaskClient.load(TypeTask.SUBTASK.toString().toLowerCase()) != null) {
            String[] tasks = kvTaskClient.load(TypeTask.SUBTASK.toString().toLowerCase()).split(":::");
            for (String str : tasks) {
                SubTask subTask = gson.fromJson(str, SubTask.class);
                listSubTask.put(subTask.getIdNumber(), subTask);
                prioritizedTasks.add(subTask);
                Epic epic = listEpic.get(subTask.getMainEpic());
                epic.getSubTasks().add(subTask);
                if (setNewId < subTask.getIdNumber()) {
                    setNewId = subTask.getIdNumber();
                }
            }
        }
        if (kvTaskClient.load("history") != null) {
            String[] history = kvTaskClient.load("history").split(",");
            List<Integer> taskList = new ArrayList<>();
            for (String str : history) {
                taskList.add(Integer.parseInt(str));
            }
            for (int i : taskList) {
                if (listTask.containsKey(i)) {
                    inMemory.addHistory(listTask.get(i));
                } else if (listSubTask.containsKey(i)) {
                    inMemory.addHistory(listSubTask.get(i));
                } else {
                    inMemory.addHistory(listEpic.get(i));
                }
            }
        }

    }
    @Override
    protected void save() {
        if (!getAllTask().isEmpty() && getAllTask() != null) {
            StringBuilder requestBuilder = new StringBuilder();
            for (Task task : getAllTask()) {
                String request = gson.toJson(task);
                requestBuilder.append(request + ":::");
            }
            kvTaskClient.put(TypeTask.TASK.toString().toLowerCase(), requestBuilder.toString());
        }
        if (!getAllEpic().isEmpty() && getAllEpic() != null) {
            StringBuilder requestBuilder = new StringBuilder();
            for (Epic epic : getAllEpic()) {
                String request = gson.toJson(epic);
                requestBuilder.append(request + ":::");
            }
            kvTaskClient.put(TypeTask.EPIC.toString().toLowerCase(), requestBuilder.toString());
        }
        if (!getAllSubTask().isEmpty() && getAllSubTask() != null) {
            StringBuilder requestBuilder = new StringBuilder();
            for (SubTask subTask : getAllSubTask()) {
                String request = gson.toJson(subTask);
                requestBuilder.append(request + ":::");
            }
            kvTaskClient.put(TypeTask.SUBTASK.toString().toLowerCase(), requestBuilder.toString());
        }
        if (!getAllHistory().isEmpty() && getAllHistory() != null) {
            StringBuilder historyTask = new StringBuilder();
            for (Task task : getAllHistory()) {
                historyTask.append(task.getIdNumber());
                historyTask.append(",");
            }
            kvTaskClient.put("history".toLowerCase(), historyTask.toString());
        }
    }
}
