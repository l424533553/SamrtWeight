package com.luofx.utils.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池 工具类  lfx
 */
public class ThreadPool {

    /**
     * java线程池
     */
    private static ThreadPoolExecutor threadPoolExecutor;

    /**
     * 最大线程数
     */
    private final static int MAX_POOL_COUNTS = 10;

    /**
     * 线程存活时间
     */
    private final static long ALIVETIME = 200L;

    /**
     * 核心线程数
     */
    private final static int CORE_POOL_SIZE = 5;

    /**
     * 线程池缓存队列
     */

    private ThreadPool() {
        BlockingQueue<Runnable> mWorkQueue = new ArrayBlockingQueue<>(CORE_POOL_SIZE);
        ThreadFactory threadFactory = new ThreadFactoryBuilder("ThreadPool");
        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_COUNTS, ALIVETIME, TimeUnit.SECONDS, mWorkQueue, threadFactory);
    }

    /**
     * 数据
     * @return  获得线程池
     */
    public static ThreadPoolExecutor getInstantiation() {
        if (threadPoolExecutor == null) {
           new ThreadPool();
        }
        return threadPoolExecutor;
    }

    /**
     * @param threadCount  固定的核心线程数
     * @return  返回线程池
     */
    public static ThreadPoolExecutor getFixedThreadPool(int threadCount ) {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
        }
        return threadPoolExecutor;
    }

    public void addTask(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("addTask(Runnable runnable)传入参数为空");
        }
        if (threadPoolExecutor != null && threadPoolExecutor.getActiveCount() < MAX_POOL_COUNTS) {
            threadPoolExecutor.execute(runnable);
        }
    }

    /**
     * 停止线程
     */
    public void stopThreadPool() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
            threadPoolExecutor = null;
        }
    }

}
