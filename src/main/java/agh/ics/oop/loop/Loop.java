package agh.ics.oop.loop;

import java.util.function.Consumer;

public abstract class Loop implements Runnable {
    private Consumer<Long> action;

    public Loop(Consumer<Long> action) {
        this.setAction(action);
    }

    public void setAction(Consumer<Long> action) {
        this.action = action;
    }

    public void runAction(Long time) {
        this.action.accept(time);
    }

    public abstract void run();
    public abstract void stop();
}
