package managers;

import tasks.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends  InMemoryTaskManager {

    protected String filePath;
    static int historyId = 1;

    public FileBackedTasksManager(HistoryManager inMemory, String filePath) {
        super(inMemory);
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public void setIdSet(){
        super.idSet = historyId;
    }
    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllSub() {
        super.removeAllSub();
        save();
    }

    @Override
    public void removeIdTask(int idTask) {
        super.removeIdTask(idTask);
        save();
    }

    @Override
    public void removeIdSub(int idTask) {
        super.removeIdSub(idTask);
        save();
    }

    @Override
    public void removeIdEpic(int idTask) {
        super.removeIdEpic(idTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public Epic getEpicById(int taskId) {
        Epic epic = super.getEpicById(taskId);
        save();
        return epic;
    }

    @Override
    public SubTask getSubById(int taskId) {
        SubTask subTask = super.getSubById(taskId);
        save();
        return subTask;
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeFromEpic(SubTask sub) {
        super.removeFromEpic(sub);
        save();
    }

    private void save() {
        if (getFilePath() == null) {
            Path taskPath = Paths.get("src", "task.csv");
            setFilePath(String.valueOf(taskPath));
        }
        try (Writer fileWriter = new FileWriter(filePath)) {
            BufferedWriter br = new BufferedWriter(fileWriter);
            for (Task task : getAllTask()) {
                br.write(task.toString()+"\n");
            }
            for (Epic epic : getAllEpic()) {
                br.write(epic.toString()+"\n");
            }
            for (SubTask subTask : getAllSubTask()) {
                br.write(subTask.toString()+"\n");
            }
            br.write("\n");
            br.write(CSVSerializer.toStringHistory(inMemory));
            br.close();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    static FileBackedTasksManager loadFromFile(String filePath) {
        FileBackedTasksManager fileBack = new FileBackedTasksManager(Managers.getDefaultHistory(), filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (br.ready()) {
                String str = br.readLine();
                if (str.isEmpty()) {
                    List<Integer> listHistory= new ArrayList<>(CSVSerializer.fromStringHistory(br.readLine()));
                    for (int j : listHistory) {
                        if (fileBack.listTask.containsKey(j)) {
                            fileBack.getTaskById(j);
                        } else if (fileBack.listSubTask.containsKey(j)) {
                            fileBack.getSubById(j);
                        } else {
                            fileBack.getEpicById(j);
                        }
                    }
                } else {
                    Task task = CSVSerializer.fromString(str);
                    switch(task.getTypeTask()){
                        case TASK:
                            fileBack.listTask.put(task.getIdNumber(), task);
                            break;
                        case SUBTASK:
                            SubTask subtask = (SubTask) task;
                            fileBack.listSubTask.put(task.getIdNumber(), subtask);
                            break;
                        case EPIC:
                            Epic epic = (Epic) task;
                            fileBack.listEpic.put(task.getIdNumber(), epic);
                            break;
                    }
                    historyId++;
                }
            }br.close();
            fileBack.setIdSet();
            return fileBack;
        }
        catch(Exception e){
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static void main(String[] args)  {
        FileBackedTasksManager fbtm = loadFromFile(String.valueOf(Path.of("src", "task.csv")));
        //FileBackedTasksManager fbtm = new FileBackedTasksManager(Managers.getDefaultHistory(),"src\\task.csv");

        fbtm.getAllHistory();

    }
}


