package agh.ics.oop.loop;

import java.util.function.Consumer;

public abstract class TimeDelayLoop extends Loop {
    protected long timeDelay;

    public TimeDelayLoop(Consumer<Long> action,
                         long milliseconds) {
        super(action);
        this.setTimeDelay(milliseconds);
    }

    public void setTimeDelay(Long milliseconds) {
        this.timeDelay = milliseconds;
    }
}
