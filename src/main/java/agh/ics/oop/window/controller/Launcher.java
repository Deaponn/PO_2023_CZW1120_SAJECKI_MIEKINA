package agh.ics.oop.window.controller;

import agh.ics.oop.window.LayoutPath;
import agh.ics.oop.window.Window;

import java.util.LinkedList;
import java.util.List;

public class Launcher {
    private final List<Window<Viewer>> viewerWindowList = new LinkedList<>();

    public void launchViewer() {
        Window<Viewer> viewerWindow = new Window<>(
                "Viewer",
                LayoutPath.VIEWER.path
        );
        this.viewerWindowList.add(viewerWindow);
        viewerWindow.show();
    }
}
