package customThread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PausableThreadPoolExecutor extends ThreadPoolExecutor {
    private boolean isPaused;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();
    public PausableThreadPoolExecutor(Integer numOfAgents, BlockingQueue<Runnable> missionsQueue, ThreadFactory customThreadFactory) {
        super(numOfAgents,numOfAgents,10, TimeUnit.SECONDS,missionsQueue,customThreadFactory);
    }
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused) unpaused.await();
        } catch (InterruptedException ie) {
            t.interrupt();      }
        finally {
            pauseLock.unlock();
        }
    }      public void pause() {
        pauseLock.lock();
        try {        isPaused = true;
        } finally {        pauseLock.unlock();
        }
    }      public void resume() {      pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {        pauseLock.unlock();
        }
    }

    public void stop() {
        super.shutdown();
    }
}