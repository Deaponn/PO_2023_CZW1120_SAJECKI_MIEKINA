package agh.ics.oop.render;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.image.ImageAtlasSampler;
import agh.ics.oop.render.image.ImageMap;
import agh.ics.oop.render.image.ImageSampler;
import agh.ics.oop.render.image.ImageSamplerMap;
import agh.ics.oop.view.WorldView;
import javafx.application.Platform;

import java.util.LinkedList;
import java.util.List;

public class WorldRenderer {
    public final ImageSamplerMap imageSamplerMap;
    public final WorldView<?> worldView;
    public final List<Overlay> overlayList;
    private final UnitRendererAssignmentMap unitRendererAssignmentMap;
    private WorldMap worldMap;

    public WorldRenderer(ImageMap imageMap, WorldView<?> worldView) {
        this.imageSamplerMap = new ImageSamplerMap(imageMap);
        this.worldView = worldView;
        this.overlayList = new LinkedList<>();
        this.unitRendererAssignmentMap = new UnitRendererAssignmentMap();
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public synchronized void renderView() {
        Platform.runLater(() -> {
            long startNanoTime = System.nanoTime();
            Boundary bounds = this.worldMap.getCurrentBounds();
            this.worldView.setGridBounds(bounds);
            bounds.mapAllPositions(this.worldMap::getElements)
                    .forEach(this::tryRender);
            this.overlayList.forEach(this::tryRender);
            this.worldView.presentView();
            long endNanoTime = System.nanoTime();
            System.out.println("Render execution time: " + (endNanoTime - startNanoTime) / 1_000_000L + "ms");
        });
    }

    public void renderUnit(Object unit) throws IllegalRendererAssignment {
        this.unitRendererAssignmentMap.renderUnit(this, unit);
    }

    private <U> void tryRender(List<U> unitList) {
        unitList.forEach(this::tryRender);
    }

    private <U> void tryRender(U unit) {
        try {
            this.renderUnit(unit);
        } catch (IllegalRendererAssignment e) {
            System.out.println("WorldRenderer: " + e.getMessage());
        }
    }

    public void putImageAtGrid(Vector2D position, String samplerKey) {
        ImageSampler sampler = this.imageSamplerMap.getImageSampler(samplerKey);
        this.worldView.putImageAtGrid(position, sampler);
    }

    public void putImageAtScreenCoords(Vector2D position, String samplerKey, float scale) {
        ImageSampler sampler = this.imageSamplerMap.getImageSampler(samplerKey);
        this.worldView.putImageAtScreenCoords(position, sampler, scale);
    }

    public void putTextAtScreenCoords(
            Vector2D position,
            String samplerKey,
            float scale,
            String text) {
        ImageAtlasSampler sampler = this.imageSamplerMap.getImageAtlasSampler(samplerKey);
        this.worldView.putTextAtScreenCoords(position, sampler, scale, text);
    }
}
