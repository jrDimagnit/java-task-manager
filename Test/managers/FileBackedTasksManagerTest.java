package managers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    @BeforeEach
    void init() {
        manager = new FileBackedTasksManager(Managers.getDefaultHistory(), Managers.pathFile);
        super.init();
    }

    @Test
    void getFilePathTest() {
        assertEquals(Managers.pathFile, manager.getFilePath());
    }

    @Test
    void setFilePath() {
        String path = "src\\task\\task.csv";
        manager.setFilePath(path);
        assertEquals(path, manager.filePath);
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
        assertEquals(1, manager2.listEpic.size());
        assertEquals(2, manager2.listSubTask.size());
        assertEquals(2, manager2.listTask.size());
        assertEquals(3, manager2.getAllHistory().size());
    }

    @Test
    void loadFromFileWithClearEpicTest() {
        manager.addEpic(epic);
        manager.addTask(task);
        manager.addTask(task1);
        manager.getTaskById(task.getIdNumber());
        manager.getTaskById(task1.getIdNumber());
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(manager.filePath);
        assertEquals(1, manager2.listEpic.size());
        assertEquals(0, manager2.listSubTask.size());
        assertEquals(2, manager2.listTask.size());
        assertEquals(2, manager2.getAllHistory().size());
    }

    @Test
    void loadFromFileWithClearHistoryTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask1);
        manager.addTask(task);
        manager.addTask(task1);
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(manager.filePath);
        assertEquals(1, manager2.listEpic.size());
        assertEquals(2, manager2.listSubTask.size());
        assertEquals(2, manager2.listTask.size());
        assertEquals(0, manager2.getAllHistory().size());
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