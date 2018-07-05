package cc.doctor.framework.concurrent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 控制线程池任务提交数量
 */
public class SemaphoreThreadPool {
    private static final Logger log = LoggerFactory.getLogger(SemaphoreThreadPool.class);

    private ExecutorService executorService;
    /**
     * 线程数量，直接生成一个固定大小的线程池
     */
    private int threads;
    /**
     * 可提交的任务数量，通过信号量控制
     */
    private int queueSize;
    private Semaphore semaphore;

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public SemaphoreThreadPool(int threads, int queueSize) {
        this.threads = threads;
        this.queueSize = queueSize;
        executorService = Executors.newFixedThreadPool(threads);
        semaphore = new Semaphore(queueSize);
    }

    public void submit(final Runnable runnable) {
        try {
            semaphore.acquire();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        log.error("", e);
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (InterruptedException ignored) {
        }
    }
}
