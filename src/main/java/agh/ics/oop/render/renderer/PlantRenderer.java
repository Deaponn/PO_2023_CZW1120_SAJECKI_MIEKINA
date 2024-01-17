package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Plant;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageSampler;

public class PlantRenderer implements UnitRenderer<Plant> {
    private final ImageSampler plant1;

    public PlantRenderer(WorldRenderer renderer) {
        this.plant1 = renderer.imageSamplerMap.getImageSampler("plant1");
    }

    @Override
    public void render(WorldRenderer renderer, Plant element) {
        renderer.worldView.putImageAtGrid(element.getPosition(), this.plant1);
    }
}
