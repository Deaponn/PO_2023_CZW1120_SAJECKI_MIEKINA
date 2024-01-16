package agh.ics.oop.render;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.renderer.TextOverlayRenderer;

@AssignRenderer(renderer = TextOverlayRenderer.class)
public abstract class TextOverlay extends Overlay {
    public String samplerKey;
    public float fontScale;
    public String text;

    public TextOverlay(
            Vector2D screenPosition,
            String samplerKey,
            float fontScale,
            String text) {
        super(screenPosition);
        this.samplerKey = samplerKey;
        this.fontScale = fontScale;
        this.text = text;
    }
}