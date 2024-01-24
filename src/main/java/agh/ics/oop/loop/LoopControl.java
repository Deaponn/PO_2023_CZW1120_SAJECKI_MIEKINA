package agh.ics.oop.loop;

import agh.ics.oop.util.ThreadManager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoopControl implements ThreadManager {
    private final ExecutorService executorService;
    private final List<Loop> loopList;

    public LoopControl(int poolSize) {
        this.executorService = Executors.newScheduledThreadPool(poolSize);
        this.loopList = new LinkedList<>();
    }

    public void addLoop(Loop loop) {
        this.loopList.add(loop);
        this.executorService.submit(loop);
    }

    public void removeLoop(Loop loop) {
        loop.stop();
        this.loopList.remove(loop);
    }

    public void kill() {
        this.loopList.forEach(Loop::stop);
        this.executorService.shutdown();
    }
}
