package agh.ics.oop.model;

import agh.ics.oop.util.ThreadManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine implements ThreadManager {
    private final ExecutorService executorService;
    public SimulationEngine() {
        this.executorService = Executors.newFixedThreadPool(8);
    }

    public Simulation run(WorldMap map) {
        return this.run(map, 250);
    }

    public Simulation run(WorldMap map, long updateDelay) {
        Simulation simulation = new Simulation(map, updateDelay);
        executorService.submit(simulation);
        return simulation;
    }

    public void kill() {
        this.executorService.shutdown();
    }
}
