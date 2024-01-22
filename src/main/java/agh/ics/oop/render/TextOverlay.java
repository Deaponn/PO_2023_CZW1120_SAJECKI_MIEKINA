package agh.ics.oop.render;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.renderer.TextOverlayRenderer;
import agh.ics.oop.util.Reactive;

@AssignRenderer(renderer = TextOverlayRenderer.class)
public abstract class TextOverlay extends Overlay {
    public final Reactive<String> samplerKey;
    public final Reactive<Float> fontScale;
    public final Reactive<String> text;

    public TextOverlay(
            Vector2D screenPosition,
            String samplerKey,
            float fontScale,
            String text) {
        super(screenPosition);
        this.samplerKey = new Reactive<>(samplerKey);
        this.fontScale = new Reactive<>(fontScale);
        this.text = new Reactive<>(text);
    }
}
