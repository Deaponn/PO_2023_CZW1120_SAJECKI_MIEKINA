package agh.ics.oop.window.controller;

import agh.ics.oop.Configuration;
import agh.ics.oop.model.EquatorialWorldMap;
import agh.ics.oop.model.MapType;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.render.ImageMap;
import agh.ics.oop.window.Bundle;
import agh.ics.oop.window.LayoutPath;
import agh.ics.oop.window.Window;
import agh.ics.oop.window.WindowController;

public class Launcher extends WindowController {
    public void launchViewer() {
        Window<Viewer> viewerWindow = new Window<>(
                "Viewer",
                LayoutPath.VIEWER.path
        );
        Configuration configuration = new Configuration(13, 8, MapType.STANDARD, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.2f, 1, false);
        WorldMap worldMap = new EquatorialWorldMap(configuration);
        ImageMap imageMap = new ImageMap("resources/gfx", "png");

        Bundle viewerBundle = new Bundle()
                .send("world_map", worldMap)
                .send("image_map", imageMap);
        viewerWindow.start(viewerBundle);
    }
}
