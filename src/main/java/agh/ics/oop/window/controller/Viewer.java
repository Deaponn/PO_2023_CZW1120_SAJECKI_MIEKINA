package agh.ics.oop.window.controller;

import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.ImageMap;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.window.CanvasWorldView;
import agh.ics.oop.window.WindowController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class Viewer extends WindowController {
    @FXML
    public Canvas canvas;
    private WorldRenderer worldRenderer;
    private CanvasWorldView worldView;

    @Override
    public void start() {
        super.start();

        this.worldView = new CanvasWorldView(this.canvas);
        this.worldRenderer = new WorldRenderer(
                (ImageMap) this.getParam("image_map").orElseThrow(),
                this.worldView
        );
    }

    public void render() {
        this.worldRenderer.renderView((WorldMap) this.getParam("world_map").orElseThrow());
    }
}
