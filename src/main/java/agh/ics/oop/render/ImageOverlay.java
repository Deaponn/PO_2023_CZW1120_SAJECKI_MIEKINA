package agh.ics.oop.render;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.renderer.ImageOverlayRenderer;

@AssignRenderer(renderer = ImageOverlayRenderer.class)
public abstract class ImageOverlay extends Overlay {
    public String imageKey;
    public float imageScale;

    public ImageOverlay(Vector2D screenPosition, String imageKey, float imageScale) {
        super(screenPosition);
        this.imageKey = imageKey;
        this.imageScale = imageScale;
    }
}
