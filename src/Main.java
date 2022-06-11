import managers.Managers;
import managers.http.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

public class Main {

    static KVServer kvServer;
    static HttpTaskServer httpTaskServer;

    public static void main(String[] args) throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer(Managers.getDefault());
        httpTaskServer.start();
    }
}


