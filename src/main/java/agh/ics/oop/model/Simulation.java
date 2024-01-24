package agh.ics.oop.model;

import agh.ics.oop.loop.FixedDelayLoop;

public class Simulation implements Runnable {
    private final WorldMap map;
    private final FixedDelayLoop loop;

    public Simulation(WorldMap map, FixedDelayLoop loop) {
        this.map = map;
        this.loop = loop;
    }

    public void run() {
        this.loop.setAction(time -> map.step());
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
