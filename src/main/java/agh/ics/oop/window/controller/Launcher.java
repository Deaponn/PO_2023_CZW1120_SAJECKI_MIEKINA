package agh.ics.oop.window.controller;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.*;
import agh.ics.oop.render.image.ImageMap;
import agh.ics.oop.resource.ResourceNotFoundException;
import agh.ics.oop.resource.Resources;
import agh.ics.oop.window.Bundle;
import agh.ics.oop.window.LayoutPath;
import agh.ics.oop.window.Window;
import agh.ics.oop.window.WindowController;
import agh.ics.oop.windowx.Toast;

public class Launcher extends WindowController {
    private Configuration configuration = new Configuration();
    private final SimulationEngine simulationEngine = new SimulationEngine();

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
                "Viewer",
                LayoutPath.VIEWER.path
        );

        try {
            WorldMap worldMap;
            if (configuration.get(Configuration.Fields.MAP_TYPE) == MapType.STANDARD)
                worldMap = new EquatorialWorldMap(this.configuration);
            else worldMap = new PoisonousPlantsWorldMap((this.configuration));
            ImageMap imageMap = new ImageMap("res/gfx", "png");
            Simulation simulation = this.simulationEngine.runSimulation(worldMap);

            Bundle viewerBundle = new Bundle()
                    .send("configuration", this.configuration)
                    .send("world_map", worldMap)
                    .send("image_map", imageMap)
                    .send("simulation", simulation);

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
    }

    private void saveConfiguration() {
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
