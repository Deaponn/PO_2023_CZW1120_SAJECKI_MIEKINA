package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Ground;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageSampler;

import java.util.Random;

public class GroundRenderer implements UnitRenderer<Ground> {
    private final Random random = new Random();

    private final ImageSampler[] frameSamplerArray;

    public GroundRenderer(WorldRenderer renderer) {
        ImageSampler sand1 = renderer.imageSamplerMap.getImageSampler("sand1");
        ImageSampler sand2 = renderer.imageSamplerMap.getImageSampler("sand2");
        ImageSampler sand3 = renderer.imageSamplerMap.getImageSampler("sand3");
        ImageSampler sand4 = renderer.imageSamplerMap.getImageSampler("sand4");

        this.frameSamplerArray = new ImageSampler[]{
                sand4, sand4, sand4, sand4,
                sand1, sand1, sand1, sand1,
                sand3, sand3, sand2, sand2,
                sand2, sand2, sand2, sand2
        };
    }

    @Override
    public void render(WorldRenderer renderer, Ground element) {
        Vector2D position = element.getPosition();
        int frame = this.getFrame(position);
        renderer.worldView.putImageAtGrid(position, this.frameSamplerArray[frame]);
    }

    private int getFrame(Vector2D position) {
        random.setSeed((position.getX() * 7193L + position.getY() * 37L) * 9000L);
        return random.nextInt(16);
    }
}
