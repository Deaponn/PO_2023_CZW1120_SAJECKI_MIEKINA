package agh.ics.oop.loop;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Loop {
    private Consumer<Long> action;
    protected final Thread thread;

    public Loop(Consumer<Long> action, Supplier<Thread> threadSupplier) {
        this.setAction(action);
        this.thread = threadSupplier.get();
        if (!this.thread.isAlive()) this.thread.start();
    }

    public void setAction(Consumer<Long> action) {
        this.action = action;
    }

    public void runAction(Long time) {
        this.action.accept(time);
    }

    public abstract void start();
    public void stop() {}
}
