package agh.ics.oop.window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class Window<T extends WindowController> {
    private final Stage stage;
    private final String title;
    private final String layoutPath;

    private Pane root;
    private T controller;

    public Window(Stage stage, String title, String layoutPath) {
        this.stage = stage;
        this.title = title;
        this.layoutPath = layoutPath;

        this.init();
    }

    public Window(String title, String layoutPath) {
        this(new Stage(), title, layoutPath);
    }

    private void init() {
        this.stage.setTitle(this.title);

        try {
            this.loadView();
            this.setView(this.root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getFXMLResourceURL());

        this.root = loader.load();
        this.controller = loader.getController();
    }

    private void setView(Pane viewRoot) {
        Scene scene = new Scene(viewRoot);
        this.stage.setScene(scene);
        this.stage.minWidthProperty().bind(viewRoot.minWidthProperty());
        this.stage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    public T getController() {
        return this.controller;
    }

    public void send(String key, Object object) {
        this.controller.send(key, object);
    }

    public Optional<Object> getState(String key) {
        return this.controller.getState(key);
    }

    private URL getFXMLResourceURL() {
        return this.getClass()
                .getClassLoader()
                .getResource(this.layoutPath);
    }

    public void show() {
        this.stage.show();
    }
}
