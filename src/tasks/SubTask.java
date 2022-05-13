package tasks;

public class SubTask extends Task {
    private Integer mainEpic;

    public SubTask(int idNumber, String nameTask, String infoTask, int mainEpic, StatusTask statusTask) {
        super(idNumber, nameTask, infoTask, statusTask);
        this.mainEpic = mainEpic;
    }

    public Integer getMainEpic() {
        return mainEpic;
    }

    public void setMainEpic(int mainEpic) {
        this.mainEpic = mainEpic;
    }

    @Override
    public String toString() {
        return super.toString() + "," + mainEpic;
    }

}
