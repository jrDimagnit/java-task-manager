package managers;

import tasks.Epic;
import tasks.StatusTask;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int idSet = 0;
    private HistoryManager inMemory = Managers.getDefaultHistory();
    private HashMap<Integer, Task> listTask = new HashMap<>();
    private HashMap<Integer, SubTask> listSubTask = new HashMap<>();
    private HashMap<Integer, Epic> listEpic = new HashMap<>();

    @Override
    public int changeId() {
        return ++idSet;
    }

    @Override
    public void addTask(Task task) {
        listTask.put(task.getIdNumber(), task);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (!listEpic.containsKey(subTask.getIdNumber())) {
            listSubTask.put(subTask.getIdNumber(), subTask);
            Epic epic = listEpic.get(subTask.getMainEpic());
            epic.getSubTasks().add(subTask);
            setStatusEpic(epic);
        } else {
            System.out.println("Id not found");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        listEpic.put(epic.getIdNumber(), epic);
    }

    public void removeAllEpic() {
        listSubTask.clear();
        listEpic.clear();
    }

    public void removeAllTask() {
        listTask.clear();
    }

    public void removeAllSub() {
        for (SubTask subTask : listSubTask.values()) {
            Epic epic = listEpic.get(subTask.getMainEpic());
            epic.getSubTasks().clear();
            setStatusEpic(epic);
        }
        listSubTask.clear();
    }

    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(listTask.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(listSubTask.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(listEpic.values());
    }

    @Override
    public ArrayList<SubTask> subInEpic(int taskId) {
        Epic epic = listEpic.get(taskId);
        return epic.getSubTasks();
    }

    @Override
    public void removeIdTask(int idTask) {
        listTask.remove(idTask);
    }

    @Override
    public void removeIdSub(int idTask) {
        SubTask subTask = listSubTask.get(idTask);
        removeFromEpic(subTask);
        listSubTask.remove(idTask);
    }

    @Override
    public void removeIdEpic(int idTask) {
        Epic epic = listEpic.get(idTask);
        ArrayList<SubTask> obj = epic.getSubTasks();
        for (SubTask subTask : obj) {
            listSubTask.remove(subTask.getIdNumber());
        }
        listEpic.remove(idTask);
    }

    @Override
    public void updateTask(Task task) {
        if (listTask.containsKey(task.getIdNumber())) {
            listTask.put(task.getIdNumber(), task);
        } else {
            System.out.println("Id not found");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (listSubTask.containsKey(subTask.getIdNumber())) {
            Epic epic = listEpic.get(subTask.getMainEpic());
            epic.getSubTasks().add(subTask);
            setStatusEpic(epic);
            listSubTask.put(subTask.getIdNumber(), subTask);
        } else {
            System.out.println("Id not found");
        }
    }

    @Override
    public Epic getEpicById(int taskId) {
        if (listEpic.containsKey(taskId)) {
            inMemory.addHistory(listEpic.get(taskId));
        }
        return listEpic.get(taskId);
    }

    @Override
    public SubTask getSubById(int taskId) {
        if (listSubTask.containsKey(taskId)) {
            inMemory.addHistory(listSubTask.get(taskId));
        }
        return listSubTask.get(taskId);
    }

    @Override
    public Task getTaskById(int taskId) {
        if (listTask.containsKey(taskId)) {
            inMemory.addHistory(listTask.get(taskId));
        }
        return listTask.get(taskId);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (listEpic.containsKey(epic.getIdNumber())) {
            setStatusEpic(epic);
            listEpic.put(epic.getIdNumber(), epic);
        } else {
            System.out.println("Id not found");
        }
    }

    public void removeFromEpic(SubTask subtask) {
        Epic epic = getEpicById(subtask.getMainEpic());
        ArrayList<SubTask> obj = epic.getSubTasks();
        for (SubTask subTask : obj) {
            if (subTask.getIdNumber() == subTask.getIdNumber()) {
                obj.remove(subTask);
                epic.setSubTasks(obj);
                setStatusEpic(epic);
                break;
            }
        }
    }

    private void setStatusEpic(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;
        for (SubTask subTask : epic.getSubTasks()) {
            if (subTask.getStatusTask() == StatusTask.NEW) {
                ++statusNew;
            } else if (subTask.getStatusTask() == StatusTask.DONE) {
                ++statusDone;
            }
        }
        if (statusNew == epic.getSubTasks().size()) {
            epic.setStatusTask(StatusTask.NEW);
        } else if (statusDone == epic.getSubTasks().size()) {
            epic.setStatusTask(StatusTask.DONE);
        } else {
            epic.setStatusTask(StatusTask.IN_PROGRESS);
        }
    }

    @Override
    public void getAllHistory() {
        System.out.println(inMemory.getHistory());

    }
}
