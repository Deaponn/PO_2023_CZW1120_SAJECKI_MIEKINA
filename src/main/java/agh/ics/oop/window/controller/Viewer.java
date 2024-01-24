package agh.ics.oop.window.controller;

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
    private WorldRenderer worldRenderer;
    private RendererEngine rendererEngine;
    private WorldMap worldMap;
    private Simulation simulation;

    @Override
    public void start() {
        super.start();

        CanvasView worldView = new CanvasView(this.canvas);
        ViewInput viewInput = new ViewInput();
        viewInput.attach(worldView);

        worldView.getRoot().widthProperty()
                .bind(this.window.getRoot().widthProperty());
        worldView.getRoot().heightProperty()
                .bind(this.window.getRoot().heightProperty().subtract(50));

        this.delaySlider.valueProperty().addListener((observable, oldValue, newValue) -> this.handleDelayUpdate(newValue.intValue()));

        this.worldRenderer = new WorldRenderer(
                this.getBundleItem("image_map", ImageMap.class).orElseThrow(),
                worldView
        );

        this.worldRenderer.imageSamplerMap.addFontAtlasSampler(
                "font0",
                "font0_atlas",
                new Vector2D(10, 16));

        this.rendererEngine = this.getBundleItem("renderer_engine", RendererEngine.class)
                .orElseThrow();

        BouncingImageOverlay testImageOverlay =
                new BouncingImageOverlay(new Vector2D(50, 50), "dvd0", 4f);
        testImageOverlay.setVelocity(new Vector2D(12, 8));
        this.worldRenderer.overlayList.add(testImageOverlay);

        GridImageOverlay selectOverlay =
                new GridImageOverlay(new Vector2D(), "sel0");

        selectOverlay.gridPosition.bindTo(
                viewInput.mousePosition,
                worldView::getGridIndex,
                ReactivePropagate.LISTENER_ONLY);

        this.worldRenderer.overlayList.add(selectOverlay);

//        StaticImageOverlay playControlOverlay =
//                new StaticImageOverlay(new Vector2D(16, 16), "btnpause", 2f);
//        this.worldRenderer.overlayList.add(playControlOverlay);

        TextOverlay frameTimeOverlay =
                new StaticTextOverlay(new Vector2D(64, 16), "font0_atlas", 1f, "");
        this.worldRenderer.overlayList.add(frameTimeOverlay);

        frameTimeOverlay.text.bindTo(
                this.worldRenderer.frameRenderTime,
                (time) -> "frame_T [ms]: " + time / 1_000_000L);

        this.rendererEngine.addRenderer(this.worldRenderer);

        this.worldMap = this.getBundleItem("world_map", WorldMap.class).orElseThrow();
        this.worldMap.addEventSubscriber(this);

        this.worldRenderer.setWorldMap(this.worldMap);

        this.simulation = this.getBundleItem("simulation", Simulation.class).orElseThrow();

        StatisticsCollector collector = new StatisticsCollector();
        StatisticsExporter exporter = new StatisticsExporter(this.worldMap.getTitle());
        collector.subscribeTo(this.worldMap);
        collector.addEventSubscriber(exporter);

        this.window.setStageOnCloseRequest(event -> this.simulation.kill());
        this.window.setStageOnCloseRequest(event -> exporter.saveToFile());
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
