package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.StatusTask;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    FileBackedTasksManager manager;
    Task task;
    Task task1;
    SubTask subTask;
    Epic epic;
    Epic epic1;
    SubTask subTask1;

    @BeforeEach
    void init(){
        manager = new FileBackedTasksManager(Managers.getDefaultHistory(),Managers.pathFile);
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
    void getFilePathTest() {
        assertEquals(Managers.pathFile,manager.getFilePath());
    }

    @Test
    void setFilePath() {
        String path = "src\\task\\task.csv";
        manager.setFilePath(path);
        assertEquals(path,manager.filePath);
    }

    @Test
    void addFirstTaskTest() {
        manager.addTask(task);
        assertEquals(1,manager.listTask.size());
    }

    @Test
    void addTaskToFillListTest() {
        manager.addTask(task);
        manager.addTask(task1);
        Assertions.assertEquals(2,manager.listTask.size());
    }

    @Test
    void addFirstEpicTest() {
        manager.addEpic(epic);
        Assertions.assertEquals(1,manager.listEpic.size());
    }

    @Test
    void addEpicToFillListTest() {
        manager.addEpic(epic);
        manager.addEpic(epic1);
        Assertions.assertEquals(2,manager.listEpic.size());
    }

    @Test
    void removeAllEpicFillListTest() {
        manager.addEpic(epic);
        manager.addEpic(epic1);
        manager.removeAllEpic();
        Assertions.assertEquals(0,manager.listEpic.size());
    }

    @Test
    void removeAllEpicEmptyListTest() {
        manager.removeAllEpic();
        Assertions.assertEquals(0,manager.listEpic.size());
    }

    @Test
    void addSubTaskWithoutEpicThrowTest(){
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.addSubTask(subTask)
        );
        assertEquals("Epic Not Found", exception.getMessage());

    }
    @Test
    void addSubTaskWithEpicTest(){
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        Assertions.assertEquals(2,manager.listSubTask.size());
        Assertions.assertEquals(2,epic.getSubTasks().size());
    }
    @Test
    void removeAllSubFillListTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        manager.removeAllSub();
        Assertions.assertEquals(0,manager.listSubTask.size());
        Assertions.assertEquals(0,epic.getSubTasks().size());
    }

    @Test
    void removeAllSubEmptyListTest(){
        manager.removeAllSub();
        Assertions.assertEquals(0,manager.listSubTask.size());
    }

    @Test
    void removeAllTaskEmptyListTest(){
        manager.removeAllTask();
        Assertions.assertEquals(0,manager.listTask.size());
    }

    @Test
    void removeAllTaskFillListTest(){
        manager.addTask(task);
        manager.addTask(task1);
        manager.removeAllTask();
        Assertions.assertEquals(0,manager.listTask.size());
    }

    @Test
    void getAllTaskWithEmptyTest() {
        manager.addTask(task);
        manager.addTask(task1);
        manager.removeAllTask();
        Assertions.assertEquals(0,manager.getAllTask().size());
    }
    @Test
    void getAllSubTaskWithEmptyListTest(){
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        manager.removeAllSub();
        Assertions.assertEquals(0,manager.getAllSubTask().size());
    }

    @Test
    void getAllSubTaskWithFillListTest(){
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        Assertions.assertEquals(2,manager.getAllSubTask().size());
    }

    @Test
    void getAllSubTaskWithoutAddTest() {
        Assertions.assertEquals(0,manager.getAllSubTask().size());
    }

    @Test
    void getAllEpicWithEmptyListTest() {
        Assertions.assertEquals(0,manager.getAllEpic().size());
    }

    @Test
    void getAllEpicWithFillListTest() {
        manager.addEpic(epic);
        manager.addEpic(epic1);
        Assertions.assertEquals(2,manager.getAllEpic().size());
    }

    @Test
    void subInEmptyEpicTest() {
        manager.addEpic(epic);
        Assertions.assertEquals(0,manager.subInEpic(epic.getIdNumber()).size());
    }

    @Test
    void subInFillEpicTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        Assertions.assertEquals(2,manager.subInEpic(epic.getIdNumber()).size());
    }
    @Test
    void subInIncorrectIdEpicThrowTest(){
        manager.addEpic(epic);
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.subInEpic(2)
        );
        assertEquals("Epic Not Found!", exception.getMessage());
    }


    @Test
    void removeIdTaskWithFillListTest() {
        manager.addTask(task);
        manager.addTask(task1);
        manager.removeIdTask(task.getIdNumber());
        Assertions.assertEquals(1,manager.listTask.size());
    }

    @Test
    void removeIdTaskWithEmptyListThrowTest() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.removeIdTask(1)
        );
        assertEquals("Task Not Found!", exception.getMessage());
    }

    @Test
    void removeIdSubWithFillListTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        manager.removeIdSub(2);
        Assertions.assertEquals(1,manager.listSubTask.size());
        Assertions.assertEquals(1,epic.getSubTasks().size());
    }

    @Test
    void removeIdSubWithEmptyListThrowTest() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.removeIdSub(2)
        );
        assertEquals("SubTask Not Found!", exception.getMessage());
    }

    @Test
    void removeIdEpicWithFillListTest() {
        manager.addEpic(epic);
        manager.addEpic(epic1);
        assertEquals(2,manager.listEpic.size());
        manager.removeIdEpic(1);
        assertEquals(1,manager.listEpic.size());
    }

    @Test
    void removeIdEpicWithEmptyListTest() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.removeIdEpic(1)
        );
        assertEquals("Epic Not Found!", exception.getMessage());
    }


    @Test
    void updateTaskWithNormalTaskTest() {
        manager.addTask(task);
        assertEquals(1,manager.listTask.size());
        assertEquals("T1",manager.listTask.get(4).getNameTask());
        task1.setIdNumber(task.getIdNumber());
        manager.updateTask(task1);
        assertEquals(1,manager.listTask.size());
        assertEquals("Т2",manager.listTask.get(4).getNameTask());
    }

    @Test
    void updateTaskWithAbnormalIdTest() {
        manager.addTask(task);
        assertEquals(1,manager.listTask.size());
        assertEquals("T1",manager.listTask.get(4).getNameTask());
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.updateTask(task1)
        );
        assertEquals("Task Not Found!", exception.getMessage());
    }

    @Test
    void updateSubTaskWithNormalSubTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        assertEquals("С1",manager.listSubTask.get(2).getNameTask());
        subTask1.setIdNumber(subTask.getIdNumber());
        manager.updateSubTask(subTask1);
        assertEquals(1,manager.listSubTask.size());
        assertEquals("С2",manager.listSubTask.get(2).getNameTask());
    }

    @Test
    void updateSubTaskWithAbnormalTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        assertEquals("С1",manager.listSubTask.get(2).getNameTask());
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.updateSubTask(subTask1)
        );
        assertEquals("SubTask Not Found!", exception.getMessage());
    }

    @Test
    void getEpicByCorrectIdTest() {
        manager.addEpic(epic);
        assertEquals(1,manager.listEpic.size());
        assertEquals("Э1",manager.getEpicById(1).getNameTask());
    }

    @Test
    void getEpicByAbnormalIdTest() {
        manager.addEpic(epic);
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.getEpicById(2)
        );
        assertEquals("Epic Not Found!", exception.getMessage());
    }


    @Test
    void getSubByCorrectIdTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        assertEquals("С2",manager.getSubById(6).getNameTask());
    }

    @Test
    void getSubByAbnormalIdTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.getSubById(3)
        );
        assertEquals("SubTask Not Found!", exception.getMessage());
    }

    @Test
    void getTaskByCorrectIdTest() {
        manager.addTask(task);
        manager.addTask(task1);
        assertEquals(2,manager.listTask.size());
        assertEquals("T1",manager.getTaskById(4).getNameTask());
    }

    @Test
    void getTaskByAbnormalIdTest() {
        manager.addTask(task);
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.getTaskById(5)
        );
        assertEquals("Task Not Found!", exception.getMessage());
    }

    @Test
    void updateEpicWithCorrectTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        epic1.setIdNumber(epic.getIdNumber());
        manager.updateEpic(epic1);
        assertEquals("Э2",manager.getEpicById(1).getNameTask());
    }

    @Test
    void updateEpicWithAbnormalTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.updateEpic(epic1)
        );
        assertEquals("Epic Not Found!", exception.getMessage());
    }

    @Test
    void removeFromEpicWithNormalTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        assertEquals(2,manager.listSubTask.size());
        manager.removeFromEpic(subTask);
        assertEquals("С2",epic.getSubTasks().get(0).getNameTask());
        assertEquals(1,epic.getSubTasks().size());
    }

    @Test
    void removeFromEpicWithIncorrectTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.removeFromEpic(subTask1)
        );
        assertEquals("SubTask Not Found!", exception.getMessage());
    }

    @Test
    void loadFromFileNormalTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        manager.addTask(task);
        manager.addTask(task1);
        manager.getTaskById(task.getIdNumber());
        manager.getTaskById(task1.getIdNumber());
        manager.getSubById(subTask.getIdNumber());
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(manager.filePath);
        assertEquals(1,manager2.listEpic.size());
        assertEquals(2,manager2.listSubTask.size());
        assertEquals(2,manager2.listTask.size());
        assertEquals(3,manager2.getAllHistory().size());
    }
    @Test
    void loadFromFileWithClearEpicTest(){
        manager.addEpic(epic);
        manager.addTask(task);
        manager.addTask(task1);
        manager.getTaskById(task.getIdNumber());
        manager.getTaskById(task1.getIdNumber());
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(manager.filePath);
        assertEquals(1,manager2.listEpic.size());
        assertEquals(0,manager2.listSubTask.size());
        assertEquals(2,manager2.listTask.size());
        assertEquals(2,manager2.getAllHistory().size());
    }

    @Test
    void loadFromFileWithClearHistoryTest(){
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        manager.addTask(task);
        manager.addTask(task1);
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(manager.filePath);
        assertEquals(1,manager2.listEpic.size());
        assertEquals(2,manager2.listSubTask.size());
        assertEquals(2,manager2.listTask.size());
        assertEquals(0,manager2.getAllHistory().size());
    }
    /*@Test
    void loadFromFileWithClearTaskTest(){//Работает при пустом файле task.csv!
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(manager.filePath);
        assertEquals(0,manager2.listEpic.size());
        assertEquals(0,manager2.listSubTask.size());
        assertEquals(0,manager2.listTask.size());
        assertEquals(0,manager2.getAllHistory().size());
    }*/
}