package managers;

import managers.http.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    static KVServer kvServer;

    @Override
    @BeforeEach
    void init() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        manager = new HttpTaskManager(Managers.kvServerUrl);
        super.init();
    }

    @AfterEach
    void close() {
        kvServer.stop();
    }

    @Test
    void createTasksThenSaveAndLoadTest() {
        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addTask(task);
        manager.getTaskById(3);
        assertEquals(1, manager.getAllTask().size());
        assertEquals(1, manager.getAllSubTask().size());
        assertEquals(1, manager.getAllEpic().size());
        manager.load();
        assertEquals(1, manager.getAllTask().size());
        assertEquals(1, manager.getAllSubTask().size());
        assertEquals(1, manager.getAllEpic().size());
        assertEquals(1,manager.getAllHistory().size());
    }

}

