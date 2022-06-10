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

    public HttpTaskManager(KVTaskClient kvTaskClient) {
        super(Managers.getDefaultHistory(), Managers.pathFile);
        this.kvTaskClient = kvTaskClient;
        kvTaskClient.register();
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
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllSub() {
        super.removeAllSub();
        save();
    }

    @Override
    public void removeIdTask(int idTask) {
        super.removeIdTask(idTask);
        save();
    }

    @Override
    public void removeIdSub(int idTask) {
        super.removeIdSub(idTask);
        save();
    }

    @Override
    public void removeIdEpic(int idTask) {
        super.removeIdEpic(idTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public Epic getEpicById(int taskId) {
        Epic epic = super.getEpicById(taskId);
        save();
        return epic;
    }

    @Override
    public SubTask getSubById(int taskId) {
        SubTask subTask = super.getSubById(taskId);
        save();
        return subTask;
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    protected void save() {
        if (!getAllTask().isEmpty() && getAllTask() != null) {
            StringBuilder requestBuilder = new StringBuilder();
            for (Task task : getAllTask()) {
                String request = gson.toJson(task);
                requestBuilder.append(request + ":::");
            }
            kvTaskClient.put(TypeTask.TASK.toString().toLowerCase(), requestBuilder.toString());
            if (400 <= kvTaskClient.getStatus() && kvTaskClient.getStatus() < 500) {
                System.out.println("Неправильный запрос");
            }
            if (200 <= kvTaskClient.getStatus() && kvTaskClient.getStatus() < 300) {
                System.out.println("Запрос успешно обработан!");
            }
        }
        if (!getAllEpic().isEmpty() && getAllEpic() != null) {
            StringBuilder requestBuilder = new StringBuilder();
            for (Epic epic : getAllEpic()) {
                String request = gson.toJson(epic);
                requestBuilder.append(request + ":::");
            }
            kvTaskClient.put(TypeTask.EPIC.toString().toLowerCase(), requestBuilder.toString());
            if (400 <= kvTaskClient.getStatus() && kvTaskClient.getStatus() < 500) {
                System.out.println("Неправильный запрос");
            }
            if (200 <= kvTaskClient.getStatus() && kvTaskClient.getStatus() < 300) {
                System.out.println("Запрос успешно обработан!");
            }
        }
        if (!getAllSubTask().isEmpty() && getAllSubTask() != null) {
            StringBuilder requestBuilder = new StringBuilder();
            for (SubTask subTask : getAllSubTask()) {
                String request = gson.toJson(subTask);
                requestBuilder.append(request + ":::");
            }
            kvTaskClient.put(TypeTask.SUBTASK.toString().toLowerCase(), requestBuilder.toString());
            if (400 <= kvTaskClient.getStatus() && kvTaskClient.getStatus() < 500) {
                System.out.println("Неправильный запрос");
            }
            if (200 <= kvTaskClient.getStatus() && kvTaskClient.getStatus() < 300) {
                System.out.println("Запрос успешно обработан!");
            }
        }
        if (!getAllHistory().isEmpty() && getAllHistory() != null) {
            StringBuilder historyTask = new StringBuilder();
            for (Task task : getAllHistory()) {
                historyTask.append(task.getIdNumber());
                historyTask.append(",");
            }
            kvTaskClient.put("history".toLowerCase(), historyTask.toString());
            if (400 <= kvTaskClient.getStatus() && kvTaskClient.getStatus() < 500) {
                System.out.println("Неправильный запрос");
            }
            if (200 <= kvTaskClient.getStatus() && kvTaskClient.getStatus() < 300) {
                System.out.println("Запрос успешно обработан!");
            }

        }
    }
}
