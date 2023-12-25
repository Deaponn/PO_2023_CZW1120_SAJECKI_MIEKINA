package agh.ics.oop.window;

public enum LayoutPath {
    LAUCHER("fxml/launcher.fxml"),
    VIEWER("fxml/viewer.fxml");

    public final String path;

    LayoutPath(String path) {
        this.path = path;
    }
}
