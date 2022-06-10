package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private Integer idNumber;
    private String nameTask;
    private String infoTask;
    private StatusTask statusTask;

    protected LocalDateTime startTime;
    protected Integer duration;

    private TypeTask typeTask;

    public Task(String nameTask, String infoTask, StatusTask statusTask) {
        this.nameTask = nameTask;
        this.infoTask = infoTask;
        this.statusTask = statusTask;
    }

    public Integer getIdNumber() {
        return idNumber;
    }

    public String getNameTask() {
        return nameTask;
    }

    public TypeTask getTypeTask() {
        return typeTask;
    }

    public void setTypeTask(TypeTask typeTask) {
        this.typeTask = typeTask;
    }

    public String getInfoTask() {
        return infoTask;
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public void setInfoTask(String infoTask) {
        this.infoTask = infoTask;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null && duration == null) {
            return null;
        }
        return startTime.plus(Duration.ofMinutes(duration));
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setUpDateAndDuration(LocalDateTime dateTime, Integer duration) {
        this.startTime = dateTime;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return idNumber + "," + typeTask + "," + nameTask + "," + infoTask + "," +
                statusTask + "," + startTime + "," + duration;
    }
}
