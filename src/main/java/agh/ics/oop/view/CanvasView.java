package agh.ics.oop.view;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.render.image.ImageAtlasSampler;
import agh.ics.oop.render.image.ImageSampler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class CanvasView implements View<Canvas> {
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private int bufferWidth;
    private int bufferHeight;

    private Collection<ViewLayer> viewLayerCollection;

    private Boundary gridBounds;
    private Float[] gridOffsetX;
    private Float[] gridOffsetY;
    private float gridImageSize;

    public CanvasView(Canvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = this.canvas.getGraphicsContext2D();
        this.registerSizeListener();
        this.gridBounds = new Boundary(new Vector2D(), new Vector2D());
    }

    public void withViewLayers(Collection<ViewLayer> viewLayerCollection) {
        this.viewLayerCollection = viewLayerCollection;
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

        this.bufferWidth = (int) Math.ceil(width);
        this.bufferHeight = (int) Math.ceil(height);
        this.viewLayerCollection.forEach(viewLayer ->
                viewLayer.updateBuffer(this.bufferWidth, this.bufferHeight));
        this.updateImageScale(width, height);
        this.updateGridOffsets(width, height);
    }

    @Override
    public Vector2D getViewSize() {
        return new Vector2D(this.bufferWidth, this.bufferHeight);
    }

    @Override
    public void putImageAtGrid(
            Vector2D gridPosition,
            ImageSampler sampler,
            ViewLayer viewLayer) {
        if (!this.gridBounds.isInBounds(gridPosition))
            return;
        float x = this.gridOffsetX[gridPosition.getX()];
        float y = this.gridOffsetY[gridPosition.getY()];

        viewLayer.rasterizeSamplerAbsoluteSized(sampler, x, y, this.gridImageSize);
    }

    @Override
    public void putImageAtScreenCoords(
            Vector2D screenPosition,
            ImageSampler sampler,
            ViewLayer viewLayer,
            float scale) {
        float x = screenPosition.getX();
        float y = screenPosition.getY();

        viewLayer.rasterizeSamplerScaled(sampler, x, y, scale);
    }

    @Override
    public void putTextAtScreenCoords(
            Vector2D screenPosition,
            ImageAtlasSampler sampler,
            ViewLayer viewLayer,
            float scale,
            String text) {
        float x = screenPosition.getX();
        float y = screenPosition.getY();
        float zx = x;

        float dx = sampler.getTileWidth() * scale;
        float dy = sampler.getTileHeight() * scale;

        for (byte sym : text.getBytes(StandardCharsets.US_ASCII)) {
            if (sym == '\n') {
                x = zx;
                y += dy;
            } else {
                ImageSampler glyphSampler = sampler.getTileSampler(sym);
                viewLayer.rasterizeSamplerOpaqueScaled(glyphSampler, x, y, scale);
                x += dx;
            }
        }
    }

    @Override
    public Vector2D getGridIndex(Vector2D position) {
        float offsetX = this.gridOffsetX[0];
        float offsetY = this.gridOffsetY[0];

        int x = position.getX();
        int y = position.getY();
        int ix = (int) ((x - offsetX) / this.gridImageSize);
        int iy = (int) ((y - offsetY) / this.gridImageSize);

        return new Vector2D(ix, iy);
    }

    /**
     * double-buffer draw view (VSYNC)
     */
    @Override
    public synchronized void presentView() {
        this.graphicsContext.clearRect(
                0, 0,
                this.canvas.getWidth(), this.canvas.getHeight());
        this.viewLayerCollection.forEach(viewLayer ->
                this.graphicsContext.drawImage(viewLayer.getBuffer(), 0, 0));
    }

    @Override
    public Canvas getRoot() {
        return this.canvas;
    }

    private void registerSizeListener() {
        this.canvas.widthProperty()
                .addListener((observable, previousValue, newValue) -> CanvasView.this.updateViewSize());
        this.canvas.heightProperty()
                .addListener((observable, previousValue, newValue) -> CanvasView.this.updateViewSize());
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
