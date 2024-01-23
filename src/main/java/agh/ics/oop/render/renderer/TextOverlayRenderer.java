package agh.ics.oop.render.renderer;

import agh.ics.oop.render.TextOverlay;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.view.ViewLayer;

public class TextOverlayRenderer implements UnitRenderer<TextOverlay> {
    public TextOverlayRenderer(WorldRenderer renderer) {}

    @Override
    public void render(WorldRenderer renderer, ViewLayer viewLayer, TextOverlay overlay) {
        renderer.putTextAtScreenCoords(
                overlay.screenPosition,
                overlay.samplerKey.getValue(),
                viewLayer,
                overlay.fontScale.getValue(),
                overlay.text.getValue());
    }
}
