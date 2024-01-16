package agh.ics.oop.render.overlay;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.ImageOverlay;
import agh.ics.oop.render.WorldRenderer;

public class StaticImageOverlay extends ImageOverlay {
    public StaticImageOverlay(Vector2D screenPosition, String imageKey, float imageScale) {
        super(screenPosition, imageKey, imageScale);
    }

    @Override
    public void updateOnFrame(WorldRenderer renderer) {}
}
