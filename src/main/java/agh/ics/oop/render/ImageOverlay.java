package agh.ics.oop.render;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.renderer.ImageOverlayRenderer;

@AssignRenderer(renderer = ImageOverlayRenderer.class)
public class ImageOverlay extends Overlay {
    public String imageKey;

    public ImageOverlay(Vector2D screenPosition, String imageKey) {
        super(screenPosition);
        this.imageKey = imageKey;
    }
}