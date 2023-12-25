package agh.ics.oop.window.controller;

import agh.ics.oop.window.LayoutPath;
import agh.ics.oop.window.Window;
import agh.ics.oop.window.WindowController;

import java.util.LinkedList;
import java.util.List;

public class Launcher extends WindowController {
    private final List<Window<Viewer>> viewerWindowList = new LinkedList<>();

    public void launchViewer() {
        Window<Viewer> viewerWindow = new Window<>(
                "Viewer",
                LayoutPath.VIEWER.path
        );
        viewerWindow.send("world_map", null);
        this.viewerWindowList.add(viewerWindow);
        viewerWindow.show();
    }
}
