package agh.ics.oop.model;

import agh.ics.oop.loop.FixedDelayLoop;

public class Simulation implements Runnable {
    private final FixedDelayLoop loop;

    public Simulation(WorldMap map, long timeDelay) {
        this.loop = new FixedDelayLoop(time -> map.step(), timeDelay);
    }

    public void run() {
        this.loop.start();
    }

    public void setUpdateDelay(long milliseconds) {
        this.loop.setTimeDelay(milliseconds);
    }

    public boolean isPaused() {
        return this.loop.isPaused();
    }

    public void resume() {
        this.loop.resume();
    }

    public void pause() {
        this.loop.pause();
    }

    public void kill() {
        this.loop.stop();
    }
}
