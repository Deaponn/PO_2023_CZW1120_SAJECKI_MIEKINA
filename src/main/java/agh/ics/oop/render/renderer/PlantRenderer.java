package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Plant;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageAtlasSampler;
import agh.ics.oop.view.ViewLayer;

public class PlantRenderer implements UnitRenderer<Plant> {
    private final ImageAtlasSampler plantAtlas;

    public PlantRenderer(WorldRenderer renderer) {
        this.plantAtlas = renderer.imageSamplerMap.getImageAtlasSampler("plant_atlas");
    }

    @Override
    public void render(WorldRenderer renderer, ViewLayer viewLayer, Plant element) {
        Vector2D plantPosition = element.getPosition();
        long plantFrame = (
                plantPosition.getX() * 3L +
                plantPosition.getY() +
                renderer.frame) % 4;
        renderer.view.putImageAtGrid(
                element.getPosition(),
                this.plantAtlas.getTileSampler((int) plantFrame),
                viewLayer);
    }
}
