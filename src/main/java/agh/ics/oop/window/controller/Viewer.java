package agh.ics.oop.window.controller;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.Plant;
import agh.ics.oop.model.MapType;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.ImageMap;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.window.CanvasWorldView;
import agh.ics.oop.window.WindowController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

import java.util.Collections;
import java.util.List;

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
        Configuration conf = new Configuration(13, 8, MapType.STANDARD, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.2f, 1, false);
        this.worldMap.placeElement(new Plant(new Vector2D(5, 5)));
        this.worldMap.placeElement(new Animal(new Vector2D(2, 2), 4, Collections.emptyList(), conf));
        this.worldMap.placeElement(new Animal(new Vector2D(3, 3), 2, Collections.emptyList(), conf));
        System.out.println(this.worldRenderer.imageMap.getImageKeys());
        System.out.println(this.worldRenderer.imageMap.getImage("sand3"));
        this.render();
    }

    public void render() {
        this.worldRenderer.renderView(this.worldMap);
    }
}
