package comp611.assignment1.func;

public abstract class Task<E,F> implements Runnable {

    private final E param;

    public Task(E param) {this.param = param;}

    public abstract int getId();

    public abstract void addListener(TaskObserver<F> observer);

    public abstract void removeListener(TaskObserver<F> observer);

    protected abstract void notifyAll(F progress);
}