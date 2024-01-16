package agh.ics.oop.render.image;

import agh.ics.oop.model.Boundary;
import javafx.scene.image.Image;
import agh.ics.oop.model.Vector2D;

public class ImageAtlasSamplerFactory {
    private final Image image;
    private int tx;
    private int ty;

    private ImageSampler[] samplerArray;

    public ImageAtlasSamplerFactory(Image image, Vector2D tileSize) {
        this.image = image;
        this.setTileSize(tileSize);
    }

    public void setTileSize(Vector2D tileSize) {
        this.tx = tileSize.getX();
        this.ty = tileSize.getY();

        int tileCountWidth = (int) Math.ceil(image.getWidth() / tx);
        int tileCountHeight = (int) Math.ceil(image.getHeight() / ty);
        this.samplerArray = Boundary.fromSize(tileCountWidth, tileCountHeight)
                .mapAllPositions(index -> index.multiply(tileSize))
                .map(offset -> new ImageSampler(this.image, offset, tileSize))
                .toArray(ImageSampler[]::new);
    }

    public ImageSampler createTileSampler(int absoluteIndex) {
        return this.samplerArray[absoluteIndex];
    }
}
