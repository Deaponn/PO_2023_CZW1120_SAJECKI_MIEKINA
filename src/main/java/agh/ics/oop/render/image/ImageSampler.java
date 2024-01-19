package agh.ics.oop.render.image;

import agh.ics.oop.model.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class ImageSampler {
    private final Image image;
    private final PixelReader pixelReader;

    private Vector2D zero = new Vector2D();
    private Vector2D size = new Vector2D();

    private int zx, zy;
    private int sx, sy;

    public ImageSampler(Image image) {
        this.image = image;
        this.pixelReader = image.getPixelReader();
        this.setZero(new Vector2D());
        this.setSize(this.getImageSize());
    }

    public ImageSampler(Image image, Vector2D zero, Vector2D size) {
        this.image = image;
        this.pixelReader = image.getPixelReader();
        this.setZero(zero);
        this.setSize(size);
    }

    public Color getPixelAt(int x, int y) {
        if (x < 0 || y < 0 || x >= sx || y >= sy) return Color.BLACK;
        return this.pixelReader.getColor(zx + x, zy + y);
    }

    public void setZero(Vector2D zero) {
        Vector2D maxZero = zero.lowerLeft(this.getImageMaxZero());
        this.zero = maxZero;
        this.zx = maxZero.getX();
        this.zy = maxZero.getY();
        this.setSize(this.size);
    }

    public void setSize(Vector2D size) {
        Vector2D maxSize = size.lowerLeft(this.getImageSize().subtract(this.zero));
        this.size = maxSize;
        this.sx = maxSize.getX();
        this.sy = maxSize.getY();
    }

    public int getWidth() {
        return this.sx;
    }

    public int getHeight() {
        return this.sy;
    }

    private Vector2D getImageSize() {
        return new Vector2D(
                (int) this.image.getWidth(),
                (int) this.image.getHeight());
    }

    private Vector2D getImageMaxZero() {
        return this.getImageSize().add(new Vector2D(-1, -1));
    }
}
