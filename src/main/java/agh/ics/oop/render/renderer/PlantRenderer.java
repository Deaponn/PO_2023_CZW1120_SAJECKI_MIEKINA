package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Plant;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;

public class PlantRenderer extends UnitRenderer<Plant> {
    @Override
    public void render(WorldRenderer renderer, Plant element) {
        renderer.putImage(element.getPosition(), "plant1");
    }
}
