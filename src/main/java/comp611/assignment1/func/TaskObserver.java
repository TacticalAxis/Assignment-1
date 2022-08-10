package comp611.assignment1.func;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface TaskObserver<F> {

    public void update(F progress);
}


//    private Task task;
//    private Thread thread;
//
//    public TaskObserver(Task task) {
//        this.task = task;
//        this.thread = new Thread(task);
//    }
//
//    public void start() {
//        this.thread.start();
//    }
//
//    public void join() {
//        try {
//            this.thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }