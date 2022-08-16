package comp611.assignment1.connectfour.observer;

public interface TaskObserver<F> {

    void update(F progress);
}
