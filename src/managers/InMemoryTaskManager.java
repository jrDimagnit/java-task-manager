package managers;

import tasks.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int idSet = 0;
    protected HistoryManager inMemory;
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>((t1, t2) -> {
        if (t1.getStartTime().isAfter(t2.getStartTime()) || t1.getStartTime() == null) {
            return 1;
        } else if (t1.getStartTime().isBefore(t2.getStartTime()) || t2.getStartTime() == null) {
            return -1;
        } else {
            return 0;
        }
    });


    protected HashMap<Integer, Task> listTask = new HashMap<>();
    protected HashMap<Integer, SubTask> listSubTask = new HashMap<>();
    protected HashMap<Integer, Epic> listEpic = new HashMap<>();

    public InMemoryTaskManager(HistoryManager inMemory) {
        this.inMemory = inMemory;
    }

    @Override
    public int changeId() {
        return ++idSet;
    }

    @Override
    public void addTask(Task task) {
        if (checkPriority(task)) {
            task.setTypeTask(TypeTask.TASK);
            listTask.put(task.getIdNumber(), task);
        } else {
            System.out.println("Добавьте корректный промежуток времени исполнения задачи!");
        }
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (checkPriority(subTask)){
        if (listEpic.containsKey(subTask.getMainEpic())) {
            subTask.setTypeTask(TypeTask.SUBTASK);
            listSubTask.put(subTask.getIdNumber(), subTask);
            Epic epic = listEpic.get(subTask.getMainEpic());
            epic.getSubTasks().add(subTask);
            setStatusEpic(epic);
            epic.setDateAndDuration();
        } else {
            throw new ManagerSaveException("Epic Not Found");
        }
    }else{
            System.out.println("Добавьте корректный промежуток времени исполнения задачи!");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setTypeTask(TypeTask.EPIC);
        listEpic.put(epic.getIdNumber(), epic);
        epic.setDateAndDuration();
    }

    @Override
    public void removeAllEpic() {
        for (Epic epic : listEpic.values()) {
            inMemory.remove(epic.getIdNumber());
            for (SubTask subtask : listSubTask.values()) {
                if (epic.getIdNumber() == subtask.getMainEpic()) {
                    inMemory.remove(subtask.getIdNumber());
                    prioritizedTasks.remove(subtask);
                }
            }
        }
        listSubTask.clear();
        listEpic.clear();
    }

    @Override
    public void removeAllTask() {
        for (Task task : listTask.values()) {
            inMemory.remove(task.getIdNumber());
            prioritizedTasks.remove(task);
        }
        listTask.clear();
    }

    @Override
    public void removeAllSub() {
        for (SubTask subTask : listSubTask.values()) {
            inMemory.remove(subTask.getIdNumber());
            prioritizedTasks.remove(subTask);
            Epic epic = listEpic.get(subTask.getMainEpic());
            epic.getSubTasks().clear();
            setStatusEpic(epic);
            epic.setDateAndDuration();
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
        if(listEpic.containsKey(taskId)) {
            Epic epic = listEpic.get(taskId);
            return epic.getSubTasks();
        }else{
            throw new ManagerSaveException("Epic Not Found!");
        }
    }

    @Override
    public void removeIdTask(int idTask) {
        if(listTask.containsKey(idTask)){
        inMemory.remove(idTask);
        prioritizedTasks.remove(getTaskById(idTask));
        listTask.remove(idTask);
    }else{
        throw new ManagerSaveException("Task Not Found!");
        }
    }

    @Override
    public void removeIdSub(int idTask) {
        if(listSubTask.containsKey(idTask)){
        SubTask subTask = listSubTask.get(idTask);
        removeFromEpic(subTask);
        inMemory.remove(idTask);
        prioritizedTasks.remove(listSubTask.get(idTask));
        listSubTask.remove(idTask);
    }else{
            throw new ManagerSaveException("SubTask Not Found!");
        }
    }

    @Override
    public void removeIdEpic(int idTask) {
        if(listEpic.containsKey(idTask)){
        Epic epic = listEpic.get(idTask);
        ArrayList<SubTask> obj = epic.getSubTasks();
        for (SubTask subTask : obj) {
            prioritizedTasks.remove(listSubTask.get(idTask));
            listSubTask.remove(subTask.getIdNumber());
            inMemory.remove(subTask.getIdNumber());
        }
        inMemory.remove(idTask);
        listEpic.remove(idTask);
    }else{
        throw new ManagerSaveException("Epic Not Found!");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (listTask.containsKey(task.getIdNumber())) {
            Task task1 = listTask.get(task.getIdNumber());
            prioritizedTasks.remove(listTask.get(task.getIdNumber()));
            if (checkPriority(task)) {
                listTask.put(task.getIdNumber(), task);
            } else {
                prioritizedTasks.add(task1);
            }
        } else {
            throw new ManagerSaveException("Task Not Found!");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (listSubTask.containsKey(subTask.getIdNumber())) {
            SubTask subTask1 = listSubTask.get(subTask.getIdNumber());
            prioritizedTasks.remove(listSubTask.get(subTask.getIdNumber()));
            if (checkPriority(subTask)) {
                    Epic epic = listEpic.get(subTask.getMainEpic());
                    removeFromEpic(subTask);
                    epic.getSubTasks().add(subTask);
                    setStatusEpic(epic);
                    epic.setDateAndDuration();
                    listSubTask.put(subTask.getIdNumber(), subTask);
            } else {
                prioritizedTasks.add(subTask1);
            }
        } else {
            throw new ManagerSaveException("SubTask Not Found!");
        }
    }

    @Override
    public Epic getEpicById(int taskId) {
        if(listEpic.containsKey(taskId)){
            Epic epic = listEpic.get(taskId);
            inMemory.addHistory(epic);
            return epic;
        }else{
            throw new ManagerSaveException("Epic Not Found!");
        }

    }

    @Override
    public SubTask getSubById(int taskId) {
        if(listSubTask.containsKey(taskId)){
            SubTask subTask = listSubTask.get(taskId);
            inMemory.addHistory(subTask);
            return subTask;
        }else{
            throw new ManagerSaveException("SubTask Not Found!");
        }
    }

    @Override
    public Task getTaskById(int taskId) {
        if(listTask.containsKey(taskId)){
            Task task = listTask.get(taskId);
            inMemory.addHistory(task);
            return task;
        }else {
            throw new ManagerSaveException("Task Not Found!");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (listEpic.containsKey(epic.getIdNumber())) {
            epic.setDateAndDuration();
            listEpic.put(epic.getIdNumber(), epic);
        } else {
            throw new ManagerSaveException("Epic Not Found!");
        }
    }

    @Override
    public void removeFromEpic(SubTask sub) {
        if (listSubTask.containsKey(sub.getIdNumber())) {
            Epic epic = listEpic.get(sub.getMainEpic());
            ArrayList<SubTask> obj = epic.getSubTasks();
            if(obj.contains(sub)) {
                obj.remove(sub);
                if (inMemory.getHistory().contains(sub)) {
                    inMemory.remove(sub.getIdNumber());
                }
                epic.setSubTasks(obj);
                setStatusEpic(epic);
                epic.setDateAndDuration();
            }
        } else {
            throw new ManagerSaveException("SubTask Not Found!");
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
    public ArrayList<Task> getAllHistory() {
        return (ArrayList<Task>) inMemory.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> check = new ArrayList<>();
        for(Task task : prioritizedTasks) {
            check.add(task);
        }
        return check;
    }

    @Override
    public boolean checkPriority(Task t1){
        try {
            if(prioritizedTasks.isEmpty()) {
                prioritizedTasks.add(t1);
                return true;
            }else{
                for(Task t2 : prioritizedTasks){
                if (t1.getStartTime().isBefore(t2.getEndTime()) && t1.getEndTime().isAfter(t2.getStartTime()) ||
                        t2.getStartTime().isBefore(t1.getEndTime()) && t2.getEndTime().isAfter(t1.getStartTime()) ||
                t1.getStartTime().isBefore(t2.getStartTime()) && t1.getEndTime().isAfter(t2.getEndTime()) ||
                        t2.getStartTime().isBefore(t1.getStartTime()) && t2.getEndTime().isAfter(t1.getEndTime())){
                    throw new ManagerSaveException("Ошибка пересечения времени задачи!");
                }
                }
            }prioritizedTasks.add(t1);
                return true;
        }catch (ManagerSaveException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

}
