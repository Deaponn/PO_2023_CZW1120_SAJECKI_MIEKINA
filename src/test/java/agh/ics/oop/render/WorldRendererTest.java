package agh.ics.oop.render;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldElementA;
import agh.ics.oop.model.WorldElementB;
import agh.ics.oop.render.image.ImageMap;
import agh.ics.oop.view.CanvasView;
import javafx.scene.canvas.Canvas;
import org.junit.Assert;
import org.junit.Test;

public class WorldRendererTest {
    @Test
    public void renderCorrectElement() {
        WorldRenderer worldRenderer = this.getWorldRenderer();
        WorldElementA element = new WorldElementA(new Vector2D());
        try {
            worldRenderer.renderUnit(worldRenderer.worldViewLayer, element);
        } catch (IllegalRendererAssignment e) {
            Assert.fail("Renderer assignment failed: " + e);
        }
    }

    @Test
    public void renderInvalidElement() {
        WorldRenderer worldRenderer = this.getWorldRenderer();
        WorldElementB element = new WorldElementB(new Vector2D());
        Assert.assertThrows(
                IllegalRendererAssignment.class,
                () -> worldRenderer.renderUnit(worldRenderer.worldViewLayer, element));
    }

    private WorldRenderer getWorldRenderer() {
        ImageMap emptyImageMap = new ImageMap();
        CanvasView canvasView = new CanvasView(new Canvas(100, 100));
        return new WorldRenderer(emptyImageMap, canvasView);
    }
}
