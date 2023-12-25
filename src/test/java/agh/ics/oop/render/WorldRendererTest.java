package agh.ics.oop.render;

import agh.ics.oop.model.WorldElementA;
import agh.ics.oop.model.WorldElementB;
import org.junit.Assert;
import org.junit.Test;

public class WorldRendererTest {
    @Test
    public void renderCorrectElement() {
        WorldRenderer worldRenderer = this.getWorldRenderer();
        WorldElementA element = new WorldElementA();
        try {
            worldRenderer.renderElement(element);
        } catch (IllegalRendererAssignment e) {
            Assert.fail("Renderer assignment failed: " + e);
        }
    }

    @Test
    public void renderInvalidElement() {
        WorldRenderer worldRenderer = this.getWorldRenderer();
        WorldElementB element = new WorldElementB();
        Assert.assertThrows(IllegalRendererAssignment.class, () -> worldRenderer.renderElement(element));
    }

    private WorldRenderer getWorldRenderer() {
        ImageMap emptyImageMap = new ImageMap();
        return new WorldRenderer(emptyImageMap, null);
    }
}
