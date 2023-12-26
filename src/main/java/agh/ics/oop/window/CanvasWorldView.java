package agh.ics.oop.window;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.OutOfMapBoundsException;
import agh.ics.oop.model.Vector2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class CanvasWorldView implements WorldView {
    private final Canvas canvas;
    private final PixelWriter canvasPixelWriter;

    private Boundary gridBounds;
    private Float[] gridOffsetX;
    private Float[] gridOffsetY;
    private float imageScale;

    public CanvasWorldView(Canvas canvas) {
        this.canvas = canvas;
        this.registerSizeListener();
        this.canvasPixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        this.gridBounds = new Boundary(new Vector2D(), new Vector2D());
    }

    @Override
    public void setGridBounds(Vector2D size) {
        this.gridBounds = Boundary.fromSize(size);
        this.updateViewSize();
    }

    @Override
    public void setGridBounds(Boundary bounds) {
        this.gridBounds = bounds;
        this.updateViewSize();
    }

    @Override
    public void updateViewSize() {
        double width = this.canvas.getWidth();
        double height = this.canvas.getHeight();
//        this.graphicsContext.clearRect(0, 0, width, height);
        this.updateImageScale((float) width, (float) height);
        this.updateGridOffsets((float) width, (float) height);
    }

    @Override
    public void put(Vector2D position, Image image) throws OutOfMapBoundsException {
        this.checkIfInBounds(position);
        this.drawAtGrid(position, image);
    }

    @Override
    public Image get(Vector2D position) throws OutOfMapBoundsException {
        this.checkIfInBounds(position);
        return null;
    }

    private void drawAtGrid(Vector2D position, Image image) {
        float x = this.gridOffsetX[position.getX()];
        float y = this.gridOffsetY[position.getY()];

        this.renderImage(image, x, y, this.imageScale);
    }

    private void renderImage(Image image, float x, float y, float imageScale) {
        PixelReader pixelReader = image.getPixelReader();
        float imageWidth = (float) image.getWidth();
        float imageHeight = (float) image.getHeight();
        float ex = x + imageScale;
        float ey = y + imageScale;
        float ix = 0f;
        float iy = 0f;
        float dx = imageWidth / imageScale;
        float dy = imageHeight / imageScale;

        for (float py = y; py < ey; py++) {
            for (float px = x; px < ex; px++) {
                Color c = pixelReader.getColor((int) ix, (int) iy);
                this.canvasPixelWriter.setColor((int) px, (int) py, c);
                ix += dx;
                if (ix >= 15f) ix = imageWidth - 1;
            }
            ix = 0;
            iy += dy;
            if (iy >= 15f) iy = imageHeight - 1;
        }
    }

    private void checkIfInBounds(Vector2D position) throws OutOfMapBoundsException {
        if (!this.gridBounds.isInBounds(position))
            throw new OutOfMapBoundsException(position);
    }

    private void registerSizeListener() {
        this.canvas.widthProperty()
                .addListener((observable, previous_value, current_value) -> CanvasWorldView.this.updateViewSize());
        this.canvas.heightProperty()
                .addListener((observable, previous_value, current_value) -> CanvasWorldView.this.updateViewSize());
    }

    private void updateImageScale(float width, float height) {
        Vector2D gridSize = this.gridBounds.getSize();
        float imageWidth = width / gridSize.getX();
        float imageHeight = height / gridSize.getY();
        this.imageScale = Math.min(imageWidth, imageHeight);
    }

    private void updateGridOffsets(float width, float height) {
        Vector2D gridSize = gridBounds.getSize();
        float padX = width - gridSize.getX() * this.imageScale;
        float padY = height - gridSize.getY() * this.imageScale;
        float startX = padX / 2;
        float startY = padY / 2;

        this.gridOffsetX = this.gridBounds
                .mapColumns(x -> startX + x * this.imageScale)
                .toArray(Float[]::new);
        this.gridOffsetY = this.gridBounds
                .mapRows(y -> startY + y * this.imageScale)
                .toArray(Float[]::new);
    }
}
