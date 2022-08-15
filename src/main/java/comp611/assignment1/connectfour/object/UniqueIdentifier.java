package comp611.assignment1.connectfour.object;

public class UniqueIdentifier {

    private int nextId;
    private static UniqueIdentifier instance;

    private UniqueIdentifier() {
        nextId = 0;
    }

    public static UniqueIdentifier getInstance() {
//        if (instance == null) {
            synchronized (UniqueIdentifier.class) {
                if (instance == null) instance = new UniqueIdentifier();
//            }
        }
        return instance;
    }

    public synchronized int getNextId() {
        return nextId++;
    }
}

// threadpool resizes thread pool when there are more tasks
// threadpool destroys threads
