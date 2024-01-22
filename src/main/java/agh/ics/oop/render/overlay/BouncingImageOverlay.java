package agh.ics.oop.render.overlay;

import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.ImageOverlay;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageSampler;

public class BouncingImageOverlay extends ImageOverlay {
    private Vector2D velocity;

    public BouncingImageOverlay(Vector2D screenPosition, String samplerKey, float scale) {
        super(screenPosition, samplerKey, scale);
        this.velocity = new Vector2D();
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    @Override
    public void updateOnFrame(WorldRenderer renderer) {
        Vector2D viewSize = renderer.worldView.getViewSize();
        String samplerKey = this.samplerKey.getValue();
        float scale = this.scale.getValue();

        int viewWidth = viewSize.getX();
        int viewHeight = viewSize.getY();
        ImageSampler sampler = renderer.imageSamplerMap.getImageSampler(samplerKey);
        int samplerWidth = sampler.getWidth();
        int samplerHeight = sampler.getHeight();

        int x = this.screenPosition.getX();
        int y = this.screenPosition.getY();

        if (x > viewWidth - samplerWidth * scale || x < 0) {
            this.velocity = this.velocity.multiply(new Vector2D(-1, 1));
        }
        if (y > viewHeight - samplerHeight * scale || y < 0) {
            this.velocity = this.velocity.multiply(new Vector2D(1, -1));
        }

        this.screenPosition = this.screenPosition.add(this.velocity);
    }
}
