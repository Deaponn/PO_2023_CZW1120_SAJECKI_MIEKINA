package agh.ics.oop.render;

import agh.ics.oop.model.*;
import agh.ics.oop.view.WorldView;
import javafx.scene.image.Image;

import java.util.LinkedList;
import java.util.List;

public class WorldRenderer {
    public final ImageMap imageMap;
    public final WorldView worldView;
    public final List<Overlay> overlayList;
    private final UnitRendererAssignmentMap unitRendererAssignmentMap;

    public WorldRenderer(ImageMap imageMap, WorldView worldView) {
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
        this.worldView.presentView();
    }

    public void renderElement(WorldElement element) throws IllegalRendererAssignment {
        this.unitRendererAssignmentMap.renderElement(this, element);
    }

    private void tryRenderElementList(List<WorldElement> elementList) {
        elementList.forEach(this::tryRenderElement);
    }

    private void tryRenderElement(WorldElement element) {
        try {
            this.renderElement(element);
            System.out.println("Render element @ " + element.getPosition());
        } catch (IllegalRendererAssignment e) {
            System.out.println("WorldRenderer: " + e.getMessage());
        }
    }

    public void putImage(Vector2D position, String imageKey) {
        try {
            Image image = this.imageMap.getImage(imageKey);
            if (image == null)
                return;
            this.worldView.put(position, image);
        } catch (OutOfMapBoundsException e) {
            System.out.println("Attempted to write outside of bounds: " + e.getMessage());
        }
    }
}
