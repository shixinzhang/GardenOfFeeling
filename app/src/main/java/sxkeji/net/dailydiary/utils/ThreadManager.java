/*
 * File Name: ThreadManager.java 
 * History:
 * Created by mwqi on 2014-4-4
 */
package sxkeji.net.dailydiary.utils;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个简易的线程池管理类，提供三个线程池
 * Created by zhangshixin on 2015/11/26.
 * Blog : http://blog.csdn.net/u011240877
 *
 * @description Codes there always can be better.
 */
public class ThreadManager {
    public static final String DEFAULT_SINGLE_POOL_NAME = "DEFAULT_SINGLE_POOL_NAME";
    private static Lock lock = new ReentrantLock();
    private static ThreadPoolProxy mLongPool = null;
//    private static Object mLongLock = new Object();

    private static ThreadPoolProxy mShortPool = null;
//    private static Object mShortLock = new Object();

    private static ThreadPoolProxy mDownloadPool = null;
//    private static Object mDownloadLock = new Object();

    private static Map<String, ThreadPoolProxy> mMap = new HashMap<String, ThreadPoolProxy>();
//    private static Object mSingleLock = new Object();

    /**
     * 获取下载线程
     */
    public static ThreadPoolProxy getDownloadPool() {
        lock.lock();
        try {
            if (mDownloadPool == null) {
                mDownloadPool = new ThreadPoolProxy(3, 3, 5L);
            }
            return mDownloadPool;
        } finally {
            lock.unlock();
        }

//		synchronized (mDownloadLock) {
//			if (mDownloadPool == null) {
//				mDownloadPool = new ThreadPoolProxy(3, 3, 5L);
//			}
//			return mDownloadPool;
//		}
    }

    /**
     * 获取一个用于执行长耗时任务的线程池，避免和短耗时任务处在同一个队列而阻塞了重要的短耗时任务，通常用来联网操作
     */
    public static ThreadPoolProxy getLongPool() {
        lock.lock();
        try {
            if (mLongPool == null) {
                mLongPool = new ThreadPoolProxy(5, 5, 5L);
            }
            return mLongPool;
        } finally {
            lock.unlock();
        }
//        synchronized (mLongLock) {
//            if (mLongPool == null) {
//                mLongPool = new ThreadPoolProxy(5, 5, 5L);
//            }
//            return mLongPool;
//        }
    }

    /**
     * 获取一个用于执行短耗时任务的线程池，避免因为和耗时长的任务处在同一个队列而长时间得不到执行，通常用来执行本地的IO/SQL
     */
    public static ThreadPoolProxy getShortPool() {
        lock.lock();
        try {
            if (mShortPool == null) {
                mShortPool = new ThreadPoolProxy(2, 2, 5L);
            }
            return mShortPool;
        } finally {
            lock.unlock();
        }
//        synchronized (mShortLock) {
//            if (mShortPool == null) {
//                mShortPool = new ThreadPoolProxy(2, 2, 5L);
//            }
//            return mShortPool;
//        }
    }

    /**
     * 获取一个单线程池，所有任务将会被按照加入的顺序执行，免除了同步开销的问题
     */
    public static ThreadPoolProxy getSinglePool() {
        return getSinglePool(DEFAULT_SINGLE_POOL_NAME);
    }

    /**
     * 获取一个单线程池，所有任务将会被按照加入的顺序执行，免除了同步开销的问题
     */
    public static ThreadPoolProxy getSinglePool(String name) {
        lock.lock();
        try {
            ThreadPoolProxy singlePool = mMap.get(name);
            if (singlePool == null) {
                singlePool = new ThreadPoolProxy(1, 1, 5L);
                mMap.put(name, singlePool);
            }
            return singlePool;
        } finally {
            lock.unlock();
        }
//        synchronized (mSingleLock) {
//            ThreadPoolProxy singlePool = mMap.get(name);
//            if (singlePool == null) {
//                singlePool = new ThreadPoolProxy(1, 1, 5L);
//                mMap.put(name, singlePool);
//            }
//            return singlePool;
//        }
    }

    public static class ThreadPoolProxy {
        private ThreadPoolExecutor mPool;
        private int mCorePoolSize;
        private int mMaximumPoolSize;
        private long mKeepAliveTime;

        private ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            mCorePoolSize = corePoolSize;
            mMaximumPoolSize = maximumPoolSize;
            mKeepAliveTime = keepAliveTime;
        }

        /**
         * 执行任务，当线程池处于关闭，将会重新创建新的线程池
         */
        public void execute(Runnable run) {
            lock.lock();
            try {
                if (run == null) {
                    return;
                }
                if (mPool == null || mPool.isShutdown()) {
                    //参数说明
                    //当线程池中的线程小于mCorePoolSize，直接创建新的线程加入线程池执行任务
                    //当线程池中的线程数目等于mCorePoolSize，将会把任务放入任务队列BlockingQueue中
                    //当BlockingQueue中的任务放满了，将会创建新的线程去执行，
                    //但是当总线程数大于mMaximumPoolSize时，将会抛出异常，交给RejectedExecutionHandler处理
                    //mKeepAliveTime是线程执行完任务后，且队列中没有可以执行的任务，存活的时间，后面的参数是时间单位
                    //ThreadFactory是每次创建新的线程工厂
                    mPool = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, mKeepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new AbortPolicy());
                }
                mPool.execute(run);
            } finally {
                lock.unlock();
            }

        }

        /**
         * 取消线程池中某个还未执行的任务
         */
        public void cancel(Runnable run) {
            lock.lock();
            try {
                if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
                    mPool.getQueue().remove(run);
                }
            } finally {
                lock.unlock();
            }

        }

        /**
         * 取消线程池中某个还未执行的任务
         */
        public boolean contains(Runnable run) {
            lock.lock();
            try {
                if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
                    return mPool.getQueue().contains(run);
                } else {
                    return false;
                }
            } finally {
                lock.unlock();
            }


        }

        /**
         * 立刻关闭线程池，并且正在执行的任务也将会被中断
         */
        public void stop() {
            if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
                mPool.shutdownNow();
            }
        }

        /**
         * 平缓关闭单任务线程池，但是会确保所有已经加入的任务都将会被执行完毕才关闭
         */
        public void shutdown() {
            lock.lock();
            try {
                if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
                    mPool.shutdownNow();
                }
            } finally {
                lock.unlock();
            }

        }
    }
}
