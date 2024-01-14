package agh.ics.oop.window.controller;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.Plant;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.ImageMap;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.window.CanvasWorldView;
import agh.ics.oop.window.WindowController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class Viewer extends WindowController implements MapChangeListener {
    @FXML
    public Canvas canvas;
    private WorldRenderer worldRenderer;
    private WorldMap worldMap;

    @Override
    public void start() {
        super.start();

        CanvasWorldView worldView = new CanvasWorldView(this.canvas);
        this.worldRenderer = new WorldRenderer(
                this.getBundleItem("image_map", ImageMap.class).orElseThrow(),
                worldView
        );

        this.worldMap = this.getBundleItem("world_map", WorldMap.class).orElseThrow();
        this.worldMap.mapChangeSubscribe(this);

        Thread thread = new Thread(() -> {
            this.worldMap.placeElement(new Plant(new Vector2D(5, 5), 5));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            this.worldMap.placeElement(new Plant(new Vector2D(8, 5), 5));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            this.worldMap.placeElement(new Plant(new Vector2D(6, 7), 4));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            // TODO: shouldn't it be done using AnimalFactory? or something like worldMap.populate(configuration)
            //          i will implement above method soon
            this.worldMap.placeElement(new Animal(new Vector2D(2, 2), 4, null));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            this.worldMap.placeElement(new Animal(new Vector2D(3, 3), 2, null));
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
