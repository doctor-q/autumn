package cc.doctor.framework.concurrent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 批量执行队列，将数据入队，到队列满或者一段时间都没有新的数据进来时执行
 */
public abstract class BatchQueue<T> {
    private static final Logger log = LoggerFactory.getLogger(BatchQueue.class);

    private Lock lock = new ReentrantLock();

    private List<T> items = new LinkedList<>();

    /**
     * 批量大小
     */
    private int size = 20;

    /**
     * 判断是否还有新的数据进来，如果持续一段时间idleNotifySecs没有新的数据，则刷新数据
     */
    private int idleNotifySecs = 60;
    private Date pushDate = new Date();
    private volatile boolean destroyed;

    public BatchQueue() {
        startNotifyThread();
    }

    public BatchQueue(int size) {
        this.size = size;
        startNotifyThread();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getIdleNotifySecs() {
        return idleNotifySecs;
    }

    public void setIdleNotifySecs(int idleNotifySecs) {
        this.idleNotifySecs = idleNotifySecs;
    }

    public void startNotifyThread() {
        Thread notifyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!destroyed) {
                    Date now = new Date();
                    if (now.getTime() - pushDate.getTime() > idleNotifySecs * 1000) {
                        log.info("Time to flush the rest items");
                        lock.lock();
                        onFull(items);
                        items.clear();
                        lock.unlock();
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        });
        notifyThread.start();
    }

    public abstract void onFull(List<T> items);

    public void push(T t) {
        lock.lock();
        pushDate = new Date();
        items.add(t);
        if (items.size() >= size) {
            log.info("Queue size[{}] full, do on full hook.", items.size());
            try {
                onFull(items);
            } catch (Exception e) {
                log.error("", e);
            } finally {
                items.clear();
            }
        }
        lock.unlock();
    }

    public void destroy() {
        log.info("Queue destroy, size [{}], do on full hook.", items.size());
        lock.lock();
        onFull(items);
        destroyed = true;
        lock.unlock();
    }
}
