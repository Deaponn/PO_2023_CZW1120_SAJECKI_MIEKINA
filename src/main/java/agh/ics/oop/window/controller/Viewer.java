package agh.ics.oop.window.controller;

import agh.ics.oop.entities.Animal;
import agh.ics.oop.entities.Plant;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.TextOverlay;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageAtlasSampler;
import agh.ics.oop.render.image.ImageMap;
import agh.ics.oop.render.overlay.BouncingImageOverlay;
import agh.ics.oop.render.overlay.StaticTextOverlay;
import agh.ics.oop.view.CanvasWorldView;
import agh.ics.oop.window.WindowController;
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
        worldView.getRoot().widthProperty()
                .bind(this.window.getRoot().widthProperty());
        worldView.getRoot().heightProperty()
                .bind(this.window.getRoot().heightProperty());

        this.worldRenderer = new WorldRenderer(
                this.getBundleItem("image_map", ImageMap.class).orElseThrow(),
                worldView
        );

        this.worldRenderer.imageSamplerMap.addImageAtlasSampler(
                "font0",
                "font0_atlas",
                (image) -> new ImageAtlasSampler(image, new Vector2D(10, 16)));

        BouncingImageOverlay testImageOverlay =
                new BouncingImageOverlay(new Vector2D(50, 50), "dvd0", 4f);
        testImageOverlay.setVelocity(new Vector2D(10, 10));

        this.worldRenderer.overlayList.add(testImageOverlay);

        String testText = "abcdefghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXYZ\n0123456789_?!-=><;:,.";
        TextOverlay testTextOverlay = new StaticTextOverlay(new Vector2D(10, 10), "font0_atlas", 2f, testText);
        this.worldRenderer.overlayList.add(testTextOverlay);

        this.worldMap = this.getBundleItem("world_map", WorldMap.class).orElseThrow();
        this.worldMap.mapChangeSubscribe(this);

        this.worldRenderer.setWorldMap(this.worldMap);

        // Testing code
        Thread thread = new Thread(() -> {
            long frameMillis = 400;
            try 
                this.worldMap.placeElement(new Plant(new Vector2D(5, 5), 5));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(8, 5), 5));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(6, 7), 4));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(7, 5), 5));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(4, 5), 5));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(9, 7), 4));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(10, 5), 5));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(2, 5), 5));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(1, 7), 4));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(10, 6), 5));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(3, 3), 5));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Plant(new Vector2D(2, 2), 4));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Animal(new Vector2D(2, 2), 4, 0, 4, null));
                Thread.sleep(frameMillis);
                this.worldMap.placeElement(new Animal(new Vector2D(3, 3), 4, 0, 2, null));

                while (!this.window.isClosed()) {
                    Thread.sleep(frameMillis);
                    this.worldMap.mapChangeNotify("update");
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });
        thread.start();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        this.worldRenderer.setWorldMap(worldMap);
        this.worldRenderer.renderView();
    }
}
