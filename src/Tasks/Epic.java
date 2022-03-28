package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;
    private String statusTask;

    public Epic(int idEpic, String nameTask, String info, String statusTask) {
        super(idEpic, nameTask, info, statusTask);
    }


    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String toString() {
        return "Номер эпика: " + getIdNumber() + ", Имя Эпика: " + getNameTask() + ", Статус:" +
                 statusTask + ", Подзадачи:" + getSubTasks();
    }

    public String getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }

    public ArrayList<SubTask> getSubTasks() {
          return subTasks;
        }

}
