package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageSampler;

public class AnimalRenderer implements UnitRenderer<Animal> {
    private final ImageSampler[] frog;
    private final ImageSampler bar0;
    private final ImageSampler bar1;
    private final ImageSampler bar2;
    private final ImageSampler bar3;
    private final ImageSampler bar4;

    public AnimalRenderer(WorldRenderer renderer) {
        String[] colors = new String[]{"blue", "cyan", "green", "orange", "purple", "red", "yellow"};

        int choice = 3;

        String color = colors[choice];

        this.frog = new ImageSampler[]{
                renderer.imageSamplerMap.getImageSampler(color + "_frog4"),
                renderer.imageSamplerMap.getImageSampler(color + "_frog3"),
                renderer.imageSamplerMap.getImageSampler(color + "_frog2"),
                renderer.imageSamplerMap.getImageSampler(color + "_frog1"),
                renderer.imageSamplerMap.getImageSampler(color + "_frog0"),
                renderer.imageSamplerMap.getImageSampler(color + "_frog7"),
                renderer.imageSamplerMap.getImageSampler(color + "_frog6"),
                renderer.imageSamplerMap.getImageSampler(color + "_frog5")
        };
        this.bar0 = renderer.imageSamplerMap.getImageSampler("bar0");
        this.bar1 = renderer.imageSamplerMap.getImageSampler("bar1");
        this.bar2 = renderer.imageSamplerMap.getImageSampler("bar2");
        this.bar3 = renderer.imageSamplerMap.getImageSampler("bar3");
        this.bar4 = renderer.imageSamplerMap.getImageSampler("bar4");
    }

    @Override
    public void render(WorldRenderer renderer, Animal element) {
        Vector2D position = element.getPosition();
        renderer.worldView.putImageAtGrid(position, this.frog[element.getDirection().ordinal()]);
        ImageSampler bar = this.getEnergyBarImageKey(element.getEnergy());
        renderer.worldView.putImageAtGrid(position, bar);
    }

    private ImageSampler getEnergyBarImageKey(int energy) {
        return switch (energy) {
            case 0 -> this.bar0;
            case 1 -> this.bar1;
            case 2 -> this.bar2;
            case 3 -> this.bar3;
            default -> this.bar4;
        };
    }
}
