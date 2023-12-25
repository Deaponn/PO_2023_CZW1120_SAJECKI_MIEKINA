package agh.ics.oop.window;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.OutOfMapBoundsException;
import agh.ics.oop.model.Vector2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class CanvasWorldView implements WorldView {
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private Boundary gridBounds;
    private Float[] gridOffsetX;
    private Float[] gridOffsetY;
    private float imageScale;
    private Image staticLayerImage;

    public CanvasWorldView(Canvas canvas) {
        this.canvas = canvas;
        this.registerSizeListener();
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.gridBounds = new Boundary(new Vector2D(), new Vector2D());
    }

    public void setGridBounds(int width, int height) {
        this.gridBounds = new Boundary(
                new Vector2D(),
                new Vector2D(width - 1, height - 1)
        );
        this.updateViewSize();
    }

    public void updateViewSize() {
        this.graphicsContext.clearRect(
                0, 0,
                this.canvas.getWidth(), this.canvas.getHeight()
        );
    }

    public void put(Vector2D position, Image image) throws OutOfMapBoundsException {
        this.checkIfInBounds(position);
        this.drawAtGrid(position, image);
    }

    public Image get(Vector2D position) throws OutOfMapBoundsException {
        this.checkIfInBounds(position);
        return null;
    }

    private void drawAtGrid(Vector2D position, Image image) {
        float x = this.gridOffsetX[position.getX()];
        float y = this.gridOffsetY[position.getY()];
        float w = this.imageScale;
        float h = this.imageScale;
        this.graphicsContext.drawImage(image, x, y, w, h);
    }

    private void checkIfInBounds(Vector2D position) throws OutOfMapBoundsException {
        if (!this.gridBounds.isInBounds(position))
            throw new OutOfMapBoundsException(position);
    }

    private void registerSizeListener() {
        this.canvas.widthProperty()
                .addListener((observable, previous_value, current_value) -> CanvasWorldView.this.updateImageScale());
        this.canvas.heightProperty()
                .addListener((observable, previous_value, current_value) -> CanvasWorldView.this.updateImageScale());
    }

    private void updateImageScale() {
        Vector2D gridSize = this.gridBounds.getSize();
        float width = (float) this.canvas.getWidth();
        float height = (float) this.canvas.getHeight();
        float imageWidth = width / gridSize.getX();
        float imageHeight = height / gridSize.getY();
        this.imageScale = Math.min(imageWidth, imageHeight);
        this.updateGridOffsets();
    }

    private void updateGridOffsets() {
        Vector2D gridSize = gridBounds.getSize();
        float padX = (float) (this.canvas.getWidth() - gridSize.getX() * this.imageScale);
        float padY = (float) (this.canvas.getHeight() - gridSize.getY() * this.imageScale);
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
