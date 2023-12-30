package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.WorldElementRenderer;
import agh.ics.oop.render.WorldRenderer;

public class AnimalRenderer extends WorldElementRenderer<Animal> {
    @Override
    public void render(WorldRenderer renderer, Animal element) {
        Vector2D position = element.getPosition();
        renderer.putImage(position, "DICK");
        renderer.putImage(position, AnimalRenderer.getEnergyBarImageKey(element.getEnergy()));
    }

    private static String getEnergyBarImageKey(int energy) {
        return switch (energy) {
            case 0 -> "TODO";
            case 1 -> "bar1";
            case 2 -> "bar2";
            case 3 -> "bar3";
            default -> "bar4";
        };
    }
}
