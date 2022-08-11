package comp611.assignment1.func;

public class ThreadPool {

    private int size;
    private TaskObserver[] taskObservers;

    public ThreadPool(int initialSize) {
        this.size = initialSize;
        this.taskObservers = new TaskObserver[initialSize];
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAvailableThreads() {
        return 0;
//        return size - getActiveThreads();
    }

    public void resize(int newSize) {
        this.size = newSize;
        TaskObserver[] newTaskObservers = new TaskObserver[newSize];
        for (int i = 0; i < taskObservers.length; i++) {
            newTaskObservers[i] = taskObservers[i];
        }
        this.taskObservers = newTaskObservers;
    }
}
