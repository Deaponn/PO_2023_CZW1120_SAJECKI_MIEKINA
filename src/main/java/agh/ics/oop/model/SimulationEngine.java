package agh.ics.oop.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine {
    private final ExecutorService executorService;
    public SimulationEngine() {
        this.executorService = Executors.newFixedThreadPool(8);
    }

    public Simulation runSimulation(WorldMap map) {
        return this.runSimulation(map, 1000);
    }

    public Simulation runSimulation(WorldMap map, int updateDelay) {
        Simulation simulation = new Simulation(map, updateDelay);
        executorService.submit(simulation);
        return simulation;
    }
}
