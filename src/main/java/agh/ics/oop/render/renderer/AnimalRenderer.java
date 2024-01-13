package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;

public class AnimalRenderer implements UnitRenderer<Animal> {
    @Override
    public void render(WorldRenderer renderer, Animal element) {
        Vector2D position = element.getPosition();
        renderer.putImageAtGrid(position, "blob1");
        renderer.putImageAtGrid(position, AnimalRenderer.getEnergyBarImageKey(element.getEnergy()));
    }

    private static String getEnergyBarImageKey(int energy) {
        return switch (energy) {
            case 0 -> "bar0";
            case 1 -> "bar1";
            case 2 -> "bar2";
            case 3 -> "bar3";
            default -> "bar4";
        };
    }
}
