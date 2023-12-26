package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Plant;
import agh.ics.oop.render.WorldElementRenderer;
import agh.ics.oop.render.WorldRenderer;

public class PlantRenderer extends WorldElementRenderer<Plant> {
    @Override
    public void render(WorldRenderer renderer, Plant element) {
        renderer.putImage(element.getPosition(), "sand3");
    }
}
