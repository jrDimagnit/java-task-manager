package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(int idEpic, String nameTask, String info, StatusTask statusTask) {
        super(idEpic, nameTask, info, statusTask);
    }


    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String toString() {
        return super.toString();
    }


    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

}
