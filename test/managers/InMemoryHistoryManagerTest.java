package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.StatusTask;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager manager;
    Task task;
    Task task1;
    SubTask subTask;
    Epic epic;
    Epic epic1;
    SubTask subTask1;

    @BeforeEach
    void init() {
        manager = new InMemoryHistoryManager();
        epic = new Epic(1, "Э1", "И", StatusTask.NEW);
        subTask = new SubTask(2, "С1", "I",
                1, StatusTask.NEW);
        subTask.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 5, 0),
                Duration.ofMinutes(60));
        epic1 = new Epic(3, "Э2", "И", StatusTask.NEW);
        task = new Task(4, "T1", " И", StatusTask.NEW);
        task.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 0, 0),
                Duration.ofMinutes(60));
        task1 = new Task(5, "Т2", " И", StatusTask.NEW);
        task1.setUpDateAndDuration(LocalDateTime.of(2022, 6, 1, 10, 0),
                Duration.ofMinutes(60));
        subTask1 = new SubTask(6, "С2", "I",
                1, StatusTask.NEW);
        subTask1.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 10, 0),
                Duration.ofMinutes(60));
    }

    @Test
    void getHistoryNormalTest() {
        manager.addHistory(task);
        manager.addHistory(task1);
        manager.addHistory(subTask);
        manager.addHistory(subTask1);
        assertEquals(4, manager.getHistory().size());
    }

    @Test
    void getHistoryEmptyTest() {
        assertEquals(0, manager.getHistory().size());
    }

    @Test
    void addHistoryNormalTest() {
        manager.addHistory(task);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void addHistoryRepeatTest() {
        manager.addHistory(task);
        manager.addHistory(task);
        manager.addHistory(task);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void addHistoryAfterRemoveTest() {
        manager.addHistory(task);
        manager.addHistory(task1);
        manager.addHistory(subTask1);
        manager.remove(task.getIdNumber());
        manager.addHistory(task);
        assertEquals(3, manager.getHistory().size());
        assertEquals(subTask1, manager.getHistory().get(1));
    }

    @Test
    void removeInStartTest() {
        manager.addHistory(task);
        manager.addHistory(task1);
        manager.addHistory(subTask1);
        manager.remove(task.getIdNumber());
        assertEquals(2, manager.getHistory().size());
        assertEquals(subTask1, manager.getHistory().get(0));
    }

    @Test
    void removeInMiddleTest() {
        manager.addHistory(task);
        manager.addHistory(task1);
        manager.addHistory(subTask1);
        manager.remove(task1.getIdNumber());
        assertEquals(2, manager.getHistory().size());
        assertEquals(subTask1, manager.getHistory().get(0));
    }
}