package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    int changeId();

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    ArrayList<Task> getAllTask();

    ArrayList<SubTask> getAllSubTask();

    ArrayList<Epic> getAllEpic();

    ArrayList<SubTask> subInEpic(int taskId);

    void removeIdTask(int idTask);

    void removeIdSub(int idTask);

    void removeIdEpic(int idTask);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    Epic getEpicById(int taskId);

    SubTask getSubById(int taskId);

    Task getTaskById(int taskId);

    ArrayList<Task> getAllHistory();

    void removeAllEpic();

    void removeAllTask();

    void removeAllSub();

    void removeFromEpic(SubTask sub);

    List<Task> getPrioritizedTasks();

    boolean checkPriority(Task t1);


}
