import managers.*;
import tasks.Epic;
import tasks.StatusTask;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task = new Task(manager.changeId(), "Задача1", " Информация", StatusTask.NEW);

        manager.addTask(task);

        Epic epic = new Epic(manager.changeId(), "Эпик1", "Инфа", StatusTask.NEW);

        manager.addEpic(epic);

        SubTask subTask = new SubTask(manager.changeId(), "ПОдзадача1", "INFO",
                2, StatusTask.NEW);

        manager.addSubTask(subTask);

        SubTask subTask1 = new SubTask(manager.changeId(), "ПОдзадача 2", "INFA",
                2, StatusTask.NEW);

        manager.addSubTask(subTask1);

        System.out.println("Подзадачи во втором эпике");

        Task task1 = new Task(1, "Задача 1,5", "Новая Инфа", StatusTask.IN_PROGRESS);

        manager.updateTask(task1);
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubById(3);

        System.out.println(manager.subInEpic(2));

        System.out.println("____________________________");
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubById(3);

        System.out.println("_____________________________");
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubById(3);

        Epic epic1 = new Epic(manager.changeId(), "Эпик 2", "Информация2", StatusTask.NEW);

        manager.addEpic(epic1);
        System.out.println(manager.getTaskById(1));
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubById(3);

        SubTask subTask2 = new SubTask(manager.changeId(), "", "", 5, StatusTask.NEW);

        manager.addSubTask(subTask2);

        System.out.println("_______________________________________________________");

        SubTask subtask3 = new SubTask(3, "", "", 2, StatusTask.DONE);

        manager.updateSubTask(subtask3);


        System.out.println("______________DELETE____________");


        System.out.println("_____________________EPIC DELETE___________");

        Epic epic2 = new Epic(5, "Новый эпик 2", "New Info", StatusTask.DONE);

        manager.updateEpic(epic2);

        System.out.println("______");

        System.out.println("Epics " + manager.getAllEpic() + "Tasks " + manager.getAllTask() +

                "SubTasks" + manager.getAllSubTask());
        manager.getAllHistory();

        System.out.println("______");
        System.out.println("______");

    }
}

