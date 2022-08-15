package comp611.assignment1.func;

import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings({"InfiniteLoopStatement", "unused", "BusyWait"})
public class ThreadPool {

    // FIFO ordering
    private final LinkedBlockingQueue<Runnable> lbQueue;

    //Thread pool size
    private int size;

    //Internally pool is an array
    private Worker[] workers;

    public ThreadPool(int size) {
        this.size = size;
        lbQueue = new LinkedBlockingQueue<>();
        workers = new Worker[size];

        for (int i = 0; i < size; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    public synchronized int getSize() {
        return size;
    }

    public synchronized int getAvailable() {
        int amount = 0;
        for (Worker worker : workers) {
            if (!worker.isBusy()) {
                amount += 1;
            }
        }

        return amount;
    }



    public void resize(int newSize) {
        this.size = newSize;
        Worker[] newWorkers = new Worker[newSize];
        int iter = 0;
        while (iter < workers.length) {
            newWorkers[iter] = workers[iter];
            iter += 1;
        }
        while (iter < newSize) {
            newWorkers[iter] = new Worker();
            iter += 1;
        }
        this.workers = newWorkers;
    }

    public void perform(Runnable task) {
        if(getAvailable() <= 0) {
            resize(size*2);
        }
        synchronized (lbQueue) {
            lbQueue.add(task);
            lbQueue.notifyAll();
        }
    }

    public boolean hasActive() {
        for (Worker worker : workers) {
            if (worker.isBusy()) {
                return true;
            }
        }

        return getAvailable() != getSize();
    }

    public void shutdown() {
        System.out.println("Shutting down thread pool");
        for (int i = 0; i < size; i++) {
            workers[i] = null;
        }
    }

    private synchronized void destroy(){
        Thread.interrupted();
    }

    private class Worker extends Thread {

        private boolean busy;
        private Runnable runnable;

        public Worker() {
            this.busy = false;
            this.runnable = null;
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public boolean isBusy() {
            return busy;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (lbQueue) {
                    while (lbQueue.isEmpty()) {
                        busy = false;
                        try {
                            lbQueue.wait();
                        } catch (InterruptedException ex) {
                            System.out.println("An error occurred while waiting: " + ex.getMessage());
                        }
                    }
                    this.runnable = lbQueue.poll();
                    busy = true;
                }

                try {
                    runnable.run();
                } catch (RuntimeException ex) {
                    System.out.println("Thread pool interrupted: " + ex.getMessage());
                }
            }
        }
    }

    private static class SomeTask extends Task<String, Integer> {

        public SomeTask(String param) {
            super(param);
        }

        public void run() {
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                System.out.println("An error occurred while thread is waiting: " + e.getMessage());
            }

            System.out.println("Task [" + getParam() + "] executed on : " + LocalDateTime.now());
        }
    }

    private static class SomeObserver implements TaskObserver<Integer> {

        @Override
        public void update(Integer progress) {
            System.out.println(progress);
        }
    }

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(2);

        for (int i = 1; i <= 400; i++) {

            SomeTask task = new SomeTask("amongus: " + i);
            System.out.println("Created : " + task.getId());

            threadPool.perform(task);
        }

        while (threadPool.hasActive()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        threadPool.shutdown();
        System.out.println("Process Complete");
    }
}