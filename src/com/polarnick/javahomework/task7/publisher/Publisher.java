package com.polarnick.javahomework.task7.publisher;

import com.polarnick.javahomework.task7.utils.Utils;

import java.util.concurrent.BlockingQueue;

/**
 * Date: 02.04.14 at 20:05
 *
 * @author Nickolay Polyarniy aka PolarNick
 */
public class Publisher implements Runnable {

    private static final boolean TO_LOG = true;

    private final int id;
    private final BlockingQueue<?> queueToPublish;

    public Publisher(int id, BlockingQueue<?> queueToPublish) {
        this.id = id;
        this.queueToPublish = queueToPublish;
    }

    @Override
    public void run() {
        try {
            Utils.log(TO_LOG, "Publisher " + id + ": result published = " + queueToPublish.take());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}