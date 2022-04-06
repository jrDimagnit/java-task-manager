package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    void addTask(Task task);

    int changeId();

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    HashMap<Integer, Task> getAllTask();

    HashMap<Integer, SubTask> getAllSubTask();

    HashMap<Integer, Epic> getAllEpic();

    ArrayList<SubTask> subInEpic(int taskId);

    void removeAll();

    void removeId(int idTask);

    String getStatus(int taskId);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    Epic getEpicById(int taskId);

    SubTask getSubById(int taskId);

    Task getTaskById(int taskId);

    void getAllHistory();

}
