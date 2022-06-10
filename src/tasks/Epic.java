package tasks;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<SubTask> subTasks;
    private LocalDateTime endTime;

    public Epic(String nameTask, String info, StatusTask statusTask) {
        super(nameTask, info, statusTask);
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void setDuration() {
        int minutesDuration = 0;
        if (!subTasks.isEmpty()) {
            for (SubTask subTask : getSubTasks()) {
                minutesDuration += subTask.getDuration();
            }
        }
        super.duration = minutesDuration;
    }

    public void setStartTime() {
        if (subTasks == null || subTasks.isEmpty()) {
            startTime = null;
        } else {
            LocalDateTime check = startTime;
            for (SubTask subTask : getSubTasks()) {
                if (subTask.getStartTime() == null) {

                } else if (startTime == null) {
                    startTime = subTask.getStartTime();
                    check = startTime;
                } else if (subTask.getStartTime().isBefore(startTime)) {
                    check = subTask.getStartTime();
                }
            }
            startTime = check;
        }
    }

    public void setDateAndDuration() {
        setStartTime();
        if (startTime == null) {
            endTime = null;
        } else {
            setDuration();
            endTime = startTime.plus(Duration.ofMinutes(duration));
        }
    }

    public ArrayList<SubTask> getSubTasks() {
        if (subTasks == null) {
            subTasks = new ArrayList<>();
        }
        return subTasks;
    }

}
