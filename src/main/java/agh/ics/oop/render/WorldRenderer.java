package agh.ics.oop.render;

import agh.ics.oop.loop.FixedDelayLoop;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.image.ImageAtlasSampler;
import agh.ics.oop.render.image.ImageMap;
import agh.ics.oop.render.image.ImageSampler;
import agh.ics.oop.render.image.ImageSamplerMap;
import agh.ics.oop.util.Reactive;
import agh.ics.oop.view.View;
import agh.ics.oop.view.ViewLayer;
import javafx.application.Platform;

import java.util.LinkedList;
import java.util.List;

public class WorldRenderer {
    public final ImageSamplerMap imageSamplerMap;
    public final View<?> view;
    public final List<Overlay> overlayList;
    private final UnitRendererAssignmentMap unitRendererAssignmentMap;
    private WorldMap worldMap;

    public final ViewLayer worldViewLayer;
    public final ViewLayer overlayViewLayer;

    public final Reactive<Long> frameRenderTime;
//    public final FixedDelayLoop overlayRenderLoop;

    public WorldRenderer(ImageMap imageMap, View<?> view) {
        this.imageSamplerMap = new ImageSamplerMap(imageMap);
        this.view = view;
        this.overlayList = new LinkedList<>();
        this.unitRendererAssignmentMap = new UnitRendererAssignmentMap();

        this.worldViewLayer = new ViewLayer();
        this.overlayViewLayer = new ViewLayer();

        this.view.withViewLayers(List.of(this.worldViewLayer, this.overlayViewLayer));

        this.frameRenderTime = new Reactive<>(0L);

        Thread overlayThread = new Thread(() -> new FixedDelayLoop(
                time -> this.renderOverlayViewLayer(),
                Thread::new,
                100L).start());
        overlayThread.start();
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public synchronized void renderWorldViewLayer() {
        Platform.runLater(() -> {
            long startNanoTime = System.nanoTime();

            Boundary bounds = this.worldMap.getCurrentBounds();
            this.view.setGridBounds(bounds);
            bounds.mapAllPositions(this.worldMap::getElements)
                    .forEach(element -> this.tryRender(this.worldViewLayer, element));
            this.renderOverlayViewLayer();

            long endNanoTime = System.nanoTime();

            this.frameRenderTime.setValue(endNanoTime - startNanoTime);
            this.overlayList.forEach(overlay -> overlay.updateOnFrame(this));
        });
    }

    public synchronized void renderOverlayViewLayer() {
        Platform.runLater(() -> {
            this.overlayViewLayer.createBuffer();
            this.overlayList
                    .forEach(overlay -> this.tryRender(this.overlayViewLayer, overlay));
            this.view.presentView();
        });
    }

    public void renderUnit(ViewLayer viewLayer, Object unit) throws IllegalRendererAssignment {
        this.unitRendererAssignmentMap.renderUnit(this, viewLayer, unit);
    }

    private <U> void tryRender(ViewLayer viewLayer, List<U> unitList) {
        unitList.forEach(unit -> this.tryRender(viewLayer, unit));
    }

    private <U> void tryRender(ViewLayer viewLayer, U unit) {
        try {
            this.renderUnit(viewLayer, unit);
        } catch (IllegalRendererAssignment e) {
            System.out.println("WorldRenderer: " + e.getMessage());
        }
    }

    public void putImageAtGrid(
            Vector2D position,
            String samplerKey,
            ViewLayer viewLayer) {
        ImageSampler sampler = this.imageSamplerMap.getImageSampler(samplerKey);
        this.view.putImageAtGrid(position, sampler, viewLayer);
    }

    public void putImageAtScreenCoords(
            Vector2D position,
            String samplerKey,
            ViewLayer viewLayer,
            float scale) {
        ImageSampler sampler = this.imageSamplerMap.getImageSampler(samplerKey);
        this.view.putImageAtScreenCoords(position, sampler, viewLayer, scale);
    }

    public void putTextAtScreenCoords(
            Vector2D position,
            String samplerKey,
            ViewLayer viewLayer,
            float scale,
            String text) {
        ImageAtlasSampler sampler = this.imageSamplerMap.getImageAtlasSampler(samplerKey);
        this.view.putTextAtScreenCoords(position, sampler, viewLayer, scale, text);
    }
}
