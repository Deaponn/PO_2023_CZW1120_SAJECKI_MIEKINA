package agh.ics.oop.model;

public class Simulation implements Runnable {
    private final WorldMap map;
    private int updateDelay;
    private boolean isPaused = false;
    private boolean isKilled = false;

    public Simulation(WorldMap map, int updateDelay) {
        this.map = map;
        this.updateDelay = updateDelay;
    }

    public void run() {
        if (isKilled) return;
        // TODO: important to check if the paused program won't cause the
        //      computer to become helicopter since run() will call itself back to back
        if (!isPaused) try {
            Thread.sleep(updateDelay);
            map.step();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.run();
    }

    public void setUpdateDelay(int newDelay) { this.updateDelay = newDelay; }

    public boolean getIsPaused() { return this.isPaused; }

    public void setIsPaused(boolean newValue) { this.isPaused = newValue; }

    public void kill() { this.isKilled = true; }
}
