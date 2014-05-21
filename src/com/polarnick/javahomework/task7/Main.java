package com.polarnick.javahomework.task7;

import com.polarnick.javahomework.task7.producer.Producer;
import com.polarnick.javahomework.task7.publisher.Publisher;
import com.polarnick.javahomework.task7.tasks.SleepingTaskFactory;
import com.polarnick.javahomework.task7.tasks.TaskFactory;
import com.polarnick.javahomework.task7.tasks.TaskWithArgument;
import com.polarnick.javahomework.task7.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Date: 02.04.14 at 18:32
 *
 * @author Nickolay Polyarniy aka PolarNick
 */
public class Main {

    private static final int COUNT_OF_RUNNABLES = 30;

    private static final int PRODUCER_COUNT = COUNT_OF_RUNNABLES / 3;
    private static final int WORKER_COUNT = COUNT_OF_RUNNABLES / 3;
    private static final int PUBLISHER_COUNT = COUNT_OF_RUNNABLES / 3;

    private static final int MAX_QUEUE_SIZE = WORKER_COUNT * 4;
    private static final int COMMON_THREADS = 21;

    public static void main(String[] args) {
        start(new SleepingTaskFactory(2000));
    }

    private static <R, V> void start(TaskFactory<R, V> taskFactory) {
        BlockingQueue<TaskWithArgument<R, V>> queueOfTasks = createQueue();
        BlockingQueue<R> queueOfResults = createQueue();
        List<Runnable> workers = new ArrayList<>();
        for (int i = 0; i < PUBLISHER_COUNT; i++) {
            workers.add(createPublisher(i, queueOfResults));
        }
        for (int i = 0; i < WORKER_COUNT; i++) {
            workers.add(createWorker(i, queueOfTasks, queueOfResults));
        }
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            workers.add(createProducer(i, queueOfTasks, taskFactory));
        }

        ThreadsDistributorRepeatedlyExecutor executor = new ThreadsDistributorRepeatedlyExecutor(workers, COMMON_THREADS);
        executor.start();
    }

    private static <E> BlockingQueue<E> createQueue() {
        return new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    }

    private static <R, V> Producer<R, V> createProducer(int id, BlockingQueue<TaskWithArgument<R, V>> queueOfTasks, TaskFactory<R, V> taskFactory) {
        return new Producer<>(id, queueOfTasks, taskFactory);
    }

    private static <R, V> Worker<R, V> createWorker(int id, BlockingQueue<TaskWithArgument<R, V>> queueOfTasks, BlockingQueue<R> queueOfResults) {
        return new Worker<>(id, queueOfTasks, queueOfResults);
    }

    private static <R> Publisher createPublisher(int id, BlockingQueue<R> queueOfResults) {
        return new Publisher(id, queueOfResults);
    }

}
