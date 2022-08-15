package comp611.assignment1.connectfour.object;

import java.util.ArrayList;
import java.util.List;

public abstract class Task<E,F> implements Runnable {

    private final int id;
    private final E param;
    private final List<TaskObserver<F>> observers;

    public Task(E param) {
        this.id = UniqueIdentifier.getInstance().getNextId();
        this.param = param;
        observers = new ArrayList<>();
    }

    public E getParam() {
        return this.param;
    }

    public int getId() {
        return this.id;
    }

    public void addListener(TaskObserver<F> observer) {
        observers.add(observer);
    }

    public void removeListener(TaskObserver<F> observer) {
        observers.remove(observer);
    }

    public void notifyAll(F progress) {
        for (TaskObserver<F> observer : observers) {
            observer.update(progress);
        }
    }
}