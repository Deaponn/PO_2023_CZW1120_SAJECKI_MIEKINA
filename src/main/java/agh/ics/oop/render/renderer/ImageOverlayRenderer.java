package agh.ics.oop.render.renderer;

import agh.ics.oop.render.ImageOverlay;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.view.ViewLayer;

public class ImageOverlayRenderer implements UnitRenderer<ImageOverlay> {
    public ImageOverlayRenderer(WorldRenderer renderer) {}

    @Override
    public void render(WorldRenderer renderer, ViewLayer viewLayer, ImageOverlay overlay) {
        if (!overlay.isVisible.getValue()) return;
        renderer.putImageAtScreenCoords(
                overlay.screenPosition,
                overlay.samplerKey.getValue(),
                viewLayer,
                overlay.scale.getValue());
    }
}
