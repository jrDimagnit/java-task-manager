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
    InMemoryTaskManager manager;
    Task task;
    Task task1;
    SubTask subTask;
    Epic epic;
    Epic epic1;
    SubTask subTask1;

    @BeforeEach
    void init() {
        manager = (InMemoryTaskManager) Managers.getDefault();
        epic = new Epic(manager.changeId(), "Э1", "И", StatusTask.NEW);
        subTask = new SubTask(manager.changeId(), "С1", "I",
                1, StatusTask.NEW);
        subTask.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 5, 0),
                Duration.ofMinutes(60));
        epic1 = new Epic(manager.changeId(), "Э2", "И", StatusTask.NEW);
        task = new Task(manager.changeId(), "T1", " И", StatusTask.NEW);
        task.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 0, 0),
                Duration.ofMinutes(60));
        task1 = new Task(manager.changeId(), "Т2", " И", StatusTask.NEW);
        task1.setUpDateAndDuration(LocalDateTime.of(2022, 6, 1, 10, 0),
                Duration.ofMinutes(60));
        subTask1 = new SubTask(manager.changeId(), "С2", "I",
                1, StatusTask.NEW);
        subTask1.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 10, 0),
                Duration.ofMinutes(60));
    }

    @Test
    void getHistoryNormalTest() {
        manager.inMemory.addHistory(task);
        manager.inMemory.addHistory(task1);
        manager.inMemory.addHistory(subTask);
        manager.inMemory.addHistory(subTask1);
        assertEquals(4, manager.inMemory.getHistory().size());
    }

    @Test
    void getHistoryEmptyTest() {
        assertEquals(0, manager.inMemory.getHistory().size());
    }

    @Test
    void addHistoryNormalTest() {
        manager.inMemory.addHistory(task);
        assertEquals(1, manager.inMemory.getHistory().size());
    }

    @Test
    void addHistoryRepeatTest() {
        manager.inMemory.addHistory(task);
        manager.inMemory.addHistory(task);
        manager.inMemory.addHistory(task);
        assertEquals(1, manager.inMemory.getHistory().size());
    }

    @Test
    void addHistoryAfterRemoveTest() {
        manager.inMemory.addHistory(task);
        manager.inMemory.addHistory(task1);
        manager.inMemory.addHistory(subTask1);
        manager.inMemory.remove(task.getIdNumber());
        manager.inMemory.addHistory(task);
        assertEquals(3, manager.inMemory.getHistory().size());
        assertEquals(subTask1, manager.inMemory.getHistory().get(1));
    }

    @Test
    void removeInStartTest() {
        manager.inMemory.addHistory(task);
        manager.inMemory.addHistory(task1);
        manager.inMemory.addHistory(subTask1);
        manager.inMemory.remove(task.getIdNumber());
        assertEquals(2, manager.inMemory.getHistory().size());
        assertEquals(subTask1, manager.inMemory.getHistory().get(0));
    }

    @Test
    void removeInMiddleTest() {
        manager.inMemory.addHistory(task);
        manager.inMemory.addHistory(task1);
        manager.inMemory.addHistory(subTask1);
        manager.inMemory.remove(task1.getIdNumber());
        assertEquals(2, manager.inMemory.getHistory().size());
        assertEquals(subTask1, manager.inMemory.getHistory().get(0));
    }
}