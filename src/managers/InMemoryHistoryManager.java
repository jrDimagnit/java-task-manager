package managers;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    @Override
    public void addHistory(Task task) {
        history.add(task);
        updateHistory();
    }

    private void updateHistory() {
        if (history.size() > 10) {
            int i = history.size() - 10;
            for (int j = 0; j < i; j++) {
                history.remove(j);
            }
        }
    }
}
