package agh.ics.oop.window.controller;

import agh.ics.oop.Configuration;
import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.Plant;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.ImageMap;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.resource.ResourceNotFoundException;
import agh.ics.oop.resource.Resources;
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
                this.getBundleItem("image_map", ImageMap.class).orElseThrow(),
                this.worldView
        );

        this.worldMap = this.getBundleItem("world_map", WorldMap.class).orElseThrow();
        this.worldMap.mapChangeSubscribe(this);
        // temporary test
        Configuration configuration = new Configuration();
        configuration.set(Configuration.Fields.MAP_WIDTH, 40);
        configuration.set(Configuration.Fields.MAP_HEIGHT, 30);
        try {
            Resources.serializeToXML("res/save/save0.xml", configuration);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
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
            this.worldMap.placeElement(new Animal(new Vector2D(2, 2), 4, Collections.emptyList(), configuration));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            this.worldMap.placeElement(new Animal(new Vector2D(3, 3), 2, Collections.emptyList(), configuration));
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
