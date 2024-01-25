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
        this.map.step();
    }

    public void setUpdateDelay(long milliseconds) {
        this.loop.setDelay(milliseconds * 1000L);
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
        this.loop.exit();
    }
}
