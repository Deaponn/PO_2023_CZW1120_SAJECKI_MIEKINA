package agh.ics.oop.window.controller;

import agh.ics.oop.loop.Loop;
import agh.ics.oop.model.*;
import agh.ics.oop.render.TextOverlay;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageMap;
import agh.ics.oop.render.overlay.BouncingImageOverlay;
import agh.ics.oop.render.overlay.GridImageOverlay;
import agh.ics.oop.render.overlay.StaticTextOverlay;
import agh.ics.oop.render.renderer.RendererEngine;
import agh.ics.oop.util.ReactivePropagate;
import agh.ics.oop.view.CanvasView;
import agh.ics.oop.view.ViewInput;
import agh.ics.oop.window.WindowController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;

public class Viewer extends WindowController implements ObjectEventListener<WorldMap> {
    @FXML
    public Canvas canvas;
    @FXML
    public Slider delaySlider;

    private CanvasView canvasView;
    private ViewInput viewInput;
    private WorldRenderer worldRenderer;
    private RendererEngine rendererEngine;
    private WorldMap worldMap;
    private Loop rendererLoop;
    private Simulation simulation;

    @Override
    public void start() {
        super.start();

        this.delaySlider.valueProperty()
                .addListener((observable, oldValue, newValue) ->
                        this.handleDelayUpdate(newValue.intValue()));

        this.initRenderer();
        this.initOverlays();
        this.startRenderer();
        this.initWorldMap();
        this.setupSimulation();
    }

    private void initRenderer() {
        this.canvasView = new CanvasView(this.canvas);
        this.viewInput = new ViewInput();
        this.viewInput.attach(this.canvasView);

        this.canvasView.getRoot().widthProperty()
                .bind(this.window.getRoot().widthProperty());
        this.canvasView.getRoot().heightProperty()
                .bind(this.window.getRoot().heightProperty().subtract(50));

        this.worldRenderer = new WorldRenderer(
                this.getBundleItem("image_map", ImageMap.class).orElseThrow(),
                this.canvasView
        );

        this.worldRenderer.imageSamplerMap.addFontAtlasSampler(
                "font0",
                "font0_atlas",
                new Vector2D(10, 16));
    }

    private void initOverlays() {
        BouncingImageOverlay bouncyDVD =
                new BouncingImageOverlay(new Vector2D(50, 50), "dvd0", 4f);
        bouncyDVD.setVelocity(new Vector2D(12, 8));
        this.worldRenderer.overlayList.add(bouncyDVD);

        GridImageOverlay selectOverlay =
                new GridImageOverlay(new Vector2D(), "sel0");
        this.worldRenderer.overlayList.add(selectOverlay);

        selectOverlay.gridPosition.bindTo(
                this.viewInput.mousePosition,
                this.canvasView::getGridIndex,
                ReactivePropagate.LISTENER_ONLY);

        TextOverlay frameTimeOverlay =
                new StaticTextOverlay(new Vector2D(64, 16), "font0_atlas", 1f, "");
        this.worldRenderer.overlayList.add(frameTimeOverlay);

        frameTimeOverlay.text.bindTo(
                this.worldRenderer.frameRenderTime,
                (time) -> "frame_T [ms]: " + time / 1_000_000L);
    }

    private void startRenderer() {
        this.rendererEngine = this.getBundleItem("renderer_engine", RendererEngine.class)
                .orElseThrow();

        this.rendererLoop = this.rendererEngine.addRenderer(this.worldRenderer);
    }

    private void initWorldMap() {
        this.worldMap = this.getBundleItem("world_map", WorldMap.class)
                .orElseThrow();
        this.worldMap.addEventSubscriber(this);

        this.worldRenderer.setWorldMap(this.worldMap);
    }

    private void setupSimulation() {
        this.simulation = this.getBundleItem("simulation", Simulation.class)
                .orElseThrow();

        StatisticsCollector collector = new StatisticsCollector();
        StatisticsExporter exporter = new StatisticsExporter(this.worldMap.getTitle());
        collector.subscribeTo(this.worldMap);
        collector.addEventSubscriber(exporter);

        this.window.setStageOnCloseRequest(event -> {
            this.simulation.kill();
            this.rendererEngine.getLoopControl()
                    .removeLoop(this.rendererLoop);
            exporter.saveToFile();
        });
    }

    @Override
    public void sendEventData(WorldMap worldMap, String message) {
        if (message.equals("step")) {
            this.worldRenderer.setWorldMap(worldMap);
            rendererEngine.renderWorld(worldRenderer);
        }
    }

    @FXML
    private void handlePauseButtonClick() {
        if (this.simulation.isPaused())
            this.simulation.resume();
        else
            this.simulation.pause();
    }

    // set the amount of milliseconds to wait before subsequent step() calls
    @FXML
    private void handleDelayUpdate(int newDelay) {
        this.simulation.setUpdateDelay(newDelay);
    }
}
