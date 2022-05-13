package tasks;

public class Task {
    private int idNumber;
    private String nameTask;
    private String infoTask;

    private StatusTask statusTask;


    private TypeTask typeTask;

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

    @Override
    public String toString() {
        return idNumber + "," + typeTask + "," + nameTask + "," + infoTask + "," +
                statusTask;
    }
}
