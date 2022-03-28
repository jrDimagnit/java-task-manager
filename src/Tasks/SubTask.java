package Tasks;

public class SubTask extends Task {
    Integer mainEpic;

    public SubTask(int idNumber, String nameTask, String infoTask, int mainEpic, String statusTask) {
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
        return super.toString() + ", Новет эпика: " + mainEpic;
    }

}
