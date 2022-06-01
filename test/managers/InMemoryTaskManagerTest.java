package managers;

import org.junit.jupiter.api.BeforeEach;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    void init() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
        super.init();
    }
}