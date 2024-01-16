package agh.ics.oop.render.overlay;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.TextOverlay;
import agh.ics.oop.render.WorldRenderer;

public class StaticTextOverlay extends TextOverlay {
    public StaticTextOverlay(
            Vector2D screenPosition,
            String samplerKey,
            float fontScale,
            String text) {
        super(screenPosition, samplerKey, fontScale, text);
    }

    @Override
    public void updateOnFrame(WorldRenderer renderer) {}
}