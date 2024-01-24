package agh.ics.oop.window.controller;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.*;
import agh.ics.oop.render.image.ImageMap;
import agh.ics.oop.render.RendererEngine;
import agh.ics.oop.resource.ResourceNotFoundException;
import agh.ics.oop.resource.Resources;
import agh.ics.oop.window.Bundle;
import agh.ics.oop.window.LayoutPath;
import agh.ics.oop.window.Window;
import agh.ics.oop.window.WindowController;
import agh.ics.oop.windowx.Toast;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Launcher extends WindowController {
    @FXML
    public TextField simulationTitle;
    private Configuration configuration = new Configuration();
    private final SimulationEngine simulationEngine = new SimulationEngine();
    private final RendererEngine rendererEngine = new RendererEngine();

    @Override
    public void start() {
        super.start();

        this.loadConfiguration();
    }

    public void launchConfigurator() {
        Window<Configurator> configuratorWindow = new Window<>(
                "Configurator",
                LayoutPath.CONFIGURATOR.path
        );

        Bundle configuratorBundle = new Bundle()
                .send("configuration", this.configuration);

        configuratorWindow.start(configuratorBundle);
        configuratorWindow.setOnClose(event -> this.saveConfiguration());
    }

    public void launchViewer() {
        Window<Viewer> viewerWindow = new Window<>(
                !this.simulationTitle.getCharacters().isEmpty() ? this.simulationTitle.getCharacters().toString()
                        : "No simulation name",
                LayoutPath.VIEWER.path
        );

        try {
            WorldMap worldMap;
            if (configuration.get(Configuration.Fields.MAP_TYPE) == MapType.STANDARD)
                worldMap = new EquatorialWorldMap(this.simulationTitle.getCharacters().toString(), this.configuration);
            else
                worldMap = new PoisonousPlantsWorldMap(this.simulationTitle.getCharacters().toString(), this.configuration);
            ImageMap imageMap = new ImageMap(new String[]{"res/gfx", "res/gfx/frogs"}, "png");
            Simulation simulation = this.simulationEngine.run(worldMap);

            Bundle viewerBundle = new Bundle()
                    .send("configuration", this.configuration)
                    .send("world_map", worldMap)
                    .send("image_map", imageMap)
                    .send("simulation", simulation)
                    .send("renderer_engine", this.rendererEngine);

            viewerWindow.start(viewerBundle);
        } catch (ResourceNotFoundException e) {
            this.window.showErrorAlert(
                    "Fatal error",
                    "Image map resource not found.\nReinstall the application to fix this issue.",
                    "Could not get resources " + e.getMessage()).setCloseWindow();
        }
    }

    private void loadConfiguration() {
        try {
            this.configuration = (Configuration) Resources.deserializeFromXML(
                    Configurator.configurationPath);
        } catch (ResourceNotFoundException e) {
            this.window.showToast("Running with new configuration", Toast.Duration.MEDIUM);
        }
    }

    public void cleanupOnClose() {
        this.simulationEngine.kill();
        this.rendererEngine.kill();
    }

    private void saveConfiguration() {
        Path folderPath = Path.of("res/save");
        if (!Files.exists(folderPath.toAbsolutePath()))
            new File("res/save").mkdirs();

        try {
            Resources.serializeToXML(
                    Configurator.configurationPath,
                    this.configuration);
        } catch (ResourceNotFoundException e) {
            this.window.showErrorAlert(
                    "Error",
                    "Could not save a configuration.",
                    "Could not save XML " + e.getMessage()).setDoNothing();
        }
    }
}
