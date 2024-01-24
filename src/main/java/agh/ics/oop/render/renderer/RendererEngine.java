package agh.ics.oop.render.renderer;

import agh.ics.oop.loop.FixedDelayLoop;
import agh.ics.oop.loop.Loop;
import agh.ics.oop.loop.LoopControl;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.util.ThreadManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RendererEngine implements ThreadManager {
    private final ExecutorService executorService;
    private final LoopControl loopControl;

    public RendererEngine() {
        this.executorService = Executors.newCachedThreadPool();
        this.loopControl = new LoopControl(RendererEngine.loopPoolSize);
    }

    public void renderOverlay(WorldRenderer worldRenderer) {
        this.executorService.submit(worldRenderer::renderOverlayViewLayer);
    }

    public void renderWorld(WorldRenderer worldRenderer) {
        this.executorService.submit(worldRenderer::renderWorldViewLayer);
    }

    public void addRenderer(WorldRenderer worldRenderer) {
        Loop overlayLoop = new FixedDelayLoop(
                time -> this.renderOverlay(worldRenderer),
                100L);
        this.loopControl.addLoop(overlayLoop);
    }

    public void kill() {
        this.executorService.shutdown();
        this.loopControl.kill();
    }

    private static final int loopPoolSize = 16;
}
