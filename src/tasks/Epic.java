package tasks;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<SubTask> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

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

    public void setDuration() {
        long minutesDuration = 0;
        if (!subTasks.isEmpty()) {
            for (SubTask subTask : getSubTasks()) {
                minutesDuration += subTask.getDuration().toMinutes();
            }
        }
        super.duration = Duration.ofMinutes(minutesDuration);
    }

    public void setStartTime() {
        if (subTasks.isEmpty()) {
            startTime = LocalDateTime.of(2030, 1, 1, 0, 0);
        } else {
            LocalDateTime check = startTime;
            for (SubTask subTask : getSubTasks()) {
                if (subTask.getStartTime().isBefore(startTime)) {
                    check = subTask.getStartTime();
                }
            }
            startTime = check;
        }
    }

    public void setDateAndDuration() {
        setStartTime();
        setDuration();
        endTime = startTime.plus(duration);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

}
