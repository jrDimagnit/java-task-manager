package Tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    Task task;
    SubTask subTask;
    Epic epic;
    int idSet = 0;
    HashMap <Integer, Task> listTask = new HashMap<>();
    HashMap <Integer, SubTask> listSubTask = new HashMap<>();
    HashMap <Integer, Epic> listEpic = new HashMap<>();

    public int changeId() {
        return ++idSet;
    }

    public void addTask(Task task){
        task.setIdNumber(task.getIdNumber());
        task.setNameTask(task.getNameTask());
        task.setInfoTask(task.getInfoTask());
        task.setStatusTask("NEW");
        listTask.put(task.getIdNumber(), task);
    }

    public void addSubTask(SubTask subTask){
        subTask.setIdNumber(subTask.getIdNumber());
        subTask.setNameTask(subTask.getNameTask());
        subTask.setInfoTask(subTask.getInfoTask());
        subTask.setMainEpic(subTask.mainEpic);
        subTask.setStatusTask("NEW");
        listSubTask.put(subTask.getIdNumber(),subTask);
        Epic epic = listEpic.get(subTask.getMainEpic());
        ArrayList<SubTask> reWrite;
        reWrite = epic.getSubTasks();
        reWrite.add(subTask);
        epic.setSubTasks(reWrite);
    }

    public void addEpic(Epic epic){
        epic.setIdNumber(epic.getIdNumber());
        epic.setNameTask(epic.getNameTask());
        epic.setInfoTask(epic.getInfoTask());
        epic.setSubTasks(new ArrayList<>());
        epic.setStatusTask(setStatusEpic(epic));
        listEpic.put(epic.getIdNumber(),epic);
    }

    public void printTask(){
        for (Object task : listTask.values()){
            System.out.println(task.toString());
        }
    }

    public void printSubTask(){
        for (Object task : listSubTask.values()){
            System.out.println(task.toString());
        }
    }

    public void printEpic(){
        for (Object task : listEpic.values()){
            System.out.println(task.toString());
        }
    }

    public void printAll(){
        printTask();
        printEpic();
        printSubTask();
    }

    public void removeAll(){
        listTask.clear();
        listSubTask.clear();
        listEpic.clear();
    }

    public void removeId(int idTask){
        if (listTask.get(idTask) != null){
            listTask.remove(idTask);
        }else if (listSubTask.get(idTask) != null) {
            SubTask subTask = listSubTask.get(idTask);
            removeFromEpic(listEpic.get(subTask.getMainEpic()), idTask);
            listSubTask.remove(idTask);
        }else if (listEpic.get(idTask) != null){
            Epic epic = listEpic.get(idTask);
            ArrayList<SubTask> obj = epic.getSubTasks();
            for ( SubTask subTask : obj){
                    listSubTask.remove(subTask.getIdNumber());
                }
            listEpic.remove(idTask);
        }
    }

    public Object getTaskById(int taskId){
        if (listTask.get(taskId) != null){
            return listTask.get(taskId);
        }else if (listSubTask.get(taskId) != null){
            return listSubTask.get(taskId);
        }else if (listEpic.get(taskId) != null){
            return listEpic.get(taskId);
        } return null;
    }


    public void getStatus(int taskId){
        if (listTask.get(taskId) != null){
            System.out.println(task.getStatusTask());
        }else if (listSubTask.get(taskId) != null){
            System.out.println(subTask.getStatusTask());
        }else if (listEpic.get(taskId) != null){
            Epic epic = listEpic.get(taskId);
            epic.setStatusTask(setStatusEpic(epic));
            System.out.println(epic.getStatusTask());
        }
    }

    public void subInEpic(int taskId){
        Epic epic = listEpic.get(taskId);
        for(SubTask subTask : epic.getSubTasks()){
            System.out.println(subTask.toString());
        }
    }

    public void updateTask(Task task){
        removeId(task.getIdNumber());
        task.setIdNumber(task.getIdNumber());
        task.setNameTask(task.getNameTask());
        task.setInfoTask(task.getInfoTask());
        task.setStatusTask(task.getStatusTask());
        listTask.put(task.getIdNumber(), task);
    }

    public void updateSubTask(SubTask subTask){
        removeId(subTask.getIdNumber());
        // removeFromEpic(listEpic.get(subTask.getMainEpic()),subTask.getIdNumber());
        subTask.setIdNumber(subTask.getIdNumber());
        subTask.setNameTask(subTask.getNameTask());
        subTask.setInfoTask(subTask.getInfoTask());
        subTask.setStatusTask(subTask.getStatusTask());
        Epic epic = listEpic.get(subTask.getMainEpic());
        ArrayList<SubTask> reWrite;
        reWrite = epic.getSubTasks();
        reWrite.add(subTask);
        epic.setSubTasks(reWrite);
        epic.setStatusTask(setStatusEpic(epic));
        listSubTask.put(subTask.getIdNumber(),subTask);
    }

    public void updateEpic(Epic epic){
        removeId(epic.getIdNumber());
        epic.setIdNumber(epic.getIdNumber());
        epic.setNameTask(epic.getNameTask());
        epic.setInfoTask(epic.getInfoTask());
        epic.setSubTasks(epic.getSubTasks());
        epic.setStatusTask(setStatusEpic(epic));
        listEpic.put(epic.getIdNumber(),epic);
    }

    public void removeFromEpic(Epic epic, int idTask){
        ArrayList<SubTask> obj = epic.getSubTasks();
        for (SubTask subTask : obj){
            if(subTask.getIdNumber() == idTask) {
                obj.remove(subTask);
                epic.setSubTasks(obj);
                epic.setStatusTask(setStatusEpic(epic));
                break;
            }
        }
    }

    public String setStatusEpic(Epic epic){
        int statusNew = 0;
        int statusDone = 0;
        for (SubTask subTask : epic.getSubTasks()){
            switch (subTask.getStatusTask()){
                case "NEW":
                    ++statusNew;
                    break;
                case "DONE":
                    ++statusDone;
                    break;
                default:
                    break;
            }
        }if (statusNew == epic.getSubTasks().size()){
            return  "NEW";
        }else if (statusDone == epic.getSubTasks().size()){
            return "DONE";
        }else{
            return "IN_PROGRESS";
        }
    }

}
