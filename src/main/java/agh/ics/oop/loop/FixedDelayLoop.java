package agh.ics.oop.loop;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FixedDelayLoop extends TimeDelayLoop implements PausedLoop {
    private final Lock pauseLock;
    private boolean isPaused;
    private boolean isStopped;
    private long timeElapsed;
    private long timeDelay;

    public FixedDelayLoop(Consumer<Long> action,
                          Supplier<Thread> threadSupplier,
                          long milliseconds) {
        super(action, threadSupplier, milliseconds);
        this.pauseLock = new ReentrantLock();
        this.isPaused = false;
        this.isStopped = false;
        this.timeElapsed = 0L;
    }

    public FixedDelayLoop(Consumer<Long> action,
                          long milliseconds) {
        this(action, Thread::currentThread, milliseconds);
    }

    @Override
    public void resume() {
        this.isPaused = false;
        this.pauseLock.unlock();
    }

    @Override
    public void pause() {
        this.isPaused = true;
        this.pauseLock.lock();
    }

    @Override
    public boolean isPaused() {
        return this.isPaused;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void start() {
        while (!this.isStopped) {
            this.pauseLock.lock();
            this.runAction(this.timeElapsed);
            this.pauseLock.unlock();

            try {
                Thread.sleep(this.timeDelay);
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
