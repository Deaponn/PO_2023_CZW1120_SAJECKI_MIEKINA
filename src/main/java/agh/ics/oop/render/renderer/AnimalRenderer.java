package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageSampler;

public class AnimalRenderer implements UnitRenderer<Animal> {
    private final ImageSampler blob1;
    private final ImageSampler bar0;
    private final ImageSampler bar1;
    private final ImageSampler bar2;
    private final ImageSampler bar3;
    private final ImageSampler bar4;
    private final ImageSampler arrown;
    private final ImageSampler arrowne;
    private final ImageSampler arrowe;
    private final ImageSampler arrowse;
    private final ImageSampler arrows;
    private final ImageSampler arrowsw;
    private final ImageSampler arroww;
    private final ImageSampler arrownw;


    public AnimalRenderer(WorldRenderer renderer) {
        this.blob1 = renderer.imageSamplerMap.getImageSampler("blob1");

        this.bar0 = renderer.imageSamplerMap.getImageSampler("bar0");
        this.bar1 = renderer.imageSamplerMap.getImageSampler("bar1");
        this.bar2 = renderer.imageSamplerMap.getImageSampler("bar2");
        this.bar3 = renderer.imageSamplerMap.getImageSampler("bar3");
        this.bar4 = renderer.imageSamplerMap.getImageSampler("bar4");

        this.arrown = renderer.imageSamplerMap.getImageSampler("arrown");
        this.arrowne = renderer.imageSamplerMap.getImageSampler("arrowne");
        this.arrowe = renderer.imageSamplerMap.getImageSampler("arrowe");
        this.arrowse = renderer.imageSamplerMap.getImageSampler("arrowse");
        this.arrows = renderer.imageSamplerMap.getImageSampler("arrows");
        this.arrowsw = renderer.imageSamplerMap.getImageSampler("arrowsw");
        this.arroww = renderer.imageSamplerMap.getImageSampler("arroww");
        this.arrownw = renderer.imageSamplerMap.getImageSampler("arrownw");
    }

    @Override
    public void render(WorldRenderer renderer, Animal element) {
        Vector2D position = element.getPosition();
        renderer.worldView.putImageAtGrid(position, this.blob1);
        ImageSampler bar = this.getEnergyBarImageSampler(element.getEnergy());
        renderer.worldView.putImageAtGrid(position, bar);
        ImageSampler arrow = this.getArrowImageSampler(element.getDirection());
        renderer.worldView.putImageAtGrid(position, arrow);
    }

    private ImageSampler getEnergyBarImageSampler(int energy) {
        return switch (energy) {
            case 0 -> this.bar0;
            case 1 -> this.bar1;
            case 2 -> this.bar2;
            case 3 -> this.bar3;
            default -> this.bar4;
        };
    }

    private ImageSampler getArrowImageSampler(MapDirection mapDirection) {
        return switch (mapDirection) {
            case NORTH -> this.arrown;
            case NORTH_EAST -> this.arrowne;
            case EAST -> this.arrowe;
            case SOUTH_EAST -> this.arrowse;
            case SOUTH -> this.arrows;
            case SOUTH_WEST -> this.arrowsw;
            case WEST -> this.arroww;
            case NORTH_WEST -> this.arrownw;
        };
    }
}
