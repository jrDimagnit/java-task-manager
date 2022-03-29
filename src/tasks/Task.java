package tasks;

public class Task {
    private int idNumber;
    private String nameTask;
    private String infoTask;
    private String statusTask;


    public Task(int idNumber, String nameTask, String infoTask, String statusTask) {
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

    public String getInfoTask() {
        return infoTask;
    }

    public String getStatusTask() {
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

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }

    @Override
    public String toString() {
        return "Номер: " + idNumber + ", название: " + nameTask + ", Информация: " + infoTask + ", Статус:" +
                statusTask;
    }
}
