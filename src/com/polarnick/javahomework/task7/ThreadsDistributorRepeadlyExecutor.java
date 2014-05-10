package com.polarnick.javahomework.task7;

import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Polyarnyi Nikolay
 */
public class ThreadsDistributorRepeadlyExecutor {

    private final Collection<Runnable> regularTasks;
    private final int threadsCountToExecuteTasks;

    public ThreadsDistributorRepeadlyExecutor(Collection<Runnable> regularTasks, int threadsCountToExecuteTasks) {
        this.regularTasks = regularTasks;
        this.threadsCountToExecuteTasks = threadsCountToExecuteTasks;
    }

    public void start() {
        Iterator<Runnable> iter = Iterators.cycle(regularTasks);
        Collection<Thread> workers = new ArrayList<>(threadsCountToExecuteTasks);
        for (int i = 0; i < threadsCountToExecuteTasks; i++) {
            Thread newWorkerThread = new Thread(new Worker(iter), "Routine executor #" + i);
            workers.add(newWorkerThread);
            newWorkerThread.start();
        }
    }

    private static class Worker implements Runnable {
        private final Iterator<Runnable> iter;

        private Worker(Iterator<Runnable> iter) {
            this.iter = iter;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Runnable task;
                synchronized (iter) {
                    if (!iter.hasNext()) {
                        break;
                    }
                    task = iter.next();
                }
                task.run();
            }
        }
    }

}
