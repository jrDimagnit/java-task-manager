package tasks;

public class SubTask extends Task {
    private Integer mainEpic;

    public SubTask(String nameTask, String infoTask, int mainEpic, StatusTask statusTask) {
        super(nameTask, infoTask, statusTask);
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
