package cc.doctor.framework.log.appender;

import cc.doctor.framework.log.appender.encode.Encoder;
import cc.doctor.framework.log.event.Event;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AsynAppender implements Appender {
    private Appender appender;
    private static final int DEFAULT_QUEUE_CAPACITY = 1000;
    private BlockingQueue<Event> blockingQueue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);

    @Override
    public String getName() {
        return appender.getName();
    }

    @Override
    public void append(Event event) {
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

    }

    class EventConsumer implements Runnable {

        @Override
        public void run() {
            try {
                Event event = blockingQueue.take();
                appender.append(event);
            } catch (InterruptedException e) {

            }
        }
    }
}
