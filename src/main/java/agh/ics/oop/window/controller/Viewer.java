package agh.ics.oop.window.controller;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.Vector2D;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.TextOverlay;
import agh.ics.oop.render.WorldRenderer;
import agh.ics.oop.render.image.ImageMap;
import agh.ics.oop.render.overlay.GridImageOverlay;
import agh.ics.oop.render.overlay.StaticImageOverlay;
import agh.ics.oop.render.overlay.StaticTextOverlay;
import agh.ics.oop.view.CanvasView;
import agh.ics.oop.view.ViewInput;
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

        CanvasView worldView = new CanvasView(this.canvas);
        ViewInput viewInput = new ViewInput();
        viewInput.attach(worldView);

        worldView.getRoot().widthProperty()
                .bind(this.window.getRoot().widthProperty());
        worldView.getRoot().heightProperty()
                .bind(this.window.getRoot().heightProperty());

        this.worldRenderer = new WorldRenderer(
                this.getBundleItem("image_map", ImageMap.class).orElseThrow(),
                worldView
        );

        this.worldRenderer.imageSamplerMap.addFontAtlasSampler(
                "font0",
                "font0_atlas",
                new Vector2D(10, 16));

//        BouncingImageOverlay testImageOverlay =
//                new BouncingImageOverlay(new Vector2D(50, 50), "dvd0", 4f);
//        testImageOverlay.setVelocity(new Vector2D(12, 8));
//        this.worldRenderer.overlayList.add(testImageOverlay);

        GridImageOverlay selectOverlay =
                new GridImageOverlay(new Vector2D(), "sel0");

        selectOverlay.gridPosition.bindTo(
                viewInput.mousePosition,
                worldView::getGridIndex);

        selectOverlay.gridPosition.addOnChange(position -> {
                this.worldRenderer.renderOverlayViewLayer();
                System.out.println(position);
        });

        this.worldRenderer.overlayList.add(selectOverlay);

        StaticImageOverlay playControlOverlay =
                new StaticImageOverlay(new Vector2D(16, 16), "btnpause", 2f);
        this.worldRenderer.overlayList.add(playControlOverlay);

        TextOverlay frameTimeOverlay =
                new StaticTextOverlay(new Vector2D(64, 16), "font0_atlas", 1f, "");
        this.worldRenderer.overlayList.add(frameTimeOverlay);

        frameTimeOverlay.text.bindTo(
                this.worldRenderer.frameRenderTime,
                (time) -> "frame_T [ms]: " + time / 1_000_000L);

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
            this.worldRenderer.renderWorldViewLayer();
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
