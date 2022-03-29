package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private int idSet = 0;
    private HashMap<Integer, Task> listTask = new HashMap<>();
    private HashMap<Integer, SubTask> listSubTask = new HashMap<>();
    private HashMap<Integer, Epic> listEpic = new HashMap<>();

    public int changeId() {
        return ++idSet;
    }

    public void addTask(Task task) {
        listTask.put(task.getIdNumber(), task);
    }

    public void addSubTask(SubTask subTask) {
        listSubTask.put(subTask.getIdNumber(), subTask);
        Epic epic = listEpic.get(subTask.getMainEpic());
        ArrayList<SubTask> reWrite;
        reWrite = epic.getSubTasks();
        reWrite.add(subTask);
        epic.setSubTasks(reWrite);
        setStatusEpic(epic);
    }

    public void addEpic(Epic epic) {
        listEpic.put(epic.getIdNumber(), epic);
    }

    public HashMap<Integer, Task> getAllTask() {
        return listTask;
    }

    public HashMap<Integer, SubTask> getAllSubTask() {
        return listSubTask;
    }

    public HashMap<Integer, Epic> getAllEpic() {
        return listEpic;
    }

    public ArrayList<SubTask> subInEpic(int taskId) {
        Epic epic = listEpic.get(taskId);
        ArrayList<SubTask> subTasks = epic.getSubTasks();
        return subTasks;
    }

    public void removeAll() {
        listTask.clear();
        listSubTask.clear();
        listEpic.clear();
    }

    public void removeId(int idTask) {
        if (listTask.get(idTask) != null) {
            listTask.remove(idTask);
        } else if (listSubTask.get(idTask) != null) {
            SubTask subTask = listSubTask.get(idTask);
            removeFromEpic(subTask);
            listSubTask.remove(idTask);
        } else if (listEpic.get(idTask) != null) {
            Epic epic = listEpic.get(idTask);
            ArrayList<SubTask> obj = epic.getSubTasks();
            for (SubTask subTask : obj) {
                listSubTask.remove(subTask.getIdNumber());
            }
            listEpic.remove(idTask);
        }
    }

    public String getStatus(int taskId) {
        String status = "";
        if (listTask.get(taskId) != null) {
            Task task = listTask.get(taskId);
            status =  task.getStatusTask();
        } else if (listSubTask.get(taskId) != null) {
            SubTask subTask = listSubTask.get(taskId);
            status = subTask.getStatusTask();
        } else if (listEpic.get(taskId) != null) {
            Epic epic = listEpic.get(taskId);
            epic.setStatusTask(setStatusEpic(epic));
            status = epic.getStatusTask();
        }
        return status;
    }

    public void updateTask(Task task) {
        removeId(task.getIdNumber());
        listTask.put(task.getIdNumber(), task);
    }

    public void updateSubTask(SubTask subTask) {
        removeId(subTask.getIdNumber());
        Epic epic = listEpic.get(subTask.getMainEpic());
        ArrayList<SubTask> reWrite;
        reWrite = epic.getSubTasks();
        reWrite.add(subTask);
        epic.setSubTasks(reWrite);
        epic.setStatusTask(setStatusEpic(epic));
        listSubTask.put(subTask.getIdNumber(), subTask);
    }

    public Epic getEpicById(int taskId) {
        return listEpic.get(taskId);
    }

    public SubTask getSubById(int taskId) {
        return listSubTask.get(taskId);
    }

    public Task getTaskById(int taskId) {
        return listTask.get(taskId);
    }

    public void updateEpic(Epic epic) {
        removeId(epic.getIdNumber());
        epic.setStatusTask(setStatusEpic(epic));
        listEpic.put(epic.getIdNumber(), epic);
    }

    public void removeFromEpic(SubTask subtask) {
        Epic epic = getEpicById(subtask.getMainEpic());
        ArrayList<SubTask> obj = epic.getSubTasks();
        for (SubTask subTask : obj) {
            if (subTask.getIdNumber() == subTask.getIdNumber()) {
                obj.remove(subTask);
                epic.setSubTasks(obj);
                epic.setStatusTask(setStatusEpic(epic));
                break;
            }
        }
    }

    private String setStatusEpic(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;
        for (SubTask subTask : epic.getSubTasks()) {
            switch (subTask.getStatusTask()) {
                case "NEW":
                    ++statusNew;
                    break;
                case "DONE":
                    ++statusDone;
                    break;
                default:
                    break;
            }
        }
        if (statusNew == epic.getSubTasks().size()) {
            return "NEW";
        } else if (statusDone == epic.getSubTasks().size()) {
            return "DONE";
        } else {
            return "IN_PROGRESS";
        }
    }
}
