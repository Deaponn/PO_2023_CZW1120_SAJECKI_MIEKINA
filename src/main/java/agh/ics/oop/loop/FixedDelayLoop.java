package agh.ics.oop.loop;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FixedDelayLoop extends AbstractLoop implements PausedLoop, DelayLoop {
    private boolean isPaused;
    private final long timeStarted;
    private long timeDelay;

    public FixedDelayLoop(Consumer<Long> action,
                          LoopController loopController,
                          long microseconds) {
        super(action, loopController);
        this.isPaused = false;
        this.timeDelay = microseconds;
        this.timeStarted = System.nanoTime();
    }

    @Override
    public void resume() {
        if (!this.isPaused) return;
        this.future = this.attach(0L);
        this.isPaused = false;
    }

    @Override
    public void pause() {
        if (this.isPaused) return;
        this.exit();
        this.isPaused = true;
    }

    @Override
    public boolean isPaused() {
        return this.isPaused;
    }

    public long getElapsedTime() {
        return System.nanoTime() - this.timeStarted;
    }

    @Override
    protected Future<?> attach(long initialDelay) {
        return this.loopController.getExecutorService()
                .scheduleWithFixedDelay(
                        this,
                        initialDelay, this.timeDelay,
                        TimeUnit.MICROSECONDS);
    }

    @Override
    public void exit() {
        if (this.future != null)
            this.future.cancel(false);
    }

    @Override
    public void setDelay(long microseconds) {
        this.exit();
        this.timeDelay = microseconds;
        this.future = this.attach(microseconds);
    }
}
