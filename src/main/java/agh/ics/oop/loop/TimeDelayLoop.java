package agh.ics.oop.loop;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class TimeDelayLoop extends Loop {
    protected long timeDelay;

    public TimeDelayLoop(Consumer<Long> action,
                         Supplier<Thread> threadSupplier,
                         long milliseconds) {
        super(action, threadSupplier);
        this.setTimeDelay(milliseconds);
    }

    public void setTimeDelay(Long milliseconds) {
        this.timeDelay = milliseconds;
    }
}
