package agh.ics.oop.loop;

import agh.ics.oop.util.ThreadManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class LoopController implements ThreadManager {
    private final ScheduledExecutorService executorService;

    public LoopController(int poolSize) {
        this.executorService = Executors.newScheduledThreadPool(poolSize);
    }

    public ScheduledExecutorService getExecutorService() {
        return this.executorService;
    }

    public void kill() {
        this.executorService.shutdown();
    }
}
