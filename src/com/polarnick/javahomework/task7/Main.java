package com.polarnick.javahomework.task7;

import com.polarnick.javahomework.task7.producer.Producer;
import com.polarnick.javahomework.task7.publisher.Publisher;
import com.polarnick.javahomework.task7.tasks.SleepingTaskFactory;
import com.polarnick.javahomework.task7.tasks.TaskFactory;
import com.polarnick.javahomework.task7.tasks.TaskWithArgument;
import com.polarnick.javahomework.task7.worker.Worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Date: 02.04.14 at 18:32
 *
 * @author Nickolay Polyarniy aka PolarNick
 */
public class Main {

    private static final int COUNT_OF_THREADS = 9;

    private static final int PRODUCER_COUNT = COUNT_OF_THREADS / 3;
    private static final int WORKER_COUNT = COUNT_OF_THREADS / 3;
    private static final int PUBLISHER_COUNT = COUNT_OF_THREADS / 3;

    private static final int MAX_QUEUE_SIZE = WORKER_COUNT * 4;

    public static void main(String[] args) {
        start(new SleepingTaskFactory(2000));
    }

    private static <R, V> void start(TaskFactory<R, V> taskFactory) {
        BlockingQueue<TaskWithArgument<R, V>> queueOfTasks = createQueue();
        BlockingQueue<R> queueOfResults = createQueue();
        for (int i = 0; i < PUBLISHER_COUNT; i++) {
            startPublisher(i, queueOfResults);
        }
        for (int i = 0; i < WORKER_COUNT; i++) {
            startWorker(i, queueOfTasks, queueOfResults);
        }
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            startProducer(i, queueOfTasks, taskFactory);
        }
    }

    private static <E> BlockingQueue<E> createQueue() {
        return new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    }

    private static <R, V> void startProducer(int id, BlockingQueue<TaskWithArgument<R, V>> queueOfTasks, TaskFactory<R, V> taskFactory) {
        Producer producer = new Producer<>(id, queueOfTasks, taskFactory);
        Thread thread = new Thread(producer, "Worker with id=" + id);
        thread.start();
    }

    private static <R, V> void startWorker(int id, BlockingQueue<TaskWithArgument<R, V>> queueOfTasks, BlockingQueue<R> queueOfResults) {
        Worker worker = new Worker<>(id, queueOfTasks, queueOfResults);
        Thread thread = new Thread(worker, "Worker with id=" + id);
        thread.start();
    }

    private static <R> void startPublisher(int id, BlockingQueue<R> queueOfResults) {
        Publisher publisher = new Publisher(id, queueOfResults);
        Thread thread = new Thread(publisher, "Publisher with id=" + id);
        thread.start();
    }

}
