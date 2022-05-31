package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int idNumber;
    private String nameTask;
    private String infoTask;
    private StatusTask statusTask;

    protected LocalDateTime startTime;
    protected Duration duration;

    private TypeTask typeTask;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(int idNumber, String nameTask, String infoTask, StatusTask statusTask) {
        this.idNumber = idNumber;
        this.nameTask = nameTask;
        this.infoTask = infoTask;
        this.statusTask = statusTask;
    }

    public int getIdNumber() {
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

    public LocalDateTime getEndTime(){
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        if(duration == null){
            return Duration.ofMinutes(0);
        }
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public void setUpDateAndDuration(LocalDateTime dateTime,Duration duration){
        this.startTime = dateTime;
        this.duration = duration;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    @Override
    public String toString() {
        return idNumber + "," + typeTask + "," + nameTask + "," + infoTask + "," +
                statusTask + "," + startTime.format(formatter) + "," + duration.toMinutes();
    }
}
