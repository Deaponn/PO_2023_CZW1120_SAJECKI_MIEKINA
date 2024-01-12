package agh.ics.oop.render.renderer;

import agh.ics.oop.entities.Ground;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.WorldElementRenderer;
import agh.ics.oop.render.WorldRenderer;

import java.util.Random;

public class GroundRenderer extends WorldElementRenderer<Ground> {
    private final Random random = new Random();

    @Override
    public void render(WorldRenderer renderer, Ground element) {
        Vector2D position = element.getPosition();
        random.setSeed((position.getX() * 7193L + position.getY() * 37L) * 9000L);
        int frame = random.nextInt(16);

        renderer.putImage(position, GroundRenderer.frames[frame]);
    }

    private static final String[] frames = new String[]{
            "sand4", "sand4", "sand4", "sand1", "sand1", "sand1", "sand1", "sand1",
            "sand3", "sand3", "sand2", "sand2", "sand2", "sand2", "sand2", "sand2"
    };
}
