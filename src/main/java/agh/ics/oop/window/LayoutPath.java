package agh.ics.oop.window;

public enum LayoutPath {
    LAUNCHER("fxml/launcher.fxml"),
    VIEWER("fxml/viewer.fxml"),
    CONFIGURATOR("fxml/configurator.fxml");

    public final String path;

    LayoutPath(String path) {
        this.path = path;
    }
}
