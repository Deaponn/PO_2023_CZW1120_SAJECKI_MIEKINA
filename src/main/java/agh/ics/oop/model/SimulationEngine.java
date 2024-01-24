package agh.ics.oop.model;

import agh.ics.oop.loop.FixedDelayLoop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine {
    private final ExecutorService executorService;
    public SimulationEngine() {
        this.executorService = Executors.newFixedThreadPool(8);
    }

    public Simulation runSimulation(WorldMap map) {
        return this.runSimulation(map, 250);
    }

    public Simulation runSimulation(WorldMap map, int updateDelay) {
        FixedDelayLoop loop = new FixedDelayLoop(updateDelay);
        Simulation simulation = new Simulation(map, loop);
        executorService.submit(simulation);
        return simulation;
    }

    public void kill() {
        this.executorService.shutdownNow();
    }
}
