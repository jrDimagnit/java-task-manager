import managers.*;
import tasks.Epic;
import tasks.StatusTask;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager(Managers.getDefaultHistory());

        Task task = new Task(manager.changeId(), "Т1", " И", StatusTask.NEW);
        task.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 0, 0), Duration.ofMinutes(60));
        manager.addTask(task);
        Task task1 = new Task(manager.changeId(), "Т1", " И", StatusTask.NEW);
        task1.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 0, 10), Duration.ofMinutes(60));
        manager.addTask(task1);
        Epic epic = new Epic(manager.changeId(), "Э1", "И", StatusTask.NEW);
        manager.addEpic(epic);
        SubTask subTask = new SubTask(manager.changeId(), "С1", "I",
                3, StatusTask.NEW);
        subTask.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 5, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask);
        SubTask subTask1 = new SubTask(manager.changeId(), "С2", "I",
                3, StatusTask.NEW);
        subTask1.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 10, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);

        System.out.println(manager.getPrioritizedTasks());
        System.out.println(manager.getAllEpic());
        manager.updateTask(task1);
    }
}

