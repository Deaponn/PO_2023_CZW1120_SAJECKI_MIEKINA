package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Plant;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageSampler;
import agh.ics.oop.view.ViewLayer;

public class PlantRenderer implements UnitRenderer<Plant> {
    private final ImageSampler plant1;

    public PlantRenderer(WorldRenderer renderer) {
        this.plant1 = renderer.imageSamplerMap.getImageSampler("plant1");
    }

    @Override
    public void render(WorldRenderer renderer, ViewLayer viewLayer, Plant element) {
        renderer.view.putImageAtGrid(
                element.getPosition(),
                this.plant1,
                viewLayer);
    }
}
