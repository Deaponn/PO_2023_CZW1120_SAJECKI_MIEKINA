package agh.ics.oop.view;

import agh.ics.oop.render.image.ImageSampler;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ViewLayer {
    protected WritableImage buffer;
    protected int bufferWidth;
    protected int bufferHeight;
    protected PixelWriter bufferPixelWriter;
    protected PixelReader bufferPixelReader;

    public ViewLayer() {
        this.bufferWidth = 0;
        this.bufferHeight = 0;
    }

    public void updateBuffer(int bufferWidth, int bufferHeight) {
        this.bufferWidth = bufferWidth;
        this.bufferHeight = bufferHeight;
        this.createBuffer();
    }

    public synchronized void createBuffer() {
        this.buffer = new WritableImage(this.bufferWidth, this.bufferHeight);
        this.bufferPixelWriter = this.buffer.getPixelWriter();
        this.bufferPixelReader = this.buffer.getPixelReader();
    }

    public WritableImage getBuffer() {
        return this.buffer;
    }

    public void rasterizeSamplerScaled(ImageSampler sampler, float x, float y, float scale) {
        float imageWidth = (float) sampler.getWidth();
        float imageHeight = (float) sampler.getHeight();
        // absolute ends of (0, imageScale) drawing area
        float ex = x + imageWidth * scale;
        float ey = y + imageHeight * scale;
        // image source pixel position
        float ix = 0f;
        float iy = 0f;
        // image source pixel step deltas
        float dx = 1f / scale;
        float dy = 1f / scale;

        for (float py = y; py < ey; py++) {
            for (float px = x; px < ex; px++) {
                Color c = sampler.getPixelAt((int) ix, (int) iy);
                this.compositePixel((int) px, (int) py, c);
                ix += dx;
                if (ix >= imageWidth) ix = imageWidth - 1;
            }
            ix = 0;
            iy += dy;
            if (iy >= imageHeight) iy = imageHeight - 1;
        }
    }

    public void rasterizeSamplerOpaqueScaled(ImageSampler sampler, float x, float y, float scale) {
        float imageWidth = (float) sampler.getWidth();
        float imageHeight = (float) sampler.getHeight();
        // absolute ends of (0, imageScale) drawing area
        float ex = x + imageWidth * scale;
        float ey = y + imageHeight * scale;
        // image source pixel position
        float ix = 0f;
        float iy = 0f;
        // image source pixel step deltas
        float dx = 1f / scale;
        float dy = 1f / scale;

        for (float py = y; py < ey; py++) {
            for (float px = x; px < ex; px++) {
                Color c = sampler.getPixelAt((int) ix, (int) iy);
                this.bufferPixelWriter.setColor((int) px, (int) py, c);
                ix += dx;
                if (ix >= imageWidth) ix = imageWidth - 1;
            }
            ix = 0;
            iy += dy;
            if (iy >= imageHeight) iy = imageHeight - 1;
        }
    }

    public void rasterizeSamplerAbsoluteSized(ImageSampler sampler, float x, float y, float size) {
        float imageWidth = (float) sampler.getWidth();
        float imageHeight = (float) sampler.getHeight();
        // absolute ends of (0, imageScale) drawing area
        float ex = x + size;
        float ey = y + size;
        // image source pixel position
        float ix = 0f;
        float iy = 0f;
        // image source pixel step deltas
        float dx = imageWidth / size;
        float dy = imageHeight / size;

        for (float py = y; py < ey; py++) {
            for (float px = x; px < ex; px++) {
                Color c = sampler.getPixelAt((int) ix, (int) iy);
                this.compositePixel((int) px, (int) py, c);
                ix += dx;
                if (ix >= imageWidth) ix = imageWidth - 1;
            }
            ix = 0;
            iy += dy;
            if (iy >= imageHeight) iy = imageHeight - 1;
        }
    }

    private void compositePixel(int x, int y, Color color) {
        if (x < 0 || y < 0 || x >= this.bufferWidth || y >= this.bufferHeight)
            return;
        Color blendColor = color;
        double opacity = color.getOpacity();
        if (opacity < 1) {
            Color prevColor = this.bufferPixelReader.getColor(x, y);
            blendColor = ViewLayer.blendOver(prevColor, color, opacity);
        }
        this.bufferPixelWriter.setColor(x, y, blendColor);
    }

    private static Color blendOver(Color colorOver, Color colorTop, double mixTop) {
        double mixOver = 1 - mixTop;
        double r = colorOver.getRed() * mixOver + colorTop.getRed() * mixTop;
        double g = colorOver.getGreen() * mixOver + colorTop.getGreen() * mixTop;
        double b = colorOver.getBlue() * mixOver + colorTop.getBlue() * mixTop;
        double a = mixTop + colorOver.getOpacity() * (1 - mixTop);
        return new Color(r, g, b, a);
    }
}
