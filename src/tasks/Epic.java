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

    @Override
    public Duration getDuration() {
        long minutesDuration = 0;
        if (!subTasks.isEmpty()) {
            for (SubTask subTask : getSubTasks()) {
                minutesDuration += subTask.getDuration().toMinutes();
            }
            }
            return Duration.ofMinutes(minutesDuration);
        }


    @Override
    public LocalDateTime getStartTime() {
        if (subTasks.isEmpty()) {
            return LocalDateTime.of(2030, 1, 1, 0, 0);
        } else {
            LocalDateTime check = startTime;
            for (SubTask subTask : getSubTasks()) {
                if(subTask.getStartTime().isBefore(startTime)) {
                    check = subTask.getStartTime();
                }
            }return check;
        }
    }
    public void setDateAndDuration(){
        super.startTime = getStartTime();
        super.duration = getDuration();
        endTime = super.startTime.plus(super.duration);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

}
