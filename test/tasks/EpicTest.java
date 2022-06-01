package tasks;

import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private InMemoryTaskManager manager;
    Epic epic;
    SubTask subTask;
    SubTask subTask1;

    @BeforeEach
    void init() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        epic = new Epic(manager.changeId(), "Э1", "И", StatusTask.NEW);
        subTask = new SubTask(manager.changeId(), "С1", "I",
                1, StatusTask.NEW);
        subTask.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 5, 0),
                Duration.ofMinutes(60));
        subTask1 = new SubTask(manager.changeId(), "С2", "I",
                1, StatusTask.NEW);
        subTask1.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 10, 0),
                Duration.ofMinutes(60));
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);

    }

    @Test
    void checkStatusEpicWithoutSub() {
        manager.removeAllSub();
        assertEquals(StatusTask.NEW, epic.getStatusTask());
    }

    @Test
    void checkStatusEpicWithNewSub() {
        assertEquals(StatusTask.NEW, epic.getStatusTask());
    }

    @Test
    void checkStatusEpicWithDoneSub() {
        subTask.setStatusTask(StatusTask.DONE);
        subTask1.setStatusTask(StatusTask.DONE);
        manager.updateSubTask(subTask);
        manager.updateSubTask(subTask1);
        assertEquals(StatusTask.DONE, epic.getStatusTask());
    }

    @Test
    void checkStatusEpicWithDoneAndNewSub() {
        SubTask subTask3 = new SubTask(3, "С1", "I",
                1, StatusTask.DONE);
        subTask3.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 10, 0),
                Duration.ofMinutes(60));
        manager.updateSubTask(subTask3);
        assertEquals(StatusTask.IN_PROGRESS, epic.getStatusTask());
        assertEquals(epic.getDuration(), Duration.ofMinutes(120));
        assertEquals(epic.getStartTime(), LocalDateTime.of(2022, 5, 1, 5, 0));
    }

    @Test
    void checkStatusEpicWithAllInProgressSub() {
        SubTask subTask2 = new SubTask(2, "С1", "I",
                1, StatusTask.IN_PROGRESS);
        subTask2.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 5, 0),
                Duration.ofMinutes(60));
        SubTask subTask3 = new SubTask(3, "С1", "I",
                1, StatusTask.IN_PROGRESS);
        subTask3.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 10, 0),
                Duration.ofMinutes(60));
        manager.updateSubTask(subTask2);
        manager.updateSubTask(subTask3);
        assertEquals(StatusTask.IN_PROGRESS, epic.getStatusTask());
    }
}