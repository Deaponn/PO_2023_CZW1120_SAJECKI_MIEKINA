package agh.ics.oop.window.controller;

import agh.ics.oop.entities.Plant;
import agh.ics.oop.model.Vector2D;
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
    private WorldMap worldMap;

    @Override
    public void start() {
        super.start();

        this.worldView = new CanvasWorldView(this.canvas);
        this.worldRenderer = new WorldRenderer(
                (ImageMap) this.getParam("image_map").orElseThrow(),
                this.worldView
        );

        // temporary test
        this.worldMap = (WorldMap) this.getParam("world_map").orElseThrow();
        this.worldMap.placeElement(new Plant(new Vector2D(5, 5)));
        System.out.println(this.worldRenderer.imageMap.getImageKeys());
        System.out.println(this.worldRenderer.imageMap.getImage("sand3"));
        this.render();
    }

    public void render() {
        this.worldRenderer.renderView(this.worldMap);
    }
}
