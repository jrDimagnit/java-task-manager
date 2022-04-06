package managers;

import tasks.Epic;
import tasks.StatusTask;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int idSet = 0;
    HistoryManager inMemory = Managers.getDefaultHistory();
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
        listSubTask.put(subTask.getIdNumber(), subTask);
        Epic epic = listEpic.get(subTask.getMainEpic());
        ArrayList<SubTask> reWrite;
        reWrite = epic.getSubTasks();
        reWrite.add(subTask);
        epic.setSubTasks(reWrite);
        setStatusEpic(epic);
    }

    @Override
    public void addEpic(Epic epic) {
        listEpic.put(epic.getIdNumber(), epic);
    }

    @Override
    public void removeAll() {
        listTask.clear();
        listSubTask.clear();
        listEpic.clear();
    }

    @Override
    public HashMap<Integer, Task> getAllTask() {
        return listTask;
    }

    @Override
    public HashMap<Integer, SubTask> getAllSubTask() {
        return listSubTask;
    }

    @Override
    public HashMap<Integer, Epic> getAllEpic() {
        return listEpic;
    }

    @Override
    public ArrayList<SubTask> subInEpic(int taskId) {
        Epic epic = listEpic.get(taskId);
        ArrayList<SubTask> subTasks = epic.getSubTasks();
        return subTasks;
    }

    @Override
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

    @Override
    public String getStatus(int taskId) {
        StatusTask tasks = StatusTask.NEW;
        if (listTask.get(taskId) != null) {
            Task task = listTask.get(taskId);
            tasks = task.getStatusTask();
        } else if (listSubTask.get(taskId) != null) {
            SubTask subTask = listSubTask.get(taskId);
            tasks = subTask.getStatusTask();
        } else if (listEpic.get(taskId) != null) {
            Epic epic = listEpic.get(taskId);
            epic.setStatusTask(setStatusEpic(epic));
            tasks = epic.getStatusTask();
        }
        return String.valueOf(tasks);
    }

    @Override
    public void updateTask(Task task) {
        removeId(task.getIdNumber());
        listTask.put(task.getIdNumber(), task);
    }

    @Override
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

    @Override
    public Epic getEpicById(int taskId) {
        inMemory.addHistory(listEpic.get(taskId));
        return listEpic.get(taskId);
    }

    @Override
    public SubTask getSubById(int taskId) {
        inMemory.addHistory(listSubTask.get(taskId));
        return listSubTask.get(taskId);
    }

    @Override
    public Task getTaskById(int taskId) {
        inMemory.addHistory(listTask.get(taskId));
        return listTask.get(taskId);
    }

    @Override
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

    private StatusTask setStatusEpic(Epic epic) {
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
            return StatusTask.NEW;
        } else if (statusDone == epic.getSubTasks().size()) {
            return StatusTask.DONE;
        } else {
            return StatusTask.IN_PROGRESS;
        }
    }

    @Override
    public void getAllHistory() {
        System.out.println(inMemory.getHistory());

    }
}
