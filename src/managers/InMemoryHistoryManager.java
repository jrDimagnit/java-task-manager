package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> tail;
    private HashMap<Integer, Node> link = new HashMap<>();

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node<Task> node = tail;
        while (node != null) {
            history.add(node.data);
            node = node.prev;
        }
        return history;
    }

    @Override
    public void addHistory(Task task) {
        if (link.containsKey(task.getIdNumber())) {
            removeNode(link.get(task.getIdNumber()));
            link.remove(task.getIdNumber());
        }
        linkedLast(task);
    }

    @Override
    public void remove(int id) {
        if (link.containsKey(id)) {
            removeNode(link.get(id));
            link.remove(id);
        }
    }

    public void linkedLast(Task task) {
        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            tail = newNode;
        } else {
            oldTail.next = newNode;
        }
        link.put(task.getIdNumber(), newNode);
    }

    public void removeNode(Node node) {
        if (node.prev == null && node.next == null) {
            node.data = null;
            tail = null;
        }
        else{
            Node<Task> next = node.next;
            Node<Task> prev = node.prev;
            node.data = null;
            if (node.prev == null) {
                next.prev = null;
            } else {
                prev.next = next;
                node.prev = null;
            }
            if (node.next == null) {
                tail = prev;
            } else {
                node.next = null;
                next.prev = prev;
            }
        }
    }


    private class Node<Task> {
        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Node<Task> prev, Task data, Node<Task> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}



