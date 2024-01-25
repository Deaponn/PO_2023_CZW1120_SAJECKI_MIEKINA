package agh.ics.oop.loop;

import java.util.concurrent.Future;
import java.util.function.Consumer;

public abstract class Loop implements Runnable {
    private final Consumer<Long> action;
    protected final LoopController loopController;
    protected Future<?> future;

    public Loop(Consumer<Long> action, LoopController loopController) {
        this.action = action;
        this.loopController = loopController;
    }

    public void run() {
        long time = System.nanoTime();
        this.action.accept(time);
    }

    public void start() {
        this.future = this.attach();
    }

    protected abstract Future<?> attach();
    public abstract void exit();
}
