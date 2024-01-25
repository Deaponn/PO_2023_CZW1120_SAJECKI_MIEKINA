package agh.ics.oop.model;

import agh.ics.oop.loop.FixedDelayLoop;
import agh.ics.oop.loop.LoopController;
import agh.ics.oop.util.ThreadManager;

public class SimulationEngine implements ThreadManager {
//    private final ExecutorService executorService;
    private final LoopController loopController;

    public SimulationEngine() {
//        this.executorService = Executors.newFixedThreadPool(8);
        this.loopController = new LoopController(8);
    }

    public Simulation run(WorldMap map) {
        FixedDelayLoop loop = new FixedDelayLoop(time -> map.step(), loopController, 250_000L);
        Simulation simulation = new Simulation(map, loop);
        loop.start();
        return simulation;
    }

//    public Simulation run(WorldMap map) {
//        Simulation simulation = new Simulation(map);
//        executorService.submit(simulation);
//        return simulation;
//    }

    public void kill() {
//        this.executorService.shutdown();
        this.loopController.kill();
    }
}
