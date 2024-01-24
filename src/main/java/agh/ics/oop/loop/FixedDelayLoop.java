package agh.ics.oop.loop;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class FixedDelayLoop extends TimeDelayLoop implements PausedLoop {
    private final Lock pauseLock;
    private final Condition pauseCondition;
    private boolean isPaused;
    private boolean isStopped;
    private long timeElapsed;
    private long timeDelay;

    public FixedDelayLoop(Consumer<Long> action,
                          long milliseconds) {
        super(action, milliseconds);
        this.pauseLock = new ReentrantLock();
        this.pauseCondition = this.pauseLock.newCondition();
        this.isPaused = false;
        this.isStopped = false;
        this.timeElapsed = 0L;
    }

    @Override
    public void resume() {
        this.pauseLock.lock();
        try {
            this.isPaused = false;
            this.pauseCondition.signalAll();
        } finally {
            this.pauseLock.unlock();
        }
    }

    @Override
    public void pause() {
        this.pauseLock.lock();
        try {
            this.isPaused = true;
        } finally {
            this.pauseLock.unlock();
        }
    }

    @Override
    public boolean isPaused() {
        return this.isPaused;
    }

    @Override
    public void run() {
        while (!this.isStopped) {
            this.pauseLock.lock();
            try {
                while (this.isPaused) {
                    this.pauseCondition.await();
                }
                this.runAction(this.timeElapsed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                this.pauseLock.unlock();
            }

            try {
                synchronized (this) {
                    this.wait(this.timeDelay);
                }
                this.timeElapsed += this.timeDelay;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void stop() {
        this.isStopped = true;
    }

    @Override
    public void setTimeDelay(Long milliseconds) {
        this.timeDelay = milliseconds;
    }
}
