package comp611.assignment1.connectfour.task.util;

public class UniqueIdentifier {

    private static UniqueIdentifier instance;
    private int nextId;

    private UniqueIdentifier() {
        nextId = 0;
    }

    public static UniqueIdentifier getInstance() {
        synchronized (UniqueIdentifier.class) {
            if (instance == null) instance = new UniqueIdentifier();
        }

        return instance;
    }

    public synchronized int getNextId() {
        return nextId++;
    }
}