package agh.ics.oop.window.controller;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.EquatorialWorldMap;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.ImageMap;
import agh.ics.oop.resource.ResourceNotFoundException;
import agh.ics.oop.window.Bundle;
import agh.ics.oop.window.LayoutPath;
import agh.ics.oop.window.Window;
import agh.ics.oop.window.WindowController;

public class Launcher extends WindowController {
    private final Configuration configuration = new Configuration();

    public void launchConfigurator() {
        Window<Configurator> configuratorWindow = new Window<>(
                "Configurator",
                LayoutPath.CONFIGURATOR.path
        );

        Bundle configuratorBundle = new Bundle()
                .send("configuration", this.configuration);

        configuratorWindow.start(configuratorBundle);
    }

    public void launchViewer() {
        Window<Viewer> viewerWindow = new Window<>(
                "Viewer",
                LayoutPath.VIEWER.path
        );

        try {
            WorldMap worldMap = new EquatorialWorldMap(this.configuration);
            ImageMap imageMap = new ImageMap("res/gfx", "png");

            Bundle viewerBundle = new Bundle()
                    .send("world_map", worldMap)
                    .send("image_map", imageMap);

            viewerWindow.start(viewerBundle);
        } catch (ResourceNotFoundException e) {
            this.window.showErrorAlert(
                    "Fatal error",
                    "Image map resource not found.\nReinstall the application to fix this issue.",
                    "Could not get resources " + e.getMessage()).setCloseWindow();
        }
    }
}
