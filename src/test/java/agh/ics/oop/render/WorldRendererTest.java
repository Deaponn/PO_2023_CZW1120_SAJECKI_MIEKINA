package agh.ics.oop.render;

import agh.ics.oop.model.WorldElementA;
import agh.ics.oop.model.WorldElementB;
import org.junit.Assert;
import org.junit.Test;

public class WorldRendererTest {
    @Test
    public void tryRegisterCorrectElementRenderer() {
        WorldRenderer worldRenderer = this.getWorldRenderer();
        WorldElementA element = new WorldElementA();
        try {
            WorldElementRenderer<WorldElementA> worldElementRenderer = worldRenderer.getElementRenderer(element);
            Assert.assertEquals(
                    WorldElementARenderer.class.getMethod("render",
                            WorldRenderer.class, WorldElementA.class),
                    worldElementRenderer.getClass().getMethod("render",
                            WorldRenderer.class, WorldElementA.class));
        } catch (IllegalRendererAssignment e) {
            Assert.fail("Renderer assignment shouldn't fail: " + e);
        } catch (NoSuchMethodException e) {
            Assert.fail("Renderer 'render' method is of wrong signature: " + e);
        }
    }

    @Test
    public void tryRegisterInvalidElementRender() {
        WorldRenderer worldRenderer = this.getWorldRenderer();
        WorldElementB element = new WorldElementB();
        try {
            WorldElementRenderer<WorldElementB> worldElementRenderer = worldRenderer.getElementRenderer(element);
            Assert.assertEquals(
                    WorldElementARenderer.class.getMethod("render",
                            WorldRenderer.class, WorldElementA.class),
                    worldElementRenderer.getClass().getMethod("render",
                            WorldRenderer.class, WorldElementA.class));
        } catch (IllegalRendererAssignment e) {
            Assert.fail("Renderer assignment shouldn't fail: " + e);
        } catch (NoSuchMethodException e) {
            Assert.fail("Renderer 'render' method is of wrong signature: " + e);
        }
    }

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
        ImageResourceAtlas emptyImageResourceAtlas = new ImageResourceAtlas();
        return new WorldRenderer(emptyImageResourceAtlas);
    }
}
