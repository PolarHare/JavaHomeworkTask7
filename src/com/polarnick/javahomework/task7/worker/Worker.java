package com.polarnick.javahomework.task7.worker;

import com.polarnick.javahomework.task7.tasks.TaskWithArgument;
import com.polarnick.javahomework.task7.utils.Utils;

import java.util.concurrent.BlockingQueue;

/**
 * Date: 02.04.14 at 20:31
 *
 * @author Nickolay Polyarniy aka PolarNick
 */
public class Worker<R, V> implements Runnable {

    private static final boolean TO_LOG = true;

    private final int id;
    private final BlockingQueue<R> queueToPublish;
    private final BlockingQueue<TaskWithArgument<R, V>> queueOfTasks;

    public Worker(int id, BlockingQueue<TaskWithArgument<R, V>> queueOfTasks, BlockingQueue<R> queueToPublish) {
        this.id = id;
        this.queueOfTasks = queueOfTasks;
        this.queueToPublish = queueToPublish;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                TaskWithArgument<R, V> task = queueOfTasks.take();
                Utils.log(TO_LOG, "Worker " + id + ": executing task with argument=" + task.getArg());
                R result = task.call();
                Utils.log(TO_LOG, "Worker " + id + ": task executed with argument=" + task.getArg() + " with result=" + result);
                queueToPublish.put(result);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
