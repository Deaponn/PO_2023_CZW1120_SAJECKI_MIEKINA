package agh.ics.oop.view;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.OutOfMapBoundsException;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.image.ImageAtlasSampler;
import agh.ics.oop.render.image.ImageSampler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.nio.charset.StandardCharsets;

public class CanvasWorldView implements WorldView<Canvas> {
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private WritableImage buffer;
    private int bufferWidth;
    private int bufferHeight;
    private PixelWriter bufferPixelWriter;
    private PixelReader bufferPixelReader;

    private Boundary gridBounds;
    private Float[] gridOffsetX;
    private Float[] gridOffsetY;
    private float gridImageSize;

    public CanvasWorldView(Canvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = this.canvas.getGraphicsContext2D();
        this.registerSizeListener();
        this.gridBounds = new Boundary(new Vector2D(), new Vector2D());
    }

    private synchronized void createBuffer(float width, float height) {
        this.bufferWidth = (int) Math.ceil(width);
        this.bufferHeight = (int) Math.ceil(height);
        this.buffer = new WritableImage(this.bufferWidth, this.bufferHeight);
        this.bufferPixelWriter = this.buffer.getPixelWriter();
        this.bufferPixelReader = this.buffer.getPixelReader();
    }

    @Override
    public void setGridBounds(Boundary bounds) {
        this.gridBounds = bounds;
        this.updateViewSize();
    }

    @Override
    public void updateViewSize() {
        float width = (float) this.canvas.getWidth();
        float height = (float) this.canvas.getHeight();
        if (width <= 0 || height <= 0)
            return;
        this.createBuffer(width, height);
        this.updateImageScale(width, height);
        this.updateGridOffsets(width, height);
    }

    @Override
    public void putImageAtGrid(
            Vector2D gridPosition,
            ImageSampler sampler) throws OutOfMapBoundsException {
        this.checkIfInBounds(gridPosition);
        float x = this.gridOffsetX[gridPosition.getX()];
        float y = this.gridOffsetY[gridPosition.getY()];

        this.rasterizeImageAbsoluteSized(sampler, x, y, this.gridImageSize);
    }

    @Override
    public void putImageAtScreenCoords(
            Vector2D screenPosition,
            ImageSampler sampler,
            float scale) {
        float x = screenPosition.getX();
        float y = screenPosition.getY();

        this.rasterizeImageScaled(sampler, x, y, scale);
    }

    @Override
    public void putTextAtScreenCoords(
            Vector2D screenPosition,
            ImageAtlasSampler sampler,
            float scale,
            String text) {
        float x = screenPosition.getX();
        float y = screenPosition.getY();

        float dx = sampler.getTileWidth() * scale;

        for (byte sym : text.getBytes(StandardCharsets.US_ASCII)) {
            ImageSampler glyphSampler = sampler.getTileSampler(sym);
            System.out.println(sym);
            this.rasterizeImageScaled(glyphSampler, x, y, scale);
            x += dx;
        }
    }

    /**
     * double-buffer draw view (VSYNC)
     */
    @Override
    public synchronized void presentView() {
        this.graphicsContext.clearRect(
                0, 0,
                this.canvas.getWidth(), this.canvas.getHeight());
        this.graphicsContext.drawImage(this.buffer, 0, 0);
    }

    @Override
    public Canvas getRoot() {
        return this.canvas;
    }

    private void rasterizeImageScaled(ImageSampler sampler, float x, float y, float imageScale) {
        float imageWidth = (float) sampler.getWidth();
        float imageHeight = (float) sampler.getHeight();
        // absolute ends of (0, imageScale) drawing area
        float ex = x + imageWidth * imageScale;
        float ey = y + imageHeight * imageScale;
        // image source pixel position
        float ix = 0f;
        float iy = 0f;
        // image source pixel step deltas
        float dx = 1f / imageScale;
        float dy = 1f / imageScale;

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

    private void rasterizeImageAbsoluteSized(ImageSampler sampler, float x, float y, float imageSize) {
        float imageWidth = (float) sampler.getWidth();
        float imageHeight = (float) sampler.getHeight();
        // absolute ends of (0, imageScale) drawing area
        float ex = x + imageSize;
        float ey = y + imageSize;
        // image source pixel position
        float ix = 0f;
        float iy = 0f;
        // image source pixel step deltas
        float dx = imageWidth / imageSize;
        float dy = imageHeight / imageSize;

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
            blendColor = CanvasWorldView.blendOver(prevColor, color, opacity);
        }
        this.bufferPixelWriter.setColor(x, y, blendColor);
    }

    private static Color blendOver(Color colorOver, Color colorTop, double mixTop) {
        double mixOver = 1 - mixTop;
        double r = colorOver.getRed() * mixOver + colorTop.getRed() * mixTop;
        double g = colorOver.getGreen() * mixOver + colorTop.getGreen() * mixTop;
        double b = colorOver.getBlue() * mixOver + colorTop.getBlue() * mixTop;
        return new Color(r, g, b, 1);
    }

    private void checkIfInBounds(Vector2D position) throws OutOfMapBoundsException {
        if (!this.gridBounds.isInBounds(position))
            throw new OutOfMapBoundsException(position);
    }

    private void registerSizeListener() {
        this.canvas.widthProperty()
                .addListener((observable, previousValue, newValue) -> CanvasWorldView.this.updateViewSize());
        this.canvas.heightProperty()
                .addListener((observable, previousValue, newValue) -> CanvasWorldView.this.updateViewSize());
    }

    private void updateImageScale(float width, float height) {
        Vector2D gridSize = this.gridBounds.getSize();
        float imageWidth = width / gridSize.getX();
        float imageHeight = height / gridSize.getY();
        this.gridImageSize = Math.min(imageWidth, imageHeight);
    }

    private void updateGridOffsets(float width, float height) {
        Vector2D gridSize = gridBounds.getSize();
        float padX = width - gridSize.getX() * this.gridImageSize;
        float padY = height - gridSize.getY() * this.gridImageSize;
        float startX = padX / 2;
        float startY = padY / 2;

        this.gridOffsetX = this.gridBounds
                .mapColumns(x -> startX + x * this.gridImageSize)
                .toArray(Float[]::new);
        this.gridOffsetY = this.gridBounds
                .mapRows(y -> startY + y * this.gridImageSize)
                .toArray(Float[]::new);
    }
}
