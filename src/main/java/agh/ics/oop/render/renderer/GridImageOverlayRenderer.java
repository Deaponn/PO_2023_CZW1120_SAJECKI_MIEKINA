package agh.ics.oop.render.renderer;

import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.overlay.GridImageOverlay;
import agh.ics.oop.view.ViewLayer;

public class GridImageOverlayRenderer implements UnitRenderer<GridImageOverlay> {
    public GridImageOverlayRenderer(WorldRenderer renderer) {}

    @Override
    public void render(WorldRenderer renderer, ViewLayer viewLayer, GridImageOverlay overlay) {
        if (!overlay.isVisible.getValue()) return;
        renderer.putImageAtGrid(
                overlay.gridPosition.getValue(),
                overlay.samplerKey.getValue(),
                viewLayer
        );
    }
}
