package agh.ics.oop.window.controller;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.Plant;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.MapType;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.ImageMap;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.window.CanvasWorldView;
import agh.ics.oop.window.WindowController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

import java.util.Collections;

public class Viewer extends WindowController implements MapChangeListener {
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

        this.worldMap = (WorldMap) this.getParam("world_map").orElseThrow();
//        this.viewerUpdater = new ViewerUpdater(this);
        this.worldMap.mapChangeSubscribe(this);
        // temporary test
        Configuration conf = new Configuration(13, 8, MapType.STANDARD, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.2f, 1, false);
        Thread thread = new Thread(() -> {
            this.worldMap.placeElement(new Plant(new Vector2D(5, 5)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            this.worldMap.placeElement(new Plant(new Vector2D(8, 5)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            this.worldMap.placeElement(new Plant(new Vector2D(6, 7)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            this.worldMap.placeElement(new Animal(new Vector2D(2, 2), 4, Collections.emptyList(), conf));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            this.worldMap.placeElement(new Animal(new Vector2D(3, 3), 2, Collections.emptyList(), conf));
        });
        thread.start();
    }

    public void render() {
        this.worldRenderer.renderView(this.worldMap);
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(this::render);
    }
}
