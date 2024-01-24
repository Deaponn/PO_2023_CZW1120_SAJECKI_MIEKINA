package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Ground;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.UnitRenderer;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageSampler;
import agh.ics.oop.view.ViewLayer;

import java.util.Random;

public class GroundRenderer implements UnitRenderer<Ground> {
    private final Random random = new Random();

    private final ImageSampler[] healthyFrameSamplerArray;

    private final ImageSampler[] poisonedFrameSamplerArray;

    public GroundRenderer(WorldRenderer renderer) {
        ImageSampler sand1 = renderer.imageSamplerMap.getImageSampler("sand1");
        ImageSampler sand2 = renderer.imageSamplerMap.getImageSampler("sand2");
        ImageSampler sand3 = renderer.imageSamplerMap.getImageSampler("sand3");
        ImageSampler sand4 = renderer.imageSamplerMap.getImageSampler("sand4");

        ImageSampler poisonedSand1 = renderer.imageSamplerMap.getImageSampler("poisoned_sand1");
        ImageSampler poisonedSand2 = renderer.imageSamplerMap.getImageSampler("poisoned_sand2");
        ImageSampler poisonedSand3 = renderer.imageSamplerMap.getImageSampler("poisoned_sand3");
        ImageSampler poisonedSand4 = renderer.imageSamplerMap.getImageSampler("poisoned_sand4");

        this.healthyFrameSamplerArray = new ImageSampler[]{
                sand4, sand4, sand4, sand4,
                sand1, sand1, sand1, sand1,
                sand3, sand3, sand2, sand2,
                sand2, sand2, sand2, sand2
        };

        this.poisonedFrameSamplerArray = new ImageSampler[]{
                poisonedSand4, poisonedSand4, poisonedSand4, poisonedSand4,
                poisonedSand1, poisonedSand1, poisonedSand1, poisonedSand1,
                poisonedSand3, poisonedSand3, poisonedSand2, poisonedSand2,
                poisonedSand2, poisonedSand2, poisonedSand2, poisonedSand2
        };
    }

    @Override
    public void render(WorldRenderer renderer, ViewLayer viewLayer, Ground element) {
        Vector2D position = element.getPosition();
        int frame = this.getFrame(position);
        if (!element.getIsPoisoned())
            renderer.view.putImageAtGrid(position, this.healthyFrameSamplerArray[frame], viewLayer);
        else
            renderer.view.putImageAtGrid(position, this.poisonedFrameSamplerArray[frame], viewLayer);
    }

    private int getFrame(Vector2D position) {
        random.setSeed((position.getX() * 7193L + position.getY() * 37L) * 9000L);
        return random.nextInt(16);
    }
}
