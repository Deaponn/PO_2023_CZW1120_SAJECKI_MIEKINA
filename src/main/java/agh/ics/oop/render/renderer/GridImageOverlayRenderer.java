package agh.ics.oop.render.renderer;

import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.overlay.GridImageOverlay;

public class GridImageOverlayRenderer implements UnitRenderer<GridImageOverlay> {
    public GridImageOverlayRenderer(WorldRenderer renderer) {}

    @Override
    public void render(WorldRenderer renderer, GridImageOverlay overlay) {
        renderer.putImageAtGrid(
                overlay.gridPosition.getValue(),
                overlay.samplerKey.getValue()
        );
    }
}
