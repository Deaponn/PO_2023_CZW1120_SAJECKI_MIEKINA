package agh.ics.oop.render;

import agh.ics.oop.model.*;
import agh.ics.oop.view.WorldView;
import javafx.scene.image.Image;

import java.util.LinkedList;
import java.util.List;

public class WorldRenderer {
    public final ImageMap imageMap;
    public final WorldView<?> worldView;
    public final List<Overlay> overlayList;
    private final UnitRendererAssignmentMap unitRendererAssignmentMap;

    public WorldRenderer(ImageMap imageMap, WorldView<?> worldView) {
        this.imageMap = imageMap;
        this.worldView = worldView;
        this.overlayList = new LinkedList<>();
        this.unitRendererAssignmentMap = new UnitRendererAssignmentMap();
    }

    public void renderView(WorldMap worldMap) {
        Boundary bounds = worldMap.getCurrentBounds();
        this.worldView.setGridBounds(bounds);
        bounds.mapAllPositions(worldMap::getElements)
                .forEach(this::tryRenderElementList);
        overlayList.forEach(this::tryRenderOverlay);
        this.worldView.presentView();
    }

    public void renderUnit(Object unit) throws IllegalRendererAssignment {
        this.unitRendererAssignmentMap.renderUnit(this, unit);
    }

    private void tryRenderElementList(List<WorldElement> elementList) {
        elementList.forEach(this::tryRenderElement);
    }

    private void tryRenderElement(WorldElement element) {
        try {
            this.renderUnit(element);
            System.out.println("Render element @ " + element.getPosition());
        } catch (IllegalRendererAssignment e) {
            System.out.println("WorldRenderer: " + e.getMessage());
        }
    }

    private void tryRenderOverlay(Overlay overlay) {
        try {
            this.renderUnit(overlay);
        } catch (IllegalRendererAssignment e) {
            System.out.println("WorldRenderer: " + e.getMessage());
        }
    }

    public void putImageAtGrid(Vector2D position, String imageKey) {
        try {
            Image image = this.imageMap.getImage(imageKey);
            if (image == null)
                return;
            this.worldView.putImageAtGrid(position, image);
        } catch (OutOfMapBoundsException e) {
            System.out.println("Attempted to write outside of bounds: " + e.getMessage());
        }
    }

    public void putImageAtScreenCoords(Vector2D position, String imageKey, float scale) {
        Image image = this.imageMap.getImage(imageKey);
        if (image == null)
            return;
        this.worldView.putImageAtScreenCoords(position, image, scale);
    }

    public void putTextAtScreenCoords(Vector2D position, String text) {
        this.worldView.putTextAtScreenCoords(position, text);
    }
}
