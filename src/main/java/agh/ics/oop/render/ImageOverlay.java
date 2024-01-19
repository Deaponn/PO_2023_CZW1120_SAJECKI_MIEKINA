package agh.ics.oop.render;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.renderer.ImageOverlayRenderer;

@AssignRenderer(renderer = ImageOverlayRenderer.class)
public abstract class ImageOverlay extends Overlay {
    public String samplerKey;
    public float scale;

    public ImageOverlay(Vector2D screenPosition, String samplerKey, float scale) {
        super(screenPosition);
        this.samplerKey = samplerKey;
        this.scale = scale;
    }
}
