package com.polarnick.javahomework.task7;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Polyarnyi Nikolay
 */
public class ThreadsDistributorRepeatedlyExecutor {

    private final BlockingQueue<Runnable> regularTasks;
    private final int threadsCountToExecuteTasks;

    public ThreadsDistributorRepeatedlyExecutor(Collection<Runnable> regularTasks, int threadsCountToExecuteTasks) {
        this.regularTasks = new LinkedBlockingQueue<>(regularTasks);
        this.threadsCountToExecuteTasks = threadsCountToExecuteTasks;
    }

    public void start() {
        Collection<Thread> workers = new ArrayList<>(threadsCountToExecuteTasks);
        for (int i = 0; i < threadsCountToExecuteTasks; i++) {
            Thread newWorkerThread = new Thread(new Worker(regularTasks), "Routine executor #" + i);
            workers.add(newWorkerThread);
            newWorkerThread.start();
        }
    }

    private static class Worker implements Runnable {
        private final BlockingQueue<Runnable> taskQueue;

        private Worker(BlockingQueue<Runnable> taskQueue) {
            this.taskQueue = taskQueue;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Runnable task = taskQueue.poll();
                task.run();
                taskQueue.add(task);
            }
        }
    }

}
