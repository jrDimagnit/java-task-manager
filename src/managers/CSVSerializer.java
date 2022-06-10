package managers;

import tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVSerializer {

    static Task fromString(String value) {
        String[] taskArray = value.split(",");
        switch (taskArray[1]) {
            case ("TASK"):
                Task task = new Task(taskArray[2], taskArray[3],
                        StatusTask.valueOf(taskArray[4]));
                task.setIdNumber(Integer.parseInt(taskArray[0]));
                task.setStartTime(LocalDateTime.parse(taskArray[5]));
                task.setDuration(Integer.parseInt(taskArray[6]));
                task.setTypeTask(TypeTask.TASK);
                return task;
            case ("SUBTASK"):
                SubTask subTask = new SubTask(taskArray[2], taskArray[3],
                        Integer.parseInt(taskArray[7]), StatusTask.valueOf(taskArray[4]));
                subTask.setIdNumber(Integer.parseInt(taskArray[0]));
                subTask.setStartTime(LocalDateTime.parse(taskArray[5]));
                subTask.setDuration(Integer.parseInt(taskArray[6]));
                subTask.setTypeTask(TypeTask.SUBTASK);
                return subTask;
            case ("EPIC"):
                Epic epic = new Epic(taskArray[2], taskArray[3],
                        StatusTask.valueOf(taskArray[4]));
                epic.setIdNumber(Integer.parseInt(taskArray[0]));
                epic.setTypeTask(TypeTask.EPIC);
                return epic;
        }
        return null;
    }

    static String toStringHistory(HistoryManager manager) {
        StringBuilder historyTask = new StringBuilder();
        for (Task task : manager.getHistory()) {
            historyTask.append(task.getIdNumber());
            historyTask.append(",");
        }
        return historyTask.toString();
    }

    static List<Integer> fromStringHistory(String value) {
        List<Integer> taskList = new ArrayList<>();
        if (value != null) {
            String[] taskArray = value.split(",");
            for (String str : taskArray) {
                taskList.add(Integer.parseInt(str));
            }
        }
        return taskList;
    }
}
