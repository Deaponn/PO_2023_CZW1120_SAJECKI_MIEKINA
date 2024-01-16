package agh.ics.oop.render.overlay;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.ImageOverlay;
import agh.ics.oop.render.WorldRenderer;

public class BouncingImageOverlay extends ImageOverlay {
    private Vector2D velocity;

    public BouncingImageOverlay(Vector2D screenPosition, String imageKey, float imageScale) {
        super(screenPosition, imageKey, imageScale);
        this.velocity = new Vector2D();
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    @Override
    public void updateOnFrame(WorldRenderer renderer) {
        this.screenPosition.add(this.velocity);
    }
}
