package agh.ics.oop.render.renderer;

import agh.ics.oop.render.TextOverlay;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;

public class TextOverlayRenderer implements UnitRenderer<TextOverlay> {
    @Override
    public void render(WorldRenderer renderer, TextOverlay overlay) {
        renderer.putTextAtScreenCoords(overlay.screenPosition, overlay.text);
    }
}
