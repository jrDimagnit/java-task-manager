import managers.*;
import tasks.Epic;
import tasks.StatusTask;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task = new Task(manager.changeId(), "Т1", " И", StatusTask.NEW);
        Task task1 = new Task(manager.changeId(), "Т2", " И", StatusTask.NEW);
        Epic epic = new Epic(manager.changeId(), "Э1", "И", StatusTask.NEW);
        Epic epic1 = new Epic(manager.changeId(), "Э2", "И", StatusTask.NEW);
        SubTask subTask = new SubTask(manager.changeId(), "С1", "I",
                3, StatusTask.NEW);
        SubTask subTask1 = new SubTask(manager.changeId(), "С2", "I",
                3, StatusTask.NEW);
        SubTask subTask2 = new SubTask(manager.changeId(), "С3", "I",
                4, StatusTask.NEW);
        SubTask subTask3 = new SubTask(manager.changeId(), "С4", "I",
                4, StatusTask.NEW);
        SubTask subTask4 = new SubTask(manager.changeId(), "С5", "I",
                4, StatusTask.NEW);

        manager.addTask(task);
        manager.addTask(task1);
        manager.addEpic(epic);
        manager.addEpic(epic1);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        manager.addSubTask(subTask4);
        System.out.println("Создали и добавили задачи 2 таска 2 эпика в одном 2 в другом 3 подзадачи!");
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getEpicById(4);
        manager.getSubById(5);
        manager.getSubById(7);
        System.out.println("____________________________");
        System.out.println("ПРОСМОТРЕЛИ ЗАДАЧИ 1 2 3 4 5 и 7");
        manager.getAllHistory();
        System.out.println("___удаляем задачи и проверяем просмотр___");

        manager.removeIdSub(8);
        manager.removeIdEpic(3);
        manager.getAllHistory();
    }
}

