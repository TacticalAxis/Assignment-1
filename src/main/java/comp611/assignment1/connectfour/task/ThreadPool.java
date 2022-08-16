package comp611.assignment1.connectfour.task;

import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings({"InfiniteLoopStatement", "unused", "UnusedReturnValue"})
public class ThreadPool {

    // use a linked blocking queue to store tasks
    private final LinkedBlockingQueue<Runnable> lbQueue;

    // local size value
    private int size;

    // internal array to store workers
    private Worker[] workers;

    public ThreadPool(int size) {
        // init actual thread pool
        this.size = size;
        lbQueue = new LinkedBlockingQueue<>();
        workers = new Worker[size];

        // create workers
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

    public void destroyPool() {
        for (Worker worker : workers) {
            worker.interrupt();
        }
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

    public boolean perform(Runnable task) {
        if (getAvailable() <= 0) {
            resize(size * 2);
        }

        synchronized (lbQueue) {
            lbQueue.add(task);
            lbQueue.notifyAll();
        }

        return true;
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
                // get next task off queue
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

                // run the actual task
                try {
                    runnable.run();
                } catch (RuntimeException ex) {
                    System.out.println("Thread pool interrupted: " + ex.getMessage());
                }

                // try to avoid synchronisation issues
                synchronized (this) {
                    try {
                        wait(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}