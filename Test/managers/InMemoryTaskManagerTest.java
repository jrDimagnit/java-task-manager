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
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    InMemoryTaskManager manager;
    Task task;
    Task task1;
    SubTask subTask;
    Epic epic;
    Epic epic1;
    SubTask subTask1;

    @BeforeEach
    void init(){
        manager = (InMemoryTaskManager)Managers.getDefault();
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
    void getAllHistoryNormalTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        manager.addTask(task);
        manager.addTask(task1);
        manager.getTaskById(4);
        manager.getTaskById(5);
        manager.getSubById(2);
        assertEquals(3,manager.getAllHistory().size());
    }

    @Test
    void getPrioritizedTasksTest() {
        manager.addTask(task);
        manager.addTask(task1);
        manager.addEpic(epic);
        assertEquals(2,manager.getPrioritizedTasks().size());
    }

    @Test
    void checkPriorityWithNormalTest() {
        manager.addTask(task);
        assertTrue(manager.checkPriority(task1));
    }
    @Test
    void checkPriorityWithIncorrectTest(){
        manager.addTask(task);
        task1.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 0, 30),
                Duration.ofMinutes(60));
        assertFalse(manager.checkPriority(task1));
        task1.setUpDateAndDuration(LocalDateTime.of(2022, 5, 1, 0, 30),
                Duration.ofMinutes(30));
        assertFalse(manager.checkPriority(task1));
        task1.setUpDateAndDuration(LocalDateTime.of(2022, 4, 30, 23, 59),
                Duration.ofMinutes(3000));
        assertFalse(manager.checkPriority(task1));
    }
}