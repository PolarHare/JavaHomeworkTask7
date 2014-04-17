package com.polarnick.javahomework.task7.producer;

import com.polarnick.javahomework.task7.tasks.Task;
import com.polarnick.javahomework.task7.tasks.TaskFactory;
import com.polarnick.javahomework.task7.tasks.TaskWithArgument;
import com.polarnick.javahomework.task7.utils.Utils;

import java.util.concurrent.BlockingQueue;

/**
 * Date: 02.04.14 at 17:41
 *
 * @author Nickolay Polyarniy aka PolarNick
 */
public class Producer<Result, Param> implements Runnable {

    private static final boolean TO_LOG = true;

    private final int id;
    private final BlockingQueue<TaskWithArgument<Result, Param>> taskQueue;
    private final TaskFactory<Result, Param> taskFactory;

    public Producer(int id, BlockingQueue<TaskWithArgument<Result, Param>> taskQueue, TaskFactory<Result, Param> taskFactory) {
        this.id = id;
        this.taskQueue = taskQueue;
        this.taskFactory = taskFactory;
    }

    @Override
    public void run() {
        boolean finished = false;
        while (!finished) {
            try {
                Task<Result, Param> task = taskFactory.createTask();
                Param argument = taskFactory.createArgument();
                Utils.log(TO_LOG, "Client " + id + ": Task queued with argument = " + argument);
                taskQueue.put(new TaskWithArgument<>(task, argument));
            } catch (InterruptedException ignored) {
                finished = true;
            }
        }
    }

}
