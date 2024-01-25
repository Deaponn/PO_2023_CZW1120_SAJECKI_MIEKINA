package agh.ics.oop.render;

import agh.ics.oop.loop.*;
import agh.ics.oop.util.ThreadManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RendererEngine implements ThreadManager {
    private final ExecutorService executorService;
    private final LoopController loopController;

    public RendererEngine() {
        this.executorService = Executors.newCachedThreadPool();
        this.loopController = new LoopController(RendererEngine.loopPoolSize);
    }

    public void renderOverlay(WorldRenderer worldRenderer) {
        this.executorService.submit(worldRenderer::renderOverlayViewLayer);
    }

    public void renderWorld(WorldRenderer worldRenderer) {
        this.executorService.submit(worldRenderer::renderWorldViewLayer);
    }

    public DelayLoop runRenderer(WorldRenderer worldRenderer) {
        DelayLoop overlayLoop = new FixedDelayLoop(
                time -> this.renderOverlay(worldRenderer),
                this.loopController,
                50_000L);
        overlayLoop.start();
        return overlayLoop;
    }

    public void kill() {
        this.executorService.shutdown();
        this.loopController.kill();
    }

    private static final int loopPoolSize = 16;
}
