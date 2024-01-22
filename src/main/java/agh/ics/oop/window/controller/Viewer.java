package agh.ics.oop.window.controller;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Simulation;
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
    private Simulation simulation;

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
        testImageOverlay.setVelocity(new Vector2D(12, 8));

//        this.worldRenderer.overlayList.add(testImageOverlay);

//        String testText = "abcdefghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXYZ\n0123456789_?!-=><;:,.";
//        TextOverlay testTextOverlay = new StaticTextOverlay(new Vector2D(10, 10), "font0_atlas", 2f, testText);
//        this.worldRenderer.overlayList.add(testTextOverlay);

        this.worldMap = this.getBundleItem("world_map", WorldMap.class).orElseThrow();
        this.worldMap.mapChangeSubscribe(this);

        this.worldRenderer.setWorldMap(this.worldMap);

        this.simulation = this.getBundleItem("simulation", Simulation.class).orElseThrow();

        this.window.setStageOnCloseRequest(event -> this.simulation.kill());
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        if (message.equals("step")) {
            this.worldRenderer.setWorldMap(worldMap);
            this.worldRenderer.renderView();
        }
    }

    private void onPauseButtonClick() {
        this.simulation.setIsPaused(!this.simulation.getIsPaused());
    }

    // set the amount of milliseconds to wait before subsequent step() calls
    private void onChangeUpdateDelay(int newDelay) {
        this.simulation.setUpdateDelay(newDelay);
    }
}
