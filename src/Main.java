import Tasks.Manager;
import Tasks.Task;
import Tasks.SubTask;
import Tasks.Epic;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task = new Task(manager.changeId(), "Задача1", " Информация","NEW" );
        manager.addTask(task);
        Epic epic = new Epic(manager.changeId(), "Эпик1", "Инфа","NEW");
        manager.addEpic(epic);
        SubTask subTask = new SubTask(manager.changeId(), "ПОдзадача1",
                "INFO", 2, "NEW");
        manager.addSubTask(subTask);
        SubTask subTask1 = new SubTask(manager.changeId(), "ПОдзадача 2",
                "INFA",2, "NEW");
        manager.addSubTask(subTask1);
        manager.printAll();
        System.out.println("Подзадачи во втором эпике");
        manager.subInEpic(2);

        Task task1 = new Task(1,"Задача 1,5", "Новая Инфа", "IN_PROGRESS");
        manager.updateTask(task1);
        System.out.println("____________________________");
        manager.printTask();
        System.out.println("_____________________________");
        Epic epic1 = new Epic(manager.changeId(), "Эпик 2","Информация2","NEW");
        manager.addEpic(epic1);
        SubTask subTask2 = new SubTask(manager.changeId(), "","",5,"NEW");
        manager.addSubTask(subTask2);
        manager.printEpic();
        System.out.println("_______________________________________________________");
        SubTask subtask3 = new SubTask( 3, "","",2,"DONE");
        manager.updateSubTask(subtask3);
        manager.printEpic();
        manager.printSubTask();
        manager.printAll();
        manager.removeId(3);
        System.out.println("______________DELETE____________");
        manager.printEpic();
        manager.printSubTask();
        manager.removeId(2);
        System.out.println("_____________________EPIC DELETE___________");
        manager.printSubTask();
        manager.printAll();
        manager.subInEpic(5);
        manager.getStatus(5);
    }
}