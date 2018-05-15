package cc.doctor.framework.log.appender;

import cc.doctor.framework.log.appender.encode.Encoder;
import cc.doctor.framework.log.event.Event;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class AsyncAppender implements Appender {
    private String name;
    private Appender appender;
    private static final int DEFAULT_QUEUE_CAPACITY = 1000;
    private Integer queueSize = DEFAULT_QUEUE_CAPACITY;
    private BlockingQueue<Event> blockingQueue;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private Thread thread;

    public AsyncAppender() {
        thread = new Thread(new EventConsumer());
        thread.setDaemon(true);
        thread.start();
    }

    public Appender getAppender() {
        return appender;
    }

    public void setAppender(Appender appender) {
        this.appender = appender;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void append(Event event) {
        if (blockingQueue == null) {
            blockingQueue = new ArrayBlockingQueue<>(queueSize);
            countDownLatch.countDown();
        }
        try {
            blockingQueue.put(event);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public void setEncoder(Encoder encoder) {
        appender.setEncoder(encoder);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    class EventConsumer implements Runnable {
        @Override
        public void run() {
            try {
                countDownLatch.await();
                Event event = blockingQueue.take();
                appender.append(event);
            } catch (InterruptedException e) {

            }
        }
    }
}
